package action.extract

import doris.{DorisParser, DorisParserBaseVisitor}
import org.antlr.v4.runtime.TokenStreamRewriter
import org.antlr.v4.runtime.tree.TerminalNode

class DorisTableModifier(tokenStream: TokenStreamRewriter)
    extends DorisParserBaseVisitor[Any] {
  override def visitColumnDef(ctx: DorisParser.ColumnDefContext): Any = {
    val dataTypeContext = ctx.dataType()
    super.visitColumnDef(ctx)
  }

  override def visitPrimitiveColType(
      ctx: DorisParser.PrimitiveColTypeContext
  ): Any = super.visitPrimitiveColType(ctx)

  override def visitPrimitiveDataType(
      ctx: DorisParser.PrimitiveDataTypeContext
  ): Any = {

    ctx.primitiveColType().`type`.getType match {
      // if match VARCHAR
      case DorisParser.TEXT => {
        tokenStream.replace(
          ctx.getStart,
          ctx.getStop,
          DorisParser.VOCABULARY.getLiteralName(DorisParser.STRING)
        )
      }
      case DorisParser.VARCHAR => {
          tokenStream.replace(
            ctx.getStart,
            ctx.getStop,
            DorisParser.VOCABULARY.getLiteralName(DorisParser.STRING)
          )
        }
      case _ => // do nothing
    }
    super.visitPrimitiveDataType(ctx)
  }
}
