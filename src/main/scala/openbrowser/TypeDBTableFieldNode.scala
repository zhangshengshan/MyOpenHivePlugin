package openbrowser

import com.zss.graph.NodeElem

case class TypeDBTableFieldNode(
                                 db: String,
                                 tb: String,
                                 column: String,
                                 typeName: Option[String] = None,
                                 currentNode: Option[Boolean] = None) extends NodeElem {

  var targetNode: Boolean = false
  // TODO: fieldComment 需要设计一下。
  var fieldComment: List[List[String]] = Nil

  /**
   * 在这里务必加上column作为唯一键，否则graph无法添加重复的节点
   */
  override def content: String = s"db:${db}tb:$tb:column:$column"

  def setFieldComment(fieldComment: List[List[String]]): Unit = {
    this.fieldComment = fieldComment
  }

  override def htmlTable: String = {

    /*   if (this.fieldComment == Nil || this.fieldComment.size == 1 ) {
         // TODO: 这里需要设计一下
         super.htmlTable
       } else {*/
    val color: String = if (this.targetNode) "#ffffff" else HierachyConfigStrategy.getBGColorForNodeType(typeName)

    val imgfile: String = HierachyConfigStrategy.getIconByNodeType(typeName)
    val rowSparn: Int = this.fieldComment.length + 4

    val href = s"https://www.baidu.com"
    // TODO: 这里需要填充正确的链接
    val dbtbdesc: String =
      s"<TR>" +
        s"<TD BGCOLOR=\"$color\" ROWSPAN=\"$rowSparn\"><IMG SRC=\"$imgfile\" HREF=\"$href\"/></TD><TD BGCOLOR=\"$color\" ROWSPAN=\"$rowSparn\" HREF=\"$href\">库表<B>$db.$tb</B>" +
        s"<BR/><FONT COLOR=\"orange\">所属数仓层次</FONT>" +
        s"<BR/><U><B><FONT COLOR=\"red\">$column</FONT></B></U>" +
        s"</TD></TR> "

    // TODO: 这里有问题
    if (this.fieldComment == Nil || this.fieldComment.size <= 1)
      s""" <<TABLE CELLBORDER="1" CELLSPACING="0" CELLPADDING="4">$dbtbdesc</TABLE>> """
    else {
      val headerMeta: List[String] = this.fieldComment.head
      val tableHeader: String = "<TR>" + headerMeta.map { x: String => s"<TD>$x</TD>" }.mkString + "</TR>"
      val value: _root_.scala.collection.immutable.List[_root_.scala.Predef.String] = this.fieldComment.tail.map(
        (x: List[String]) => {
          x.map((item: String) => s""" <TD>$item</TD> """).mkString("<TR>", "\r", "</TR>")
        }
      )
      val str: String = value.mkString(" ")
      s""" <<TABLE CELLBORDER="1" CELLSPACING="0" CELLPADDING="4">$dbtbdesc $tableHeader $str</TABLE>> """
    }
  }

  def genSelectCode(condition: String): String = {
    s"-----------------$db.$tb $column---------------------" +
      System.lineSeparator() +
      s"SELECT * FROM $db.$tb WHERE date =  " + "\"" + "${date}" + "\"" + " AND " + s"$column = " + "\"" + condition + "\";"
  }
}
