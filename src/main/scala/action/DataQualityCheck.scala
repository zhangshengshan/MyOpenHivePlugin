package action

import action.util.EditorUtil
import another.ClusDbTbNode
import com.intellij.openapi.actionSystem.{
  AnAction,
  AnActionEvent,
  CommonDataKeys
}
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileChooser.{
  FileChooserDescriptor,
  FileChooserDialog,
  FileChooserFactory
}
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.{PsiDocumentManager, PsiFile}
import com.zss.graph.{Graph, Node}
import config.os.OsConfig
import hierachyconfig.MyConfigurable
import misc.ClipBoardUtil
import myui.MyMultiChoiceDialog

import scala.jdk.CollectionConverters.{CollectionHasAsScala, SeqHasAsJava}
//case class Property(
//    name: String,
//    aggregation_type: String,
//    comment: String,
//    `type`: String
//)
//case class Data(keysType: String, properties: List[Property], status: Int)
//case class Response(msg: String, code: Int, data: Data, count: Int)

class DataQualityCheck extends AnAction {
  final val DOT = "\\."

  /** @param responseObj
    * @param db
    * @param tb
    * @return
    */
  def GenDataCheckSQL(
      responseObj: Response,
      db: String,
      tb: String
  ): String = {

    val maxLength =
      responseObj.data.properties.map(item => item.name.length + 2).max
    val maxCommentLength = 2 * maxLength + 4

    val SEP = System.lineSeparator()

    def fillAsSpaces(str: String): String = {
      val spaceNum = maxLength - str.length
      " " * spaceNum
    }

    def fillCommentSpaces(str: String): String = {
      val spaceNum = maxCommentLength - str.length + maxLength
      " " * spaceNum
    }

    val sqlStyle = Messages.showYesNoDialog(
      "Do you want to use Doris SQL style?",
      s""" FOR ${db}.${tb} """,
      Messages.getQuestionIcon
    )

    def getAliasName(item: Property): String = {
      if (item.comment == null || item.comment.isEmpty) {
        item.name
      } else {
        "`" + item.comment + "`"
      }
    }

    val groupByList = responseObj.data.properties.map(item => item.name).asJava

    val dialog = new MyMultiChoiceDialog(groupByList, s"""${db}.${tb}""")
    dialog.show()
    val selectedGroupFields = dialog.getSelectedOptions.asScala.toList.distinct

    val selectList = responseObj.data.properties
      .filter(item =>
        (
          item.`type`.toUpperCase.contains("DECIMAL")
            || item.`type`.toUpperCase.contains("INT")
            || item.`type`.toUpperCase.contains("FLOAT")
            || item.`type`.toUpperCase.contains("DOUBLE")
        ) && (
          !selectedGroupFields.contains(item.name)
        )
      )
      .map { item =>
        {
          val isDateType = item.`type`.toUpperCase.contains("DATE")
          val stddevExpr = if (isDateType) s"STDDEV(${item.name})" else "NULL"
          s"""COUNT(DISTINCT ${item.name}) AS COUNT_DISINTCT_${item.name} -- 计算COUNT( DISTINCT ${item.name})
             |,COUNT(CASE WHEN ${item.name} IS NULL THEN 1 ELSE NULL END) AS ${item.name}_NULL_CNT -- 计算${item.name}NULL值个数
             |,STDDEV( ${item.name} ) AS STDDEV_${item.name} -- 获取${item.name}的STDDEV
             |,AVG(${item.name}) AS AVG_${item.name} -- 获取${item.name}的AVG
             |,SUM(${item.name}) AS SUM_${item.name} -- 获取${item.name}的SUM
             |,MIN(${item.name}) AS MIN_${item.name} -- 获取${item.name}的最小值
             |,MAX(${item.name}) AS MAX_${item.name} -- 获取${item.name}的最小值
             |""".stripMargin
//          |,PERCENTILE(${item.name}, 0.25) AS PERCENTILE_25_${item.name}
//          |,PERCENTILE(${item.name}, 0.5) AS PERCENTILE_50_${item.name}
//          |,PERCENTILE(${item.name}, 0.75) AS PERCENTILE_75_${item.name}
        }
      }
      .mkString("\n,")

    if (selectedGroupFields.isEmpty)
      s"${SEP}SELECT${SEP}\t $selectList${SEP}FROM${SEP}\t$db.$tb ;${SEP}${SEP}"
    else
      s"""
         |
         |SELECT
         |${selectedGroupFields.mkString(",\n")} 
         |, ${selectList}
         |FROM 
         |  $db.$tb
         |GROUP BY 
         |  ${selectedGroupFields.mkString(",")}
         |  ;
         |
         |""".stripMargin
  }
  override def actionPerformed(anActionEvent: AnActionEvent): Unit = {
    val (host, port, user, password) = getConfigValues()

    val editor: Editor = anActionEvent.getData(CommonDataKeys.EDITOR)

    val selectText = editor.getSelectionModel.getSelectedText
    Messages.showInfoMessage(selectText, "selected text!")
    val model = editor.getCaretModel
    val offset = model.getOffset
    val project: Project = editor.getProject
    val psiFile: PsiFile =
      PsiDocumentManager getInstance project getPsiFile editor.getDocument;

    val expandObj = EditorUtil.getCaretUnderCursor(editor)

    var db = ""
    var tb = ""
    if (selectText.split(System.lineSeparator()).length > 1) {

      Messages.showInfoMessage(
        "SELECT MULTIPLE TABLES, SEPARATED BY LINE BREAKS",
        "Data Quality Check"
      )

      var sqlList: List[String] = List()
      selectText
        .split(System.lineSeparator())
        .map(_.strip())
        .foreach(item => {
          val strings = item.split("\\.")
          db = strings(0)
          tb = strings(1)
          val str: Option[String] =
            getSingleTableDataCheckSQL(host, port, user, password, db, tb)
          str match {
            case Some(value) =>
              Messages.showInfoMessage(value, "Data Quality Check")
              sqlList = sqlList :+ value
            case None =>
          }
        })

      Messages.showInfoMessage(
        s"""共计生成SQL数量 ${sqlList.size}, 输入的表格数量为  ${selectText.split(System.lineSeparator()).length}""",
        "Data Quality Check"
      )
      ClipBoardUtil.copyToClipBoard(sqlList.mkString("\n"))
    } else if (
      expandObj != null && expandObj
        .contains(".") && expandObj.split("\\.").length == 2
    ) {

      Messages.showInfoMessage(
        "SELECT A SINGLE TABLE",
        "Data Quality Check"
      )
      val strings = expandObj.split("\\.")
      db = strings(0)
      tb = strings(1)

      val str: Option[String] =
        getSingleTableDataCheckSQL(host, port, user, password, db, tb)

      str match {
        case Some(value) =>
          Messages.showInfoMessage(value, "Data Quality Check")
          ClipBoardUtil.copyToClipBoard(value)
        case None =>
      }

    } else if (
      selectText != null && selectText
        .contains(".") && selectText.split("\\.").length == 2
    ) {
      Messages.showInfoMessage(
        "SELECT A SINGLE TABLE",
        "Data Quality Check"
      )
      val strings = selectText.split("\\.")
      db = strings(0)
      tb = strings(1)

      val str: Option[String] =
        getSingleTableDataCheckSQL(host, port, user, password, db, tb)

      str match {
        case Some(value) =>
          Messages.showInfoMessage(value, "Data Quality Check")
          ClipBoardUtil.copyToClipBoard(value)
        case None =>
      }
    } else {
      Messages.showInfoMessage(
        "SELECT A SINGLE TABLE",
        "Data Quality Check"
      )
      // intellij pop up a input to get the db and table name
      val str = Messages.showInputDialog(
        "Please input the db and table name",
        "Input Dialog",
        Messages.getQuestionIcon
      )
      val strings = str.split("\\.")
      db = strings(0)
      tb = strings(1)
    }

  }

  private def getSingleTableDataCheckSQL(
      host: String,
      port: String,
      user: String,
      password: String,
      db: String,
      tb: String
  ): Option[String] = {
    val yourdb = db
    val yourtb = tb
    println(yourdb)
    println(yourtb)

    import java.util.Base64
    val encoded =
      Base64.getEncoder.encodeToString((user + ":" + password).getBytes)
    val headers = Map("Authorization" -> s"Basic $encoded")

    val url = s"http://$host:$port/api/$yourdb/$yourtb/_schema"
    val response = requests.get(url, headers = headers)
    val jsonValue = ujson.read(response.text())

    if (jsonValue("code").num != 0) {
      Messages.showInfoMessage(jsonValue("msg").str, "Error")
      return None
    }

    implicit val propertyRW = upickle.default.macroRW[Property]
    implicit val dataRW = upickle.default.macroRW[Data]
    implicit val responseRW = upickle.default.macroRW[Response]
    println(jsonValue)
    val responseObj: Response = upickle.default.read[Response](jsonValue)
    responseObj.data.properties.foreach(item => {
      println(item.name)
    })

    val sql = GenDataCheckSQL(responseObj, yourdb, yourtb)

    Some(sql)
  }

  private def getConfigValues(): (String, String, String, String) = {
    val value: MyConfigurable = MyConfigurable.getInstance()
    (value.getHost, value.getPort, value.getUser, value.getPassword)
  }
}

// define a case class as above json
