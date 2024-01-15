package action

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent, CommonDataKeys}
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.ui.Messages
import hierachyconfig.MyConfigurable
import misc.ClipBoardUtil

case class Property(name: String, aggregation_type: String, comment: String, `type`: String)
case class Data(keysType: String, properties: List[Property], status: Int)
case class Response(msg: String, code: Int, data: Data, count: Int)

class GetDorisSchemaAction extends AnAction {
  /**
   * @param responseObj
   * @param db
   * @param tb
   * @return
   */
  def genDorisSelectQuery(responseObj: Response, db:String, tb:String): String = {

    val maxLength = responseObj.data.properties.map(item => item.name.length + 2).max
    val maxCommentLength = responseObj.data.properties.map(item => item.name.length * 2 + 9 ).max

    val SEP = System.lineSeparator()

    def fillAsSpaces(str: String): String = {
      val spaceNum = maxLength - str.length
      " " * spaceNum
    }

    def fillCommentSpaces(str:String): String = {
      val spaceNum = maxCommentLength - 2 * str.length
      " " * spaceNum
    }

    val selectList = responseObj.data.properties.map(item => {
      item.name + fillAsSpaces(item.name) +" AS " + item.name + " -- " + fillCommentSpaces(item.name)+ item.comment + SEP
    }).mkString("\t,")
    s"SELECT${SEP}\t$selectList${SEP}FROM${SEP}\t$db.$tb ;"
  }
  override def actionPerformed(anActionEvent: AnActionEvent): Unit = {
    val value: MyConfigurable = MyConfigurable.getInstance()
    println(value.getHost)
    println(value.getPort)
    println(value.getUser)
    println(value.getPassword)

    val host = value.getHost
    val port = value.getPort
    val user = value.getUser
    val password = value.getPassword
    //    anneng_ods.ods_bduan_eam_power_station_base_dt_bduan_dashboard

    val editor: Editor = anActionEvent.getData(CommonDataKeys.EDITOR)

    val selectText = editor.getSelectionModel.getSelectedText

    var db = ""
    var tb = ""
    if (selectText != null && selectText.contains(".") && selectText.split("\\.").length == 2) {
      val strings = selectText.split("\\.")
      db = strings(0)
      tb = strings(1)
    } else {
      // intellij pop up a input to get the db and table name
      val str = Messages.showInputDialog(
        "Please input the db and table name",
        "Input Dialog",
        Messages.getQuestionIcon)
      val strings = str.split("\\.")
      db = strings(0)
      tb = strings(1)
    }
    val yourdb = db
    val yourtb = tb
    println(yourdb)
    println(yourtb)

    import java.util.Base64
    val encoded = Base64.getEncoder.encodeToString((user + ":" + password).getBytes)
    val headers = Map("Authorization" -> s"Basic $encoded")

    val url = s"http://$host:$port/api/$yourdb/$yourtb/_schema"
    val response = requests.get(url, headers = headers)
    val jsonValue = ujson.read(response.text())

    if(jsonValue("code").num != 0) {
      Messages.showInfoMessage(jsonValue("msg").str, "Error")
      return
    }

    implicit val propertyRW = upickle.default.macroRW[Property]
    implicit val dataRW = upickle.default.macroRW[Data]
    implicit val responseRW = upickle.default.macroRW[Response]
    println(jsonValue)
    val responseObj : Response = upickle.default.read[Response](jsonValue)
    responseObj.data.properties.foreach(item => {
      println(item.name)
    })
    val sql = genDorisSelectQuery(responseObj, yourdb, yourtb)

    Messages.showInfoMessage(sql, "Generated Doris SQL")
    ClipBoardUtil.copyToClipBoard(sql)
  }
}


// define a case class as above json
