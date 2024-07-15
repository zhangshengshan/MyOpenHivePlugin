package action.linage

import another.KVNode
import com.zss.graph.{Graph, Node}
import config.os.OsConfig

object MultiLayerLinageAnalysisUtil {

  import scala.collection.mutable.ListBuffer
  case class TargetSourcePair(target: String, source: String)

  def findDependency(
      list: List[TargetSourcePair],
      source: String,
      stack: collection.mutable.Stack[String],
      graph: Graph,
      preNode: Option[Node[KVNode]]
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

    val curNode: Node[KVNode] = Node(
      KVNode(
        "库表名",
        source
      )
    )
    graph.add(
      curNode
    )
    if (preNode.isDefined) {
      preNode.get.connectTo(curNode)
    }
    val targets: List[String] = list.filter(_.source == source).map(_.target)
    listBuffer ++= targets
    targets.foreach(target => {
      listBuffer ++= findDependency(list, target, stack, graph, Some(curNode))
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
    val graph_anaother = new Graph(fileName + "_another")
    findDependency(paris, source, stack, graph, None).toSet.foreach(println)

    stack.clear()
    findDependency(paris_another, source, stack, graph, None).toSet
    graph.render(
      fileName,
      outputDir,
      Some(true),
      Some(OsConfig.winDotPath),
      Some(true)
    )
//    findDependency(paris_another, source, stack, graph_anaother, None).toSet
//      .foreach(println)
//    graph_anaother.render(
//      fileName + "_another",
//      outputDir,
//      Some(true),
//      Some(OsConfig.winDotPath),
//      Some(true)
//    )
  }
}
