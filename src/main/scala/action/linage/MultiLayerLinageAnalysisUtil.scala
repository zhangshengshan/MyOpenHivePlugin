package action.linage

import com.zss.graph.const.{Color, NodeStyle}
import com.zss.graph.{Graph, Node, NodeElem}
import config.os.OsConfig

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

    val node = if (arrowDir.isDefined && arrowDir.get && preNode.nonEmpty) {
      new MyKVNode(
        "库表名",
        source
      ).asInstanceOf[NodeElem]
        .setStyle(NodeStyle.FILLED)
        .setColor(Color("lightblue"))
        .asInstanceOf[MyKVNode] // TODO: 需要根据source的type来设置颜色
    } else if (arrowDir.isDefined && !arrowDir.get && preNode.nonEmpty) {
      new MyKVNode(
        "库表名",
        source
      ).asInstanceOf[NodeElem]
        .setStyle(NodeStyle.FILLED)
        .setColor(Color("lightyellow"))
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
    val stack = new collection.mutable.Stack[String]
    val paris: List[TargetSourcePair] =
      input.map(x => TargetSourcePair(x._1, x._2))
    val paris_another = input.map(x => TargetSourcePair(x._2, x._1))

    val graph = new Graph(fileName)
    findDependency(paris, source, stack, graph, None, Some(true)).toSet
      .foreach(println)
    stack.clear()
    findDependency(paris_another, source, stack, graph, None, Some(false)).toSet
    graph.render(
      fileName,
      outputDir,
      Some(true),
      Some(OsConfig.winDotPath),
      Some(true)
    )
  }
}
