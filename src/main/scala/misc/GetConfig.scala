package misc

import com.intellij.openapi.actionSystem.{AnActionEvent, CommonDataKeys}
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import hierachyconfig.MyConfigurable

object GetConfig {

  def getConfig(e: AnActionEvent) = {
    val clipboard = ClipBoardUtil.getFromClipboard
    val value: MyConfigurable = MyConfigurable.getInstance()
    val host = value.getHost
    val port = value.getPort
    val user = value.getUser
    val password = value.getPassword
    val editor: Editor = e.getData(CommonDataKeys.EDITOR)

    val selectText = editor.getSelectionModel.getSelectedText
    val model = editor.getCaretModel
    val offset = model.getOffset
    val project: Project = editor.getProject
    (clipboard, host, port, user, password, project)
  }

}
