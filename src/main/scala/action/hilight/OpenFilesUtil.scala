package action.hilight

import com.intellij.openapi.editor.{Editor, EditorFactory}
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

/**
 * 文件操作工具类，提供获取当前项目中所有打开的文件和编辑器的方法
 */
object OpenFilesUtil {
  /**
   * 获取当前项目中所有打开的文件
   *
   * @param project 当前项目实例，用于定位特定项目中打开的文件
   * @return 返回一个VirtualFile数组，包含当前项目中所有打开的文件
   */
  def getAllOpenFiles(project: Project): Array[VirtualFile] =
    FileEditorManager.getInstance(project).getOpenFiles

  /**
   * 获取所有当前活跃的编辑器
   *
   * @return 返回一个Editor数组，包含所有当前活跃的编辑器实例
   */
  def getAllEditors: Array[Editor] = EditorFactory.getInstance().getAllEditors
}
