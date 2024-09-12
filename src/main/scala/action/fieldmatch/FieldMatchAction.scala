package action.fieldmatch

import com.intellij.openapi.actionSystem.{AnAction, AnActionEvent, CommonDataKeys}
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.{Document, Editor}
import com.intellij.openapi.project.Project

class FieldMatchAction extends AnAction {
  override def actionPerformed(e: AnActionEvent): Unit = {
    val project: Project = e.getData(CommonDataKeys.PROJECT)
    val editor: Editor = e.getData(CommonDataKeys.EDITOR)
    val document: Document = editor.getDocument
    val matchMap = scala.collection.mutable.Map[String, String]()
    matchMap.put("a", "b")
    matchMap.put("c", "d")
    matchMap.put("e", "f")
    matchMap.put("g", "h")
    matchMap.foreach(kv => {

      /** SELECT
        *  NULL AS b
        *  , NULL AS d
        *  , NULL AS f
        *  , NULL AS h
        * FROM
        *  da.ta
        *
        * 转化为
        *
        * SELECT
        *  a AS b
        *  , c AS d
        *  , d AS f
        *  , e AS h
        * FROM
        *  da.ta
        */
      val sourceField = kv._1
      val targetField = kv._2
      val oldFieldPattern = s"NULL AS $targetField"
      val newFieldPattern = s"$sourceField AS $targetField"
      val oldText = document.getText

//      val newText = oldText.replaceAll(oldFieldPattern, newFieldPattern)
      // NULL 和 AS之间可能有多个空格， 怎么处理？
      val newText = oldText.replaceAll(
        s"NULL\\s+AS\\s+$targetField",
        s"$sourceField AS $targetField"
      )
      ApplicationManager.getApplication.runWriteAction(new Runnable {
        override def run(): Unit = {
          document.setText(newText)
          editor.getCaretModel.moveToOffset(0)
        }
      })
    })
  }
}
