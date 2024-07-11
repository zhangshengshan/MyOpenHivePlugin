package action

import another.KVNode
import com.zss.graph.{Graph, Node}

object Hello {

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
        "table",
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

  def main(args: Array[String]): Unit = {

    val stack = new collection.mutable.Stack[String]

    val a = TargetSourcePair("b", "a")
    val b = TargetSourcePair("c", "a")
    val c = TargetSourcePair("d", "a")
    val d = TargetSourcePair("e", "b")
    val e = TargetSourcePair("f", "c")
    val f = TargetSourcePair("a", "f")
    val h = TargetSourcePair("h", "f")
    val g = TargetSourcePair("g", "a")
    val k = TargetSourcePair("a", "a")
    val l1 = TargetSourcePair("l1", "a")
    val l12 = TargetSourcePair("l2", "l1")
    val l23 = TargetSourcePair("l3", "l2")
    val l34 = TargetSourcePair("l4", "l3")
    val l4a = TargetSourcePair("a", "l4")
    val lgf = TargetSourcePair("g", "f")
    val l4g = TargetSourcePair("g", "l4")

    val paris = List(a, b, c, d, e, f, h, g, l1, l12, l23, l34, l4a, lgf, l4g)

    val graph = new Graph("aaaaaa")

    findDependency(paris, "a", stack, graph, None).toSet.foreach(println)

    graph.render(
      "aaaaaa",
      Some("/Users/zhangshengshan/Desktop/MyOpenHivePlugin"),
      Some(true),
      Some("/usr/local/bin/dot"),
      None
    )
  }

}
