package action

import com.intellij.build.events.BuildEventsNls.Message
import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}
import com.intellij.openapi.ui.Messages
import hierachyconfig.MyConfigurable
import misc.ClipBoardUtil

case class Property(name: String, aggregation_type: String, comment: String, `type`: String)
case class Data(keysType: String, properties: List[Property], status: Int)
case class Response(msg: String, code: Int, data: Data, count: Int)

class GetDorisSchemaAction extends AnAction {
  def genDorisSelectQuery(responseObj: Response, db:String, tb:String): String = {
    val selectList = responseObj.data.properties.map(item => {
      item.name + " AS " + item.name + " -- " + item.comment  +"\n"
    }).mkString(",")
    s"select $selectList from $db.$tb"
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

    // intellij pop up a input to get the db and table name
    val str = Messages.showInputDialog(
      "Please input the db and table name",
      "Input Dialog",
      Messages.getQuestionIcon)

    val strings = str.split("\\.")
    val yourdb = strings(0)
    val yourtb = strings(1)

    println(yourdb)
    println(yourtb)

    import java.util.Base64
    val encoded = Base64.getEncoder.encodeToString((user + ":" + password).getBytes)
    val headers = Map("Authorization" -> s"Basic $encoded")

    val url = s"http://$host:$port/api/$yourdb/$yourtb/_schema"
    val response = requests.get(url, headers = headers)
    val jsonValue = ujson.read(response.text())

    implicit val authorRW = upickle.default.macroRW[Author]
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
