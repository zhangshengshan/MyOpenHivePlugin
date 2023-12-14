package openbrowser

import another.GraphBuilder
import com.intellij.openapi.util.SystemInfo
import com.zss.graph.Graph
import config.os.OsConfig
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
      Some("/usr/local/Cellar/graphviz/9.0.0/bin/dot")
    )

    if (SystemInfo.isMac) {
      graph.render(
        fileName,
        Some(OsConfig.macOutputPath),
        Some(openOrNot),
        Some(OsConfig.macDotPath),
        Some(false)
      )
    } else {
      graph.render(
        fileName,
        Some(OsConfig.winOutputPath),
        Some(openOrNot),
        Some(OsConfig.winDotPath),
        Some(false)
      )
    }
  }
}
