package action.extract

import com.intellij.openapi.ui.Messages
import doris.{DorisParser, DorisParserBaseVisitor}
import okhttp3.internal.ws.RealWebSocket.Message
import org.antlr.v4.runtime.TokenStreamRewriter
import org.antlr.v4.runtime.tree.ParseTree

class DorisTableNameModifier(
    tokenStream: TokenStreamRewriter,
    addSuffix: Boolean = true,
    onlyInsertChange: Boolean = true
) extends DorisParserBaseVisitor[Any] {

  private val aliasTableNames = scala.collection.mutable.Set[String]()

  // 添加后缀的辅助方法
  private def addTestSuffix(tableName: String): String = {
    val cleanName = tableName.replaceAll("`", "")

    if (addSuffix) {
      if (cleanName.endsWith("_strategy_test")) {
        tableName // 保持原格式（包括反引号）
      } else {
        if (tableName.contains("`")) {
          s"`${cleanName}_strategy_test`"
        } else {
          s"${cleanName}_strategy_test"
        }
      }
    } else {
      if (cleanName.endsWith("_strategy_test")) {
        // 删除 _strategy_test 后缀
        if (tableName.contains("`")) {
          s"`${cleanName.stripSuffix("_strategy_test")}`"
        } else {
          cleanName.stripSuffix("_strategy_test")
        }
      } else {
        tableName // 保持原格式（包括反引号）
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

  override def visitDelete(ctx: DorisParser.DeleteContext): Any = {
    val originalText = ctx.multipartIdentifier().getText
    val modifiedText = addTestSuffix(originalText)

    tokenStream.replace(
      ctx.multipartIdentifier().getStart,
      ctx.multipartIdentifier().getStop,
      modifiedText
    )
    super.visitDelete(ctx)
  }

  override def visitAliasQuery(ctx: DorisParser.AliasQueryContext): Any = {
    val originalText = ctx.identifier().getText
    aliasTableNames += originalText
    super.visitAliasQuery(ctx)
  }
  //  // 修改 FROM 子句和其他地方的表名，但排除别名查询
//  override def visitTableName(ctx: DorisParser.TableNameContext): Any = {
//    // 如果在别名查询上下文中，则不修改
//    if (!isInAliasQueryContext(ctx)) {
//      val originalText = ctx.multipartIdentifier().getText
//      val modifiedText = addTestSuffix(originalText)
//
//      tokenStream.replace(
//        ctx.multipartIdentifier().getStart,
//        ctx.multipartIdentifier().getStop,
//        modifiedText
//      )
//    }
//    super.visitTableName(ctx)
//  }
  // 针对 TRUNCATE 语句，需要查找相应的 Context
  // 由于 DorisParser 中可能没有专门的 TruncateTableContext，
  // 我们需要查找 TRUNCATE 相关的语句处理

  // 如果有 TruncateTable 相关的 Context，添加以下方法
  // 注意：这个方法名可能需要根据实际的 DorisParser 结构调整
  def visitTruncateTable(ctx: Any): Any = {
    // 这里需要根据实际的 DorisParser 结构来实现
    // 由于无法确定确切的 Context 类型，这里提供一个通用的实现思路

    // 使用反射来获取表名字段
    try {
      val tableNameField = ctx.getClass.getDeclaredField("tableName")
      tableNameField.setAccessible(true)
      val tableName = tableNameField.get(ctx)

      if (tableName != null) {
        val tableNameContext =
          tableName.asInstanceOf[DorisParser.MultipartIdentifierContext]
        val originalText = tableNameContext.getText
        val modifiedText = addTestSuffix(originalText)

        tokenStream.replace(
          tableNameContext.getStart,
          tableNameContext.getStop,
          modifiedText
        )
      }
    } catch {
      case _: Exception => // 忽略异常，可能该 Context 不包含表名字段
    }

    // 继续访问子节点
    ctx match {
      case parseTree: org.antlr.v4.runtime.tree.ParseTree =>
        for (i <- 0 until parseTree.getChildCount) {
          visit(parseTree.getChild(i))
        }
      case _ =>
    }

    null
  }

  // 通用的访问方法，用于处理可能的 TRUNCATE 语句
  override def visit(tree: org.antlr.v4.runtime.tree.ParseTree): Any = {
    if (tree != null) {
      // 检查是否为 TRUNCATE 相关的语句
      val contextName = tree.getClass.getSimpleName
      if (contextName.toLowerCase.contains("truncate")) {
        visitTruncateTable(tree)
      } else {
        super.visit(tree)
      }
    } else {
      super.visit(tree)
    }
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

  override def visitTableName(ctx: DorisParser.TableNameContext): Any = {
    if (!onlyInsertChange) {
      val originalText = ctx.multipartIdentifier().getText
      val cleanName = originalText.replaceAll("`", "")
      // 不修改的情况：
      // 1. 在别名查询上下文中
      // 2. 当前表名是已知的别名
      // 3. 在表别名定义上下文中
      if (!aliasTableNames.contains(cleanName)) {
        val modifiedText = addTestSuffix(originalText)

        tokenStream.replace(
          ctx.multipartIdentifier().getStart,
          ctx.multipartIdentifier().getStop,
          modifiedText
        )
      } else {
//        Messages.showInfoMessage(
//          s"${cleanName} 已经是别名，不需要修改",
//          aliasTableNames.mkString("\r")
//        )
      }
    }
    super.visitTableName(ctx)
  }

}
