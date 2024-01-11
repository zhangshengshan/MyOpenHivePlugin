package action

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}
import hierachyconfig.MyConfigurable

case class Author( login: String, subscriptions_url: String, organizations_url: String)


class GetDorisSchemaAction extends AnAction {
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

    val yourdb = "yourdb"
    val yourtb = "youtb"

    import java.util.Base64
    val encoded =
      Base64.getEncoder.encodeToString((user + ":" + password).getBytes)
    val headers = Map("Authorization" -> s"Basic $encoded")

    val url = s"http://$host:$port/api/$yourdb/$yourtb/_schema"
//      val response = requests.get(url, headers = headers)
    val jsongString = os.read(
      os.Path(
        "/Users/zhangshengshan/Desktop/MyOpenHivePlugin/ammonite-releases.json"
      )
    )
    val jsonValue = ujson.read(jsongString)

    implicit val authorRW = upickle.default.macroRW[Author]

    val author : Author = upickle.default.read[Author](jsonValue(0).obj("author"))
    println(author)
    println(author.login)

//    try {
//      jsonValue.arr
//        .take(2)
//        .foreach(item => {
//          println(item.obj("node_id"))
//        })
//    } catch {
//      case e: Exception => println(e)
//    }
//
  }
}


// define a case class as above json
