package misc

import openbrowser.{GenCodeOp, TenonStructureOp}

/**
 * [[misc.ScalaBridge]] 的 Scala 实现，供 Java 侧通过 applicationService 调用。
 */
class ScalaBridgeImpl extends ScalaBridge {

  override def genSelectCodePiece(param: String, sessionStr: String): String =
    GenCodeOp.genSelectCodePiece(param, sessionStr)

  override def genTenonStructureGraph(param: String): Unit =
    TenonStructureOp.genTenonStructureGraph(param)
}
