package action.fieldmatch

import com.intellij.openapi.actionSystem.{
  AnAction,
  AnActionEvent,
  CommonDataKeys
}
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.{Document, Editor}
import com.intellij.openapi.fileChooser.{
  FileChooserDescriptor,
  FileChooserDialog,
  FileChooserFactory
}
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import misc.ExcelObject

import scala.collection.mutable

class FieldMatchAction extends AnAction {
  override def actionPerformed(e: AnActionEvent): Unit = {

    val project: Project = e.getData(CommonDataKeys.PROJECT)
    val editor: Editor = e.getData(CommonDataKeys.EDITOR)
    val document: Document = editor.getDocument
    val matchMap = scala.collection.mutable.Map[String, String]()

    /** ExcelObject
      * 在这里用ExcelObject代替Excel，ExcelObject是一个类，包含了Excel的所有属性和方法，这样就可以直接调用ExcelObject的方法，而不用再去调用Excel的方法了。
      */
    // 创建一个文件选择器描述符
    val descriptor =
      new FileChooserDescriptor(true, false, false, false, false, false)

    // 创建一个文件选择器对话框
    val dialog: FileChooserDialog = FileChooserFactory.getInstance
      .createFileChooser(descriptor, project, null)

    // 显示文件选择器对话框并获取选中的文件
    val chosenFiles: Array[VirtualFile] = dialog.choose(null, project)

    // 检查是否有文件被选中
    val chosenFile: VirtualFile = chosenFiles(0)
    // 在这里处理选中的文件

    val excelObject = new ExcelObject();

    val content: List[List[String]] = excelObject.readExcel(
      chosenFile.getPath,
      List("sourceTable", "sourceField", "targetTable", "targetField")
    )

    for (i <- 0 until content.length) {
      val sourceField = content(i)(0) + "." + content(i)(1)
      val targetField = content(i)(3)
      matchMap.put(sourceField, targetField)
    }

    // 这里可能有多个sourceField 被映射成为一个相同的targetField 该如何处理？
    // 希望能够转化为这种逻辑
    /*      a -> b
      c -> b
      e -> f
    转化为
      a,c -> b
      e -> f
     */

    val reversedMatchMap: Map[String, mutable.Map[String, String]] =
      matchMap.groupBy(_._2)

    reversedMatchMap.foreach(kv => {

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
      val sourceField: String = kv._2.mkString(",")
      val targetField: String = kv._1
      val oldFieldPattern = s"NULL AS $targetField"
      val newFieldPattern = s"$sourceField AS $targetField"
      val oldText = document.getText

      // val newText = oldText.replaceAll(oldFieldPattern, newFieldPattern)
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
