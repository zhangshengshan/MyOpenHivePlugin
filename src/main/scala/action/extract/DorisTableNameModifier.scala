package action.extract

import doris.{DorisParser, DorisParserBaseVisitor}
import org.antlr.v4.runtime.TokenStreamRewriter
import org.antlr.v4.runtime.tree.ParseTree

class DorisTableNameModifier(tokenStream: TokenStreamRewriter)
    extends DorisParserBaseVisitor[Any] {

  // 添加后缀的辅助方法
  private def addTestSuffix(tableName: String): String = {
    val cleanName = tableName.replaceAll("`", "")
    if (cleanName.endsWith("_test")) {
      tableName // 保持原格式（包括反引号）
    } else {
      if (tableName.contains("`")) {
        s"`${cleanName}_test`"
      } else {
        s"${cleanName}_test"
      }
    }
  }

  // 检查是否在别名查询上下文中
  private def isInAliasQueryContext(
      ctx: DorisParser.TableNameContext
  ): Boolean = {
    var parent: ParseTree = ctx.getParent
    while (parent != null) {
      if (parent.isInstanceOf[DorisParser.AliasQueryContext]) {
        return true
      }
      parent = parent.getParent
    }
    false
  }

  // 修改 INSERT 语句中的目标表名
  override def visitInsertTable(ctx: DorisParser.InsertTableContext): Any = {
    val originalText = ctx.multipartIdentifier().getText
    val modifiedText = addTestSuffix(originalText)

    tokenStream.replace(
      ctx.multipartIdentifier().getStart,
      ctx.multipartIdentifier().getStop,
      modifiedText
    )
    super.visitInsertTable(ctx)
  }

  // 修改 FROM 子句和其他地方的表名，但排除别名查询
  override def visitTableName(ctx: DorisParser.TableNameContext): Any = {
    // 如果在别名查询上下文中，则不修改
    if (!isInAliasQueryContext(ctx)) {
      val originalText = ctx.multipartIdentifier().getText
      val modifiedText = addTestSuffix(originalText)

      tokenStream.replace(
        ctx.multipartIdentifier().getStart,
        ctx.multipartIdentifier().getStop,
        modifiedText
      )
    }
    super.visitTableName(ctx)
  }

  // 如果有 CREATE TABLE 语句，也需要修改
  override def visitCreateTable(ctx: DorisParser.CreateTableContext): Any = {
    if (ctx.multipartIdentifier() != null) {
      val originalText = ctx.multipartIdentifier().getText
      val modifiedText = addTestSuffix(originalText)

      tokenStream.replace(
        ctx.multipartIdentifier().getStart,
        ctx.multipartIdentifier().getStop,
        modifiedText
      )
    }
    super.visitCreateTable(ctx)
  }

}
