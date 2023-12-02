package action

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent, CommonDataKeys}
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.SystemInfo
import config.os.OsConfig
import config.os.OsConfig.{macDotPath, winDotPath}
import demo.SparkSqlVisitorV2
import hierachyconfig.MyConfigurable
import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}
import zss.mysqlparser.{CaseChangingCharStream, SqlBaseLexer, SqlBaseParser}

class GraphvizShowAction extends AnAction {
  override def actionPerformed(event: AnActionEvent): Unit = {
    try {
      val editor: Editor = event.getData(CommonDataKeys.EDITOR)

      val text = editor.getDocument.getText
      val lexer: SqlBaseLexer =
        new SqlBaseLexer(
          new CaseChangingCharStream(CharStreams.fromString(text), true)
        )

      val commonTokenStream: CommonTokenStream = new CommonTokenStream(lexer)
      val parser: SqlBaseParser = new SqlBaseParser(commonTokenStream)
      val context: SqlBaseParser.StatementContext = parser.statement()
      val visitor: SparkSqlVisitorV2 = new SparkSqlVisitorV2()
      visitor.visit(context)

      val gen: Boolean = MyConfigurable.getInstance().isOpenAfterGen
      // TODO: 这里的路径设置的有问题啊
      val name: String = SystemInfo.getOsName
      Messages.showMessageDialog(
        name,
        "OsName",
        Messages.getInformationIcon
      )
      // 底层的库中需要增加一个plot函数, 用于支持Windows和Mac的图形化显示
      if(SystemInfo.isMac){
        visitor.plot(
          Some("test"),
          Some(OsConfig.macOutputPath),
          Some(true),
          Some(macDotPath)
        )
      }else if(SystemInfo.isWindows) {
        visitor.plot(
          Some("test"),
          Some(OsConfig.winOutputPath),
          Some(true),
          Some(winDotPath)
        )
      }
      // Linux System current not supported

    } catch {
      case e: Exception =>
        Messages.showMessageDialog(
          "Error: " + e.getMessage,
          "Error",
          Messages.getErrorIcon
        )
    } finally {
      Messages.showMessageDialog(
        "Done",
        "Done",
        Messages.getInformationIcon
      )
    }
  }
}
