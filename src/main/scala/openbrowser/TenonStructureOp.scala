package openbrowser

import another.GraphBuilder
import com.intellij.openapi.fileChooser.FileChooserDialog
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.vfs.VirtualFile
import com.zss.graph.Graph
import config.os.OsConfig
import hierachyconfig.MyConfigurable
import misc.ClipBoardUtil

object TenonStructureOp {

  def genTenonStructureGraph(param: String): Unit = {

    param
      .replace("(", "_")
      .replace(")", "_")
      .replace(".", "_")
      .replace("（", "_")
      .replace("）", "_")
      .replace("=", "_")

    def genUrl(ss: Array[String]): String = {
      s"https://www.baidu.com"
    }

    val config: Map[String, String] = Map(
      "mode" -> MyConfigurable.getInstance().getTennonShowMode
    ) ++ HierachyConfigStrategy.getColorConfig

    val openOrNot: Boolean = MyConfigurable.getInstance().isOpenAfterGen

    import com.intellij.openapi.fileChooser.{FileChooserDescriptor, FileChooserFactory}
    import com.intellij.openapi.project.ProjectManager
    import com.intellij.openapi.ui.Messages

    val descriptor =
      new FileChooserDescriptor(false, true, false, false, false, false)
    val project: Project = ProjectManager.getInstance().getDefaultProject

    val initialFileName: String = param.split("/").last.split("\\.").head
    //TODO: 给一个默认的文件名
    val fileName: String = Messages.showInputDialog(
      project,
      "请输入文件名",
      "文件名",
      Messages.getQuestionIcon,
      initialFileName,
      null
    )
    val graph: Graph =
      GraphBuilder.make(param, Some(genUrl), Some(config), Some(fileName))

    val dialog: FileChooserDialog = FileChooserFactory
      .getInstance()
      .createFileChooser(descriptor, project, null)

    val option: Option[VirtualFile] = dialog.choose(project).headOption
    val outPutDir: String = {
      if (option.isDefined) option.get.getPath
      else if (SystemInfo.isMac) OsConfig.macOutputPath
      else OsConfig.winOutputPath
    }

    try {
      if (SystemInfo.isMac) {
        graph.render(
          fileName,
          Some(outPutDir),
          Some(openOrNot),
          Some(OsConfig.macDotPath),
          Some(false)
        )
      } else {
        graph.render(
          fileName,
          Some(outPutDir),
          Some(openOrNot),
          Some(OsConfig.winDotPath),
          Some(true)
        )
      }
    } catch {
      case e: Exception =>
        Messages.showMessageDialog(
          project,
          e.getMessage,
          "错误",
          Messages.getErrorIcon
        )
    }

    val genSQL: String = graph.toSQLWithOn(0)

    ClipBoardUtil.copyToClipBoard(genSQL)

    Messages.showMessageDialog(
      project,
      genSQL,
      "SQL",
      Messages.getInformationIcon
    )

  }
}
