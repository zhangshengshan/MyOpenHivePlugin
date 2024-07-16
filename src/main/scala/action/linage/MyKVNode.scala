package action.linage

import another.KVNode
import com.zss.graph.NodeElem



class MyKVNode(key: String, value: String) extends NodeElem{
  override def content: String = s" ${key} : ${value} "
}