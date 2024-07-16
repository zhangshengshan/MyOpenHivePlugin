package action.linage

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileChooser.{FileChooserDescriptor, FileChooserDialog, FileChooserFactory}
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.vfs.VirtualFile
import config.os.OsConfig
import myui.MyMultiChoiceDialog

object PlotUtil {
  def plotAction(
      editor: Editor,
      mutableList: List[(String, String)]
  ): Unit = {
    val sourceList = mutableList.toList.map(_._2).distinct.toList

    val project: Project = editor.getProject
    val descriptor: FileChooserDescriptor =
      new FileChooserDescriptor(false, true, false, false, false, false)

    val dialog: FileChooserDialog = FileChooserFactory
      .getInstance()
      .createFileChooser(descriptor, project, null)

    val option: Option[VirtualFile] = dialog.choose(project).headOption
    val outPutDir: String = {
      if (option.isDefined) option.get.getPath
      else if (SystemInfo.isMac) OsConfig.macOutputPath
      else OsConfig.winOutputPath
    }

    import scala.jdk.CollectionConverters._

    val scalaList: List[String] = sourceList
    val javaList: java.util.List[String] = scalaList.asJava

    val myMultiChoiceDialog: MyMultiChoiceDialog = new MyMultiChoiceDialog(
      javaList
    )
    myMultiChoiceDialog.show()

    val source = myMultiChoiceDialog.isOK match {
      case true =>
        myMultiChoiceDialog.getSelectedOptions.toArray().toList.head.toString
      case _ => {
        Messages.showInfoMessage("没有选择任何一个表格作为分析对象， 退出处理程序", "Information")
        return
      }
    }

    // source 如果是 db.tb 这种形式取 tb
    val tableName = source.split("\\.") match {
      case Array(_, tb) => tb
      case _            => source
    }
    val fileName: String = Messages.showInputDialog(
      project,
      "请输入保存的文件名",
      "文件名",
      Messages.getQuestionIcon,
      tableName,
      null
    )

    MultiLayerLinageAnalysisUtil.plotDependency(
      mutableList,
      Some(outPutDir),
      fileName,
      source
    )
  }
}
