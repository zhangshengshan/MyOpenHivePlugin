package action.linage

import com.intellij.openapi.util.SystemInfo
import com.zss.graph.const.{Color, NodeStyle}
import com.zss.graph.{Graph, Node, NodeElem}
import config.os.OsConfig
import misc.ExcelObject
import openbrowser.HierachyConfigStrategy

import scala.collection.mutable

object MultiLayerLinageAnalysisUtil {

  import scala.collection.mutable.ListBuffer
  case class TargetSourcePair(target: String, source: String)

  def findDependency(
      list: List[TargetSourcePair],
      source: String,
      stack: collection.mutable.Stack[String],
      graph: Graph,
      preNode: Option[Node[MyKVNode]],
      arrowDir: Option[Boolean] = Some(true)
  ): List[String] = {
    val listBuffer = new ListBuffer[String]
    if (stack.contains(source)) {
      println("stack duplicated!")
      println(stack.toList.reverse.mkString("=>") + "=>" + source)
      // 说明该source节点曾经在栈中出现过，说明有环
      return listBuffer.toList
    }

    // 将当前节点压入栈中
    stack.push(source)

    val config = HierachyConfigStrategy.getColorConfig

    val node = if (arrowDir.isDefined && arrowDir.get && preNode.nonEmpty) {
      new MyKVNode(
        "库表名",
        source
      ).asInstanceOf[NodeElem]
        .setStyle(NodeStyle.FILLED)
        .setColor(Color(config.getOrElse("bgcolor1", "lightyellow")))
        .asInstanceOf[MyKVNode] // TODO: 需要根据source的type来设置颜色
    } else if (arrowDir.isDefined && !arrowDir.get && preNode.nonEmpty) {
      new MyKVNode(
        "库表名",
        source
      ).asInstanceOf[NodeElem]
        .setStyle(NodeStyle.FILLED)
        .setColor(Color(config.getOrElse("bgcolor2", "lightgreen")))
        .asInstanceOf[MyKVNode] // TODO: 需要根据source的type来设置颜色
    } else {
      new MyKVNode(
        "库表名",
        source
      )
    }

    val curNode: Node[MyKVNode] = Node(
      node
    )

    graph.add(
      curNode
    )
    if (preNode.isDefined) {
      if (arrowDir.isDefined && arrowDir.get)
        preNode.get.connectTo(curNode)
      else
        curNode.connectTo(preNode.get)
    }
    val targets: List[String] = list.filter(_.source == source).map(_.target)
    listBuffer ++= targets
    targets.foreach(target => {
      arrowDir match {
        case Some(true) =>
          listBuffer ++= findDependency(
            list,
            target,
            stack,
            graph,
            Some(curNode),
            Some(true)
          )
        case _ =>
          listBuffer ++= findDependency(
            list,
            target,
            stack,
            graph,
            Some(curNode),
            Some(false)
          )
      }
    })

    // 将当前节点从栈中弹出
    stack.pop()
    return listBuffer.toList
  }

  def plotDependency(
      input: List[(String, String)],
      outputDir: Option[String],
      fileName: String,
      source: String
  ): Unit = {
    val stack = new mutable.Stack[String]
    val paris: List[TargetSourcePair] =
      input.map(x => TargetSourcePair(x._1, x._2))
    val paris_another = input.map(x => TargetSourcePair(x._2, x._1))

    val graph = new Graph(fileName)
    findDependency(paris, source, stack, graph, None, Some(true)).toSet

    stack.clear()
    val set = findDependency(
      paris_another,
      source,
      stack,
      graph,
      None,
      Some(false)
    ).toSet

    val excel = new ExcelObject
    val excelFilePath = outputDir.get + "\\" + fileName + ".xlsx"
    excel.saveToExcel(
      set.toList.map(x => List(x)),
      List("依赖关系"),
      excelFilePath
    )

    openFileDispatchOsSystem(excelFilePath)

    graph.render(
      fileName,
      outputDir,
      Some(true),
      Some(OsConfig.winDotPath),
      Some(true)
    )
  }

  def openFileDispatchOsSystem(excelFilePath: String) = {
    if (SystemInfo.isWindows) {
      Runtime.getRuntime.exec("cmd /c start " + excelFilePath)
    } else if (SystemInfo.isMac) {
      Runtime.getRuntime.exec("open " + excelFilePath)
    } else if (SystemInfo.isLinux) {
      Runtime.getRuntime.exec("xdg-open " + excelFilePath)
    }
  }
}
