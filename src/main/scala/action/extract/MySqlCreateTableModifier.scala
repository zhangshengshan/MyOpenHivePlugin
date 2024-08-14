package action.extract

import antlr4.mysql.{MySqlParser, MySqlParserBaseVisitor}
import org.antlr.v4.runtime.TokenStreamRewriter

class MySqlCreateTableModifier(tokenStream: TokenStreamRewriter)
    extends MySqlParserBaseVisitor[Any] {

  override def visitConstraintDeclaration(
      ctx: MySqlParser.ConstraintDeclarationContext
  ): Any = {
    tokenStream.replace(
      ctx.getStart,
      ctx.getStop,
      ""
    )
    super.visitConstraintDeclaration(ctx)
  }

  //  tableOption: tableOptionEngine
  override def visitTableOptionEngine(
      ctx: MySqlParser.TableOptionEngineContext
  ): Any = {
    tokenStream.replace(
      ctx.getStart,
      ctx.getStop,
      ""
    )
    super.visitTableOptionEngine(ctx)
  }

  //  tableOption: tableOptionAutolncrement
  override def visitTableOptionAutoIncrement(
      ctx: MySqlParser.TableOptionAutoIncrementContext
  ): Any = {
    tokenStream.replace(
      ctx.getStart,
      ctx.getStop,
      ""
    )
    super.visitTableOptionAutoIncrement(ctx)
  }

  //  tableOption: tableOptionCharset
  override def visitTableOptionCharset(
      ctx: MySqlParser.TableOptionCharsetContext
  ): Any = {
    tokenStream.replace(
      ctx.getStart,
      ctx.getStop,
      ""
    )
    super.visitTableOptionCharset(ctx)
  }

  //  tableOption: tableOptionCollate
  override def visitTableOptionCollate(
      ctx: MySqlParser.TableOptionCollateContext
  ): Any = {
    tokenStream.replace(
      ctx.getStart,
      ctx.getStop,
      ""
    )
    super.visitTableOptionCollate(ctx)
  }

  override def visitTableName(ctx: MySqlParser.TableNameContext): Any = {
    tokenStream.replace(
      ctx.getStart,
      ctx.getStop,
      "ods_" + ctx.getText.replaceAll("`", "") + "dt"
    )
    super.visitTableName(ctx)
  }

  override def visitNullColumnConstraint(
      ctx: MySqlParser.NullColumnConstraintContext
  ): Any = {

    tokenStream.replace(
      ctx.getStart,
      ctx.getStop,
      ""
    )
    super.visitNullColumnConstraint(ctx)
  }

  override def visitDefaultValue(ctx: MySqlParser.DefaultValueContext): Any = {
    tokenStream.replace(
      ctx.getStart,
      ctx.getStop,
      ""
    )
    super.visitDefaultValue(ctx)
  }

  override def visitStringDataType(
      ctx: MySqlParser.StringDataTypeContext
  ): Any = {
    tokenStream.replace(
      ctx.getStart,
      ctx.getStop,
      "STRING "
    )
    super.visitStringDataType(ctx)
  }



}
