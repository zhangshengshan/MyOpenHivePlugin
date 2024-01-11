package action

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent}
import hierachyconfig.MyConfigurable

class GetDorisSchemaAction extends AnAction{
  override def actionPerformed(anActionEvent: AnActionEvent): Unit =  {
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
      val encoded = Base64.getEncoder.encodeToString((user + ":" + password).getBytes)
      val headers = Map("Authorization" -> s"Basic $encoded")

      val url = s"http://$host:$port/api/$yourdb/$yourtb/_schema"
      val response = requests.get(url, headers = headers)

      println(response.text())
  }
}
