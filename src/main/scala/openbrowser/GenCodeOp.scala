package openbrowser

class GenCodeOp

object GenCodeOp {
  private final val layer_num: Int = 1
  private final val AS = " AS "
  private final val COMMENT = " -- "

  def getMaxLength(list: List[String]): Int = {
    var max = 0
    for (str <- list) {
      if (str.length > max) {
        max = str.length
      }
    }
    max + 2
  }

  def genSelectCodePiece(param: String, sessionStr: String): String = {
    ""
  }

}
