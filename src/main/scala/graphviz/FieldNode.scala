package graphviz

import com.zss.graph.NodeElem
import graphviz.GraphVizConst._

object GraphVizConst {
  val HTMLTABLE_BEGIN = "<<TABLE>"
  val HTMLTABLE_END = "</TABLE>>"
  val TR_BEGIN = "<TR>"
  val TR_END = "</TR>"
  val TD_BEGIN =
    "<TD BGCOLOR=\"#ffcf45\" >"
  val TD_END = "</TD>"
}

case class FieldNode(
                      typeName: String,
                      name: String,
                      comment: Option[String] = None,
                      db: Option[String] = None,
                      tb: Option[String] = None
                    ) extends NodeElem {
  override def content: String = s"$name$comment"

  override def htmlTable: String = {

    val dbtbmeta =
      TR_BEGIN + s"<TD BGCOLOR=\"#f6ff15\" ROWSPAN=\"9\" HREF=\" +  ${nodeUrl} + \">" + s"库表:<B>${db.get}${tb.get}</B>" + TD_END + TR_END

    val fieldsDesc: String = (1 to 5).toList
      .map(x => {
        TR_BEGIN + TD_BEGIN + s"字段名" + x + TD_END + TD_BEGIN + s"字段名" + (x + 1) + TD_END + TR_END
      })
      .mkString(" ")

    val html = HTMLTABLE_BEGIN + dbtbmeta + fieldsDesc + HTMLTABLE_END

    html
  }

  def nodeUrl = {
    if (db.isDefined && tb.isDefined) {
      s""
    } else {
      ""
    }
  }
}
