package action.editor.tech

import action.util.ExceptionHandle
import com.google.gson.{Gson, GsonBuilder, JsonArray, JsonNull, JsonObject, JsonParser, JsonPrimitive}
import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent, CommonDataKeys}
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.{Document, Editor, SelectionModel}
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import misc.{ClipBoardUtil, MybatisSqlRenderer}

import scala.collection.mutable
import scala.jdk.CollectionConverters._

/**
 * 渲染 MyBatis 动态 SQL（方案B：基于官方 MyBatis XMLLanguageDriver）。
 *
 * 用途：测试/联调时，把接口 SQL 中的 <if>/<when>/<choose>/<foreach>/<trim>/<where>/<set>
 *       以及 #{param} / ${param} 按用户输入的参数值渲染成一条可执行的纯 SQL，
 *       自动去掉动态标签，方便直接丢进 Doris 客户端执行。
 *
 * 交互：
 *   1. 读取编辑器选中文本（未选中则用整个文档）；
 *   2. 自动扫描 SQL 中的全部参数名，弹框预填 "参数名="（每行一个）；
 *      支持两种输入，自动识别：
 *       a) 整段 JSON：{"agent_id":1547,"ids":["A","B"]}（支持数组/嵌套，供 foreach 使用）；
 *       b) 键值对：每行 "参数名=值"（留空视为 null，对应 <if> 块自动删除）。
 *   3. 渲染前以 "参数名=值" 逐行展示解析结果，可修改后确认；
 *   4. 展示渲染结果，选择「替换编辑区 / 复制到剪贴板 / 取消」。
 */
class RenderMybatisSqlAction extends AnAction {

  /** 没有编辑器或文件为空时禁用入口，避免点了才提示。 */
  override def update(e: AnActionEvent): Unit = {
    val editor: Editor = e.getData(CommonDataKeys.EDITOR)
    val hasText: Boolean = editor != null && editor.getDocument != null && editor.getDocument.getText.trim.nonEmpty
    e.getPresentation.setEnabledAndVisible(hasText)
  }

  override def actionPerformed(e: AnActionEvent): Unit = {
    val project: Project = e.getData(CommonDataKeys.PROJECT)
    val editor: Editor = e.getData(CommonDataKeys.EDITOR)
    if (editor == null) {
      Messages.showWarningDialog(project, "请先打开并选中一个编辑器中的 SQL", "未找到编辑器")
      return
    }

    val document: Document = editor.getDocument
    val selection: SelectionModel = editor.getSelectionModel
    val hasSelection: Boolean = selection.getSelectionStart != selection.getSelectionEnd

    // 1) 取待渲染文本：优先选中区，否则全文档
    val source: String =
      if (hasSelection) selection.getSelectedText
      else document.getText
    if (source == null || source.trim.isEmpty) {
      Messages.showWarningDialog(project, "选区内无可渲染的 SQL 文本", "空输入")
      return
    }

    try {
      // 2) 收集参数名，生成填写预留：每行 "参数名=值"（值留空）
      val paramNames: List[String] = MybatisSqlRenderer.collectParams(source).distinct
      val prefill: String = paramNames.map(p => s"$p=").mkString("\n")
      var paramMap: Map[String, Any] = Map.empty[String, Any]

      if (paramNames.nonEmpty) {
        val tip: String =
          "请填写参数值，每行一个 \"参数名=值\"（留空视为 null，对应 <if> 块会自动删除）。\n" +
            "也可整段贴入 JSON，如 {\"agent_id\":1547,\"ids\":[\"A\",\"B\"]}。\n\n" +
            "识别到的参数：" + paramNames.mkString("、")
        val input: String = Messages.showMultilineInputDialog(
          project,
          tip,
          "MyBatis 动态 SQL 参数（" + paramNames.size + " 个）",
          prefill, // 预填：每行 "参数名="，用户填值即可
          Messages.getQuestionIcon,
          null
        )
        if (input == null) return // 用户取消
        paramMap = parseInputParams(input)

        // 2.1) 无效行提示（缺少 = 的行会被忽略，提醒用户但不阻断）
        val badLines: List[String] = invalidLines(input)
        if (badLines.nonEmpty) {
          Messages.showWarningDialog(
            project,
            "以下行缺少 \"=\"，已忽略：\n" + badLines.mkString("\n"),
            "部分参数未生效"
          )
        }

        // 2.2) 渲染前预览确认：以 "参数名=值" 逐行展示，可修改后确认
        if (paramMap.nonEmpty) {
          val preview: String = toKeyValuePreview(paramMap)
          val confirm: String = Messages.showMultilineInputDialog(
            project,
            "预览解析出的参数（每行 参数名=值，可修改后确认）：",
            "参数预览确认",
            preview,
            Messages.getQuestionIcon,
            null
          )
          if (confirm == null) return // 用户取消
          if (confirm.trim != preview.trim) paramMap = parseInputParams(confirm)
        }
      }

      // 3) 渲染
      val rendered: String = MybatisSqlRenderer.render(source, paramMap)

      // 4) 输出：先展示渲染结果，再选择「替换 / 复制 / 取消」
      val shown: String =
        "渲染结果预览（" + rendered.length + " 字符）：\n\n" +
          truncate(rendered, 1200) + "\n\n" +
          "「替换编辑区」：覆盖当前" + (if (hasSelection) "选中内容" else "整个文件") + "\n" +
          "「复制到剪贴板」：保留原文，仅复制结果"
      val choice: Int = Messages.showYesNoCancelDialog(
        project,
        shown,
        "渲染完成",
        "替换编辑区",
        "复制到剪贴板",
        "取消",
        Messages.getQuestionIcon
      )

      choice match {
        case Messages.YES  => replaceEditor(project, document, selection, hasSelection, rendered)
        case Messages.NO   =>
          ClipBoardUtil.copyToClipBoard(rendered)
          Messages.showInfoMessage(project, "已复制到剪贴板（" + rendered.length + " 字符）", "完成")
        case _             => // 取消：什么都不做
      }
    } catch {
      case ex: Throwable => ExceptionHandle.handleException(ex)
    }
  }

  /** 找出键值对输入中缺少 "=" 的非空行（JSON 输入不校验）。 */
  private def invalidLines(input: String): List[String] = {
    val t: String = Option(input).getOrElse("").trim
    if (t.isEmpty || t.startsWith("{")) Nil
    else t.split("\n").iterator.map(_.trim).filter(l => l.nonEmpty && l.indexOf('=') <= 0).toList
  }

  /** 渲染结果太长时截断显示，保留头尾。 */
  private def truncate(s: String, maxLen: Int): String = {
    if (s.length <= maxLen) s
    else s.take(maxLen / 2) + "\n...（中间省略 " + (s.length - maxLen) + " 字符）...\n" + s.takeRight(maxLen / 2)
  }

  /** 自动识别输入内容是 整段 JSON 还是 每行参数名=值。 */
  private def parseInputParams(input: String): Map[String, Any] = {
    val t: String = Option(input).getOrElse("").trim
    if (t.isEmpty) Map.empty[String, Any]
    else if (t.startsWith("{")) parseJsonParams(t) // 整段 JSON
    else parseKeyValueParams(t)                    // 每行 参数名=值
  }

  /** 用 Gson 解析整段 JSON（支持数组/嵌套，供 foreach/复杂参数使用；顶层须为对象）。 */
  private def parseJsonParams(json: String): Map[String, Any] = {
    val obj: JsonObject = JsonParser.parseString(json).getAsJsonObject
    val map: mutable.Map[String, Any] = mutable.Map.empty[String, Any]
    obj.entrySet().asScala.foreach { e =>
      map.put(e.getKey, jsonValue(e.getValue))
    }
    map.toMap
  }

  /** 递归把 Gson JsonElement 转成 MyBatis 可直接使用的值。 */
  private def jsonValue(el: com.google.gson.JsonElement): Any = el match {
    case p: JsonPrimitive =>
      if (p.isBoolean) java.lang.Boolean.valueOf(p.getAsBoolean)
      else if (p.isNumber) {
        val d = p.getAsBigDecimal
        // 整数值用 Long，避免小数被截断
        if (d.stripTrailingZeros.scale() <= 0) java.lang.Long.valueOf(d.longValue())
        else java.lang.Double.valueOf(d.doubleValue())
      }
      else p.getAsString
    case a: JsonArray =>
      val list: java.util.List[Any] = new java.util.ArrayList[Any]()
      a.asScala.foreach(x => list.add(jsonValue(x)))
      list
    case _: JsonNull => null
    case o: JsonObject =>
      // 嵌套对象按字符串兜底（避免复杂类型引发 MyBatis 绑定问题）
      o.toString
    case _ => null
  }

  /** 每行 “参数名=值”，值转 int/boolean/原字符串。空值返回 null；以 # 开头的行视为注释。 */
  private def parseKeyValueParams(input: String): Map[String, Any] = {
    val map: mutable.Map[String, Any] = mutable.Map.empty[String, Any]
    input.split("\n").foreach { rawLine =>
      val line: String = rawLine.trim
      if (line.nonEmpty && !line.startsWith("#")) {
        val idx: Int = line.indexOf('=')
        if (idx > 0) {
          val name: String = line.substring(0, idx).trim
          val raw: String = line.substring(idx + 1).trim
          if (name.nonEmpty) {
            if (raw.isEmpty) map.put(name, null)
            else map.put(name, convertValue(raw))
          }
        }
      }
    }
    map.toMap
  }

  /** 把参数 Map 渲染为 "参数名=值" 逐行文本，用于预览确认；数组/对象用 JSON 表示以便回填解析。 */
  private def toKeyValuePreview(paramMap: Map[String, Any]): String = {
    paramMap.map { case (k, v) => s"$k=${displayValue(v)}" }.mkString("\n")
  }

  /** 预览时单个值的展示：null、字符串原样、集合/数组用 JSON、其它转字符串。 */
  private def displayValue(v: Any): String = v match {
    case null                        => "null"
    case s: String                   => s
    case xs: java.util.Collection[_] => jsonGson.toJson(xs)
    case arr: Array[_]               => jsonGson.toJson(arr.map(_.asInstanceOf[AnyRef]))
    case other                       => String.valueOf(other)
  }

  private lazy val jsonGson: Gson = new GsonBuilder().disableHtmlEscaping().create()

  /** 值类型转换：JSON 数组/对象 -> 递归解析；true/false/null；整数/小数；其余按字符串（去引号）。 */
  private def convertValue(raw: String): Any = {
    val r: String = raw.trim
    if (r.startsWith("[") || r.startsWith("{")) {
      // 值本身是 JSON（如 ids=["A","B"]），交给 Gson 解析，失败再按字符串兜底
      try jsonValue(JsonParser.parseString(r))
      catch { case _: Throwable => stripQuotes(r) }
    } else if (r.equalsIgnoreCase("true")) java.lang.Boolean.TRUE
    else if (r.equalsIgnoreCase("false")) java.lang.Boolean.FALSE
    else if (r.equalsIgnoreCase("null")) null
    else if (r.matches("[-+]?\\d+")) java.lang.Long.valueOf(r)
    else if (r.matches("[-+]?\\d+(\\.\\d+)?")) java.lang.Double.valueOf(r)
    else stripQuotes(r)
  }

  private def stripQuotes(s: String): String = {
    if (s.length >= 2 && ((s.startsWith("'") && s.endsWith("'")) || (s.startsWith("\"") && s.endsWith("\"")))) {
      s.substring(1, s.length - 1)
    } else s
  }

  /** 在写命令里把渲染结果写回编辑区。 */
  private def replaceEditor(project: Project, document: Document, selection: SelectionModel,
                            hasSelection: Boolean, rendered: String): Unit = {
    WriteCommandAction.runWriteCommandAction(project, new Runnable {
      override def run(): Unit = {
        if (hasSelection) {
          val start: Int = selection.getSelectionStart
          val end: Int = selection.getSelectionEnd
          document.replaceString(start, end, rendered)
        } else {
          document.setText(rendered)
        }
      }
    })
    Messages.showInfoMessage(project, "替换完成", "完成")
  }
}