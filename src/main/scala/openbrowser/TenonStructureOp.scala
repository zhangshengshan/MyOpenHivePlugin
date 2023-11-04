package openbrowser


import another.GraphBuilder
import com.zss.graph.Graph
import hierachyconfig.MyConfigurable

object TenonStructureOp {

  def genTenonStructureGraph(param: String): Unit = {
    param
      .replace("(", "_")
      .replace(")", "_")
      .replace(".", "_")
      .replace("（", "_")
      .replace("）", "_")
      .replace("=", "_")

    def genUrl(ss: Array[String]) = {
      val db = ss(1)
      val tb = ss(2)
      s"https://www.baidu.com"
    }

    val config = Map("mode" → MyConfigurable.getInstance().getTennonShowMode)
    val graph: Graph = GraphBuilder.make(param, Some(genUrl), Some(config))
    val outPutDir = MyConfigurable.getInstance().getOutputPath
    val openOrNot = MyConfigurable.getInstance().isOpenAfterGen

    val fileName = param.split("\\/").last.split("\\.").head

    graph.render(
      fileName,
      Some(outPutDir),
      Some(openOrNot),
      Some("/usr/local/bin")
    )
  }

}

object mytest extends App {
  val p1 = List("1", "2", "3")

  def f(args: String*): String = {
    args.toList.mkString("_")
  }
  println(f(p1: _*))
}
