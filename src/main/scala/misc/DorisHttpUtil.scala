package misc

import action.{Data, Property, Response}
import com.intellij.openapi.ui.Messages

object DorisHttpUtil {

  def getTableMeta(user: String, password: String, host: String, port: String, yourdb: String, yourtb: String): Option[Response] = {
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
    Some(responseObj)
  }
}
