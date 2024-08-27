package action.hilight

import com.intellij.openapi.editor.{Editor, EditorFactory}
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

object OpenFilesUtil {
  def getAllOpenFiles(project: Project): Array[VirtualFile] =
    FileEditorManager.getInstance(project).getOpenFiles

  def getAllEditors: Array[Editor] = EditorFactory.getInstance().getAllEditors
}
