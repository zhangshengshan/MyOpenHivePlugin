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
 *   2. 自动扫描 SQL 中的全部参数名，弹出输入框。支持两种输入方式：
 *       a) 整段 JSON：直接贴入 {"agent_id":1547,"ids":["A","B"]}（自动识别，支持数组/嵌套，for foreach）；
 *       b) 键值对：每行 “参数名=值”（留空视为 null，对应 <if> 块自动删除）。
 *   3. 渲染前展示解析出的参数 JSON 让用户确认/微调；
 *   4. 选择输出方式：替换当前编辑区 / 复制到剪贴板。
 */
class RenderMybatisSqlAction extends AnAction {

  /** 输出到剪贴板 */
  private final val MODE_CLIPBOARD: String = "复制到剪贴板"

  /** 替换当前编辑区（选中区或全文） */
  private final val MODE_REPLACE: String = "替换当前编辑区"

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
      // 2) 收集参数名，生成预填：默认给出 键值对 格式，并提示可整段贴 JSON
      val paramNames: List[String] = MybatisSqlRenderer.collectParams(source).distinct
      val prefill: String = paramNames.map(p => s"$p=").mkString("\n")
      val tip: String =
        "支持两种输入：\n" +
          "  A) 整段 JSON：{" + paramNames.mkString(",\"", "\",\"", "\"") + "}\n" +
          "  B) 每行 参数名=值（留空视为 null，对应 <if> 块删除）\n\n" + prefill
      val input: String = Messages.showMultilineInputDialog(
        project,
        tip,
        "MyBatis 动态 SQL 参数",
        "参数名=值",
        Messages.getQuestionIcon,
        null
      )

      if (input == null) return // 用户取消

      // 3) 解析用户输入（自动识别 JSON / 键值对）
      var paramMap: Map[String, Any] = parseInputParams(input)

      // 3.1) 渲染前预览确认（展示解析出的参数 JSON，可微调；修改后重新解析）
      val confirmJson: String = toJsonPreview(paramMap)
      val confirm: String = Messages.showMultilineInputDialog(
        project,
        "预览解析出的参数（可在此修改后再确认）：",
        "参数预览确认",
        confirmJson,
        Messages.getQuestionIcon,
        null
      )
      if (confirm == null) return // 用户取消
      // 若用户在预览框里做了修改，按最新内容重新解析
      if (confirm.trim != confirmJson.trim) paramMap = parseInputParams(confirm)

      // 4) 渲染
      val rendered: String = MybatisSqlRenderer.render(source, paramMap)

      // 5) 选择输出方式
      val mode: String = Messages.showEditableChooseDialog(
        "已渲染完成。选择输出方式：",
        "渲染结果输出",
        Messages.getQuestionIcon,
        Array(MODE_REPLACE, MODE_CLIPBOARD),
        MODE_REPLACE,
        null
      )

      if (MODE_CLIPBOARD == mode) {
        ClipBoardUtil.copyToClipBoard(rendered)
        Messages.showInfoMessage(project, "已复制到剪贴板", "完成")
      } else {
        replaceEditor(project, document, selection, hasSelection, rendered)
      }
    } catch {
      case ex: Throwable => ExceptionHandle.handleException(ex)
    }
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

  /** 每行 “参数名=值”，值转 int/boolean/原字符串。空值返回 null。 */
  private def parseKeyValueParams(input: String): Map[String, Any] = {
    val map: mutable.Map[String, Any] = mutable.Map.empty[String, Any]
    input.split("\n").foreach { line =>
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
    map.toMap
  }

  /** 把参数 Map 序列化为紧凑 JSON 用于预览。 */
  private def toJsonPreview(paramMap: Map[String, Any]): String = {
    new GsonBuilder().disableHtmlEscaping().create().toJson(paramMap.asJava)
  }

  /** 值类型启发式转换：整数 -> Int 不适用 Scala，用 Long；小数 -> Double；true/false -> Boolean；'a,b'/'a,b' 字符串默认原样。 */
  private def convertValue(raw: String): Any = {
    val r: String = raw.trim
    r.toLowerCase match {
      case "true"  => java.lang.Boolean.TRUE
      case "false" => java.lang.Boolean.FALSE
      case "null"  => null
      case _ =>
        // 纯数字
        if (r.matches("[-+]?\\d+")) java.lang.Long.valueOf(r)
        else if (r.matches("[-+]?\\d+(\\.\\d+)?")) java.lang.Double.valueOf(r)
        else stripQuotes(r)
    }
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