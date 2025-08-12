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
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.{PsiDocumentManager, PsiFile}
import com.zss.graph.{Graph, Node}
import config.os.OsConfig
import hierachyconfig.MyConfigurable
import misc.ClipBoardUtil

case class Property(
    name: String,
    aggregation_type: String,
    comment: String,
    `type`: String
)
case class Data(keysType: String, properties: List[Property], status: Int)
case class Response(msg: String, code: Int, data: Data, count: Int)

class GetDorisSchemaAction extends AnAction {
  final val DOT = "\\."

  /** 生成 Doris 的 SELECT 查询语句
    * @param responseObj 包含表结构信息的响应对象
    * @param db 数据库名称
    * @param tb 表名
    * @return 格式化后的 SQL 查询字符串
    */
  private def genDorisSelectQuery(
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
      "SQL Style",
      Messages.getQuestionIcon
    )

    def getAliasName(item: Property): String = {
      if (item.comment == null || item.comment.isEmpty) {
        item.name
      } else {
        "`" + item.comment + "`"
      }
    }

    val selectList = responseObj.data.properties
      .map { item =>
        val aliasName =
          if (sqlStyle == Messages.NO) getAliasName(item) else item.name
        val spaces = fillAsSpaces(item.name)
        val commentSpaces = fillCommentSpaces(aliasName)
        s"${item.name}$spaces AS $aliasName$commentSpaces -- ${item.comment}$SEP"
      }
      .mkString("\t,")

    s"SELECT${SEP}\t $selectList${SEP}FROM${SEP}\t$db.$tb ;"
  }
  override def actionPerformed(anActionEvent: AnActionEvent): Unit = {
    val value: MyConfigurable = MyConfigurable.getInstance()
    val host = value.getHost
    val port = value.getPort
    val user = value.getUser
    val password = value.getPassword
    val dotPathConfig = value.getDotPathConfig
    val editor: Editor = anActionEvent.getData(CommonDataKeys.EDITOR)

    val selectText = editor.getSelectionModel.getSelectedText
    val model = editor.getCaretModel
    val offset = model.getOffset
    val project: Project = editor.getProject
    val psiFile: PsiFile =
      PsiDocumentManager getInstance project getPsiFile editor.getDocument;

    val expandObj = EditorUtil.getCaretUnderCursor(editor)

    var db = ""
    var tb = ""
    if (
      expandObj != null && expandObj
        .contains(".") && expandObj.split("\\.").length == 2
    ) {
      val strings = expandObj.split("\\.")
      db = strings(0)
      tb = strings(1)
    } else if (
      selectText != null && selectText
        .contains(".") && selectText.split("\\.").length == 2
    ) {
      val strings = selectText.split("\\.")
      db = strings(0)
      tb = strings(1)
    } else {
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
      return
    }

    implicit val propertyRW = upickle.default.macroRW[Property]
    implicit val dataRW = upickle.default.macroRW[Data]
    implicit val responseRW = upickle.default.macroRW[Response]
    println(jsonValue)
    val responseObj: Response = upickle.default.read[Response](jsonValue)
    responseObj.data.properties.foreach(item => {
      println(item.name)
    })

    val fieldComments =
      responseObj.data.properties.map(item => (item.name, item.comment)).toList
    val graphNode = Node(
      ClusDbTbNode(
        "doris",
        yourdb,
        yourtb,
        Some("https://www.baidu.com"),
        Some(false),
        None,
        None
      )
    )

    graphNode.x.asInstanceOf[ClusDbTbNode].setFieldComment(fieldComments)
    val graph = Graph("doris" + yourdb + yourtb)
    graph.add(graphNode)
    graphNode.toGraphViz(true)

    val descriptor: FileChooserDescriptor =
      new FileChooserDescriptor(false, true, false, false, false, false)

    val dialog: FileChooserDialog = FileChooserFactory
      .getInstance()
      .createFileChooser(descriptor, project, null)

    val option: Option[VirtualFile] = dialog.choose(project).headOption
    val outPutDir: String = {
      if (option.isDefined) option.get.getPath
      else if (SystemInfo.isMac) OsConfig.macOutputPath
      else OsConfig.winOutputPath
    }

    val dotPath = dotPathConfig
    val isWin = SystemInfo.isWindows

    // 此处要检查 dotPath是否存在, 而且是操作系统中是否真的存在这个文件
    if (FileUtil.exists(dotPath)) {
      val fileName: String = Messages.showInputDialog(
        project,
        """请输入文件名""",
        "文件名",
        Messages.getQuestionIcon,
        graphNode.x.getTableAlias(),
        null
      )
      graph.render(
        fileName,
        Some(outPutDir),
        Some(true),
        Some(dotPath),
        Some(isWin)
      )
    }

    val sql = genDorisSelectQuery(responseObj, yourdb, yourtb)
    Messages.showInfoMessage(sql, "Generated Doris SQL")
    ClipBoardUtil.copyToClipBoard(sql)
  }
}

// define a case class as above json
