package misc

import java.util
import java.util.regex.{Matcher, Pattern}

import org.apache.ibatis.mapping.{BoundSql, ParameterMapping}
import org.apache.ibatis.scripting.xmltags.XMLLanguageDriver
import org.apache.ibatis.session.Configuration

import scala.collection.mutable
import scala.jdk.CollectionConverters._

/**
 * MyBatis 动态 SQL 渲染器（方案B：复用官方 XMLLanguageDriver + SqlSourceBuilder）。
 *
 * 用途：把接口 SQL（含 <if>/<choose>/<when>/<otherwise>/<foreach>/<trim>/<where>/<set>
 *       以及 #{param} / ${param}）按给定的参数值渲染成一条可执行的纯 SQL，供测试/联调。
 *
 * 出参约定：
 *   - collectParams(sql) : 提取 SQL 中出现的所有参数名（<if test>/<when test>/#{}/${}/foreach item），去重返回。
 *   - render(sql, params) : 渲染后返回纯 SQL。动态标签按参数值求值（null -> <if> 块丢弃），
 *                           #{param} 内联为真实字面量，${param} 由 MyBatis 直接内联。
 */
object MybatisSqlRenderer {

  // ---- 参数名提取（供 UI 生成输入框） --------------------------------------
  private val IF_PARAM: Pattern = Pattern.compile("(?i)(?:if|when)\\s+test\\s*=\\s*\"([^\"]*)\"")
  private val HASH_PARAM: Pattern = Pattern.compile("#\\{\\s*([A-Za-z_][\\w.-]*)\\s*[},]")
  private val DOLLAR_PARAM: Pattern = Pattern.compile("\\$\\{\\s*([A-Za-z_][\\w.-]*)\\s*}")
  private val FOREACH_ITEM: Pattern = Pattern.compile("(?i)<foreach[^>]*?\\bitem\\s*=\\s*\"([^\"]*)\"")

  /**
   * 从动态 SQL 中提取所有参数名（按出现顺序、去重）。
   * @param sql MyBatis 接口 SQL（可含 <script> 标记）
   * @return 参数名列表
   */
  def collectParams(sql: String): List[String] = {
    val seen: mutable.LinkedHashSet[String] = mutable.LinkedHashSet.empty

    // 1) <if test="..."> / <when test="..."> 里的参数表达式 -> 拆出变量名
    val m1: Matcher = IF_PARAM.matcher(sql)
    while (m1.find()) {
      val testExpr = m1.group(1)
      // test 表达式如：agent_id != null、agent_id != null and agent_id != ''、name == 'x'、list != null
      // 提取其中被比较/判空的标识符，排除 OGNL 关键字与字面量
      PATTERN_TOKENS.split(testExpr).foreach { tok =>
        val t = tok.trim.stripPrefix("'").stripPrefix("\"").stripSuffix("'").stripSuffix("\"")
        if (t.nonEmpty && IDENTIFIER.matcher(t).matches() && !OGNL_KEYWORDS.contains(t.toLowerCase)) seen.add(t)
      }
    }

    // 2) #{param} / ${param}
    val m2: Matcher = HASH_PARAM.matcher(sql)
    while (m2.find()) seen.add(m2.group(1))
    val m3: Matcher = DOLLAR_PARAM.matcher(sql)
    while (m3.find()) seen.add(m3.group(1))

    // 3) <foreach item="item">
    val m4: Matcher = FOREACH_ITEM.matcher(sql)
    while (m4.find()) seen.add(m4.group(1))

    seen.toList
  }

  /** 拆分 test 表达式为候选标识符：按空白、括号、比较/逻辑运算符、字符串字面量切开 */
  private val PATTERN_TOKENS: Pattern = Pattern.compile("\\s+|[\\(]|\\)|!=|==|&&|\\|\\||<|>|>=|<=|'[^']*'")

  private val IDENTIFIER: Pattern = Pattern.compile("[A-Za-z_][A-Za-z0-9_.-]*")

  /** OGNL 表达式关键字/占位，不作为参数收集 */
  private val OGNL_KEYWORDS: Set[String] = Set(
    "null", "true", "false", "and", "or", "not", "in", "is", "eq", "neq", "lt", "gt", "new",
    "instanceof", "this", "super", "class", "static", "contains", "empty", "notempty"
  )

  // ---- MyBatis 渲染核心 -----------------------------------------------------
  private lazy val configuration: Configuration = new Configuration()
  private lazy val languageDriver: XMLLanguageDriver = new XMLLanguageDriver()

  /**
   * 渲染动态 SQL。
   * @param sql    接口动态 SQL
   * @param params 参数->值。值为 null 表示未提供，对应的 <if> 将被丢弃，#{param} 渲染为 NULL。
   * @return 可执行的纯 SQL
   */
  def render(sql: String, params: Map[String, Any]): String = {
    // MyBatis 要求以 <script> 包裹动态片段；先做 XML 转义，避免 SQL 中的比较符 <、& 破坏 XML 解析
    val wrapped: String = "<script>" + escapeForXml(sql.trim) + "</script>"
    val paramJava: util.Map[String, Any] = new util.HashMap[String, Any]()
    params.foreach { case (k, v) => paramJava.put(k, if (v == null) null else v) }

    val renderedSql: String =
      try {
        val sqlSource = languageDriver.createSqlSource(configuration, wrapped, classOf[util.Map[String, Any]])
        val boundSql: BoundSql = sqlSource.getBoundSql(paramJava)
        inlineParams(boundSql.getSql, boundSql, paramJava)
      } catch {
        case e: Throwable =>
          throw new IllegalStateException(friendlyError(e, sql), e)
      }
    tidy(renderedSql)
  }

  /** 提取 <foreach collection="xxx"> 的集合参数名。 */
  private def foreachCollections(sql: String): List[String] = {
    val out: mutable.LinkedHashSet[String] = mutable.LinkedHashSet.empty
    val m: Matcher = FOREACH_COLLECTION.matcher(sql)
    while (m.find()) out.add(m.group(1).trim)
    out.toList
  }

  private val FOREACH_COLLECTION: Pattern =
    Pattern.compile("(?i)<foreach\\b[^>]*?\\bcollection\\s*=\\s*\"([^\"]*)\"")

  /** 提取异常链上的全部消息，便于识别根因（MyBatis 常把真实原因包在 cause 里）。 */
  private def allMessages(e: Throwable): String = {
    val sb: StringBuilder = new StringBuilder()
    var cur: Throwable = e
    var guard: Int = 0
    while (cur != null && guard < 10) {
      sb.append(Option(cur.getMessage).getOrElse(cur.getClass.getSimpleName)).append('\n')
      cur = cur.getCause
      guard += 1
    }
    sb.toString
  }

  /** 给 MyBatis 的底层异常补充常见原因提示，方便定位。 */
  private def friendlyError(e: Throwable, sql: String): String = {
    val msg: String = allMessages(e)
    val lower: String = msg.toLowerCase
    val hints: List[String] =
      if (msg.contains("Error creating document instance") || msg.contains("must be well-formed"))
        List("SQL 中含未转义的 XML 特殊字符（如 < 或 &），或动态标签未正确闭合")
      else if (msg.contains("OGNL") || msg.contains("ExpressionSyntaxException") || msg.contains("ExpressionEvaluationException"))
        List("动态标签的 test 表达式（OGNL）语法有误，请检查 <if>/<when> 的条件")
      else if (lower.contains("evaluated to a null value") || lower.contains("nullpointerexception") || lower.contains("cannot invoke") || lower.contains("is null")) {
        val cols: List[String] = foreachCollections(sql)
        if (cols.nonEmpty)
          List(
            "foreach 的集合参数（" + cols.mkString("、") + "）未赋值或为 null",
            "请在参数里给它一个数组值（如 " + cols.head + "=[\"A\",\"B\"]），",
            "或用 <if test=\"" + cols.head + " != null\"> 包住这段 foreach"
          )
        else List("某个参数为 null 但被直接用于表达式，请检查 #{}/<if> 的写法")
      }
      else Nil
    if (hints.isEmpty) msg.stripTrailing()
    else msg.stripTrailing() + "\n\n可能原因：\n" + hints.map("- " + _).mkString("\n")
  }

  /** 清理渲染结果的排版：去掉行尾空白，把连续空行压缩为一行。 */
  private def tidy(sql: String): String = {
    val sb: StringBuilder = new StringBuilder()
    var blankPending: Boolean = false
    var started: Boolean = false
    sql.split("\n", -1).foreach { raw =>
      val line: String = raw.replaceAll("[ \\t]+$", "")
      if (line.trim.isEmpty) {
        if (started) blankPending = true
      } else {
        if (blankPending) { sb.append('\n'); blankPending = false }
        sb.append(line).append('\n')
        started = true
      }
    }
    sb.toString.stripTrailing()
  }

  /**
   * 转义 SQL 文本中的 XML 特殊字符，同时保留 MyBatis 动态标签。
   * - `<` 只在后面不是标签起始字符（字母 / `/` / `!` / `?`）时转义，保护 <if>/</if>/<![CDATA[/<?xml；
   * - `&` 仅在不是已有实体（&amp; / &lt; / &#123; 等）时转义。
   */
  private def escapeForXml(sql: String): String = {
    val afterLt: String = RAW_LT.matcher(sql).replaceAll("&lt;")
    RAW_AMP.matcher(afterLt).replaceAll("&amp;")
  }

  private val RAW_LT: Pattern = Pattern.compile("<(?![A-Za-z/!?])")
  private val RAW_AMP: Pattern = Pattern.compile("&(?!(?:amp|lt|gt|quot|apos|#\\d+|#x[0-9a-fA-F]+);)")

  /**
   * 把 boundSql.getSql() 中的 `?` 按 ParameterMapping 顺序替换为真实字面量。
   * （MyBatis 渲染后的 SQL 中，`?` 只对应 #{param} 占位；字符串常量原样保留。）
   * 字符串加单引号并转义内部单引号；数字/布尔原样；null 输出 NULL。
   */
  private def inlineParams(sql: String, boundSql: BoundSql, params: util.Map[String, Any]): String = {
    val mappings: List[ParameterMapping] = boundSql.getParameterMappings match {
      case null  => Nil
      case coll: java.util.List[ParameterMapping] => coll.asScala.toList
    }
    val sb: StringBuilder = new StringBuilder()
    val src: String = sql
    var i: Int = 0
    var mIdx: Int = 0
    while (i < src.length) {
      val ch: Char = src.charAt(i)
      if (ch == '?') {
        if (mIdx < mappings.length) {
          val property: String = mappings(mIdx).getProperty // 形如 "name"、"name.value"、"__frch_c_0"
          // 优先取 MyBatis foreach 生成的附加参数（如 __frch_c_0）；否则从顶层参数按首段取
          val value: Any =
            if (boundSql.hasAdditionalParameter(property)) {
              boundSql.getAdditionalParameter(property)
            } else {
              val key: String = property.split("\\.")(0).split("\\[")(0)
              if (params.containsKey(key)) params.get(key) else null
            }
          sb.append(literal(value))
          mIdx += 1
        } else {
          sb.append('?')
          mIdx += 1
        }
        i += 1
      } else {
        sb.append(ch)
        i += 1
      }
    }
    sb.toString
  }

  /** 把 java/scala 值渲染为 SQL 字面量。 */
  private def literal(v: Any): String = v match {
    case null                      => "NULL"
    case s: String                 => "'" + s.replaceAll("'", "''") + "'"
    case b: java.lang.Boolean      => b.toString
    case n: java.lang.Number       => n.toString
    case d: java.util.Date         => "'" + d + "'"
    case xs: java.lang.Iterable[_] => xs.asScala.map(literal).mkString(",")
    case arr: Array[_]             => arr.map(literal).mkString(",")
    case other                     => "'" + String.valueOf(other).replaceAll("'", "''") + "'"
  }
}