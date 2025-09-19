package action.hilight

import action.hilight.CaretUtil.getWordAtCaret
import action.hilight.ColorScheme.{BACKGROUD_COLORS, FONT_COLORS}
import com.intellij.openapi.actionSystem.{
  AnAction,
  AnActionEvent,
  CommonDataKeys
}
import com.intellij.openapi.editor.ScrollType
import com.intellij.openapi.editor.markup.{
  EffectType,
  HighlighterLayer,
  HighlighterTargetArea,
  TextAttributes
}
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.editor.Document
import com.intellij.openapi.fileEditor.TextEditor

import scala.util.matching.Regex

class MoveToNextWordAction extends AnAction {

  var colorIndex = 0
  var currentWord: String = ""

  // 当动作触发时的处理函数
  override def actionPerformed(e: AnActionEvent): Unit = {

    // 获取当前编辑器和项目实例
    val editor = e.getData(CommonDataKeys.EDITOR)
    val project = e.getProject
    // 如果编辑器或项目实例为空，则直接返回
    if (editor == null || project == null) {
      return
    }

    // 获取光标模型和当前光标位置
    val caretModel = editor.getCaretModel
    var offset = caretModel.getOffset
    // 获取文档文本
    val documentText = editor.getDocument.getText

    // 获取光标下的单词
    val word = getWordAtCaret(editor)
    // 如果获取不到单词，则直接返回
    if (word == null) {
      return
    }
    // 如果当前的单词和上一次的单词不同，就切换颜色
    if (
      currentWord == null || word.toLowerCase() != currentWord.toLowerCase()
    ) {
      currentWord = word
      colorIndex = (colorIndex + 1) % FONT_COLORS.length
    }

    // 高亮当前的单词
    val markupModel = editor.getMarkupModel

    // 根据当前的颜色索引创建文本属性
    val textAttributes =
      new TextAttributes(
        FONT_COLORS(colorIndex),
        BACKGROUD_COLORS(colorIndex),
        null,
        EffectType.BOXED,
        0
      )

    // 添加高亮标记到文档中
    markupModel.addRangeHighlighter(
      caretModel.getOffset,
      caretModel.getOffset + word.length,
      HighlighterLayer.LAST,
      textAttributes,
      HighlighterTargetArea.EXACT_RANGE
    )

    // 构建单词的正则表达式，用于查找下一个匹配的单词
    val wordPattern = new Regex(s"(?i)\\b$word\\b").unanchored
    
    // 获取所有打开的文件
    val fileEditorManager = FileEditorManager.getInstance(project)
    val openFiles = fileEditorManager.getOpenFiles
    val currentFile = editor.getVirtualFile
    
    // 查找下一个匹配的单词位置
    findAndMoveToNextWord(fileEditorManager, openFiles, currentFile, editor, wordPattern, offset)
  }
  
  // 在所有打开的文件中查找并跳转到下一个匹配的单词
  private def findAndMoveToNextWord(
    fileEditorManager: FileEditorManager,
    openFiles: Array[VirtualFile],
    currentFile: VirtualFile,
    currentEditor: com.intellij.openapi.editor.Editor,
    wordPattern: Regex,
    currentOffset: Int
  ): Unit = {
    
    val currentDocumentText = currentEditor.getDocument.getText
    var foundInCurrentFile = false
    
    // 首先尝试在当前文件中查找下一个匹配项
    var nextWordMatch = wordPattern.findFirstMatchIn(currentDocumentText.substring(currentOffset + 1))
    
    if (nextWordMatch.isDefined) {
      // 在当前文件中找到了下一个匹配项
      val searchStartOffset = 1
      val nextWordOffset = currentOffset + nextWordMatch.get.start + searchStartOffset
      currentEditor.getCaretModel.moveToOffset(nextWordOffset)
      currentEditor.getScrollingModel.scrollToCaret(ScrollType.CENTER)
      return
    }
    
    // 如果在当前文件的后续位置没找到，则在所有打开的文件中搜索
    var currentIndex = openFiles.indexOf(currentFile)
    var filesSearched = 0
    val totalFiles = openFiles.length
    
    while (filesSearched < totalFiles) {
      currentIndex = (currentIndex + 1) % totalFiles
      val file = openFiles(currentIndex)
      
      // 获取文件对应的编辑器
      val editors = fileEditorManager.getEditors(file)
      if (editors.nonEmpty && editors(0).isInstanceOf[TextEditor]) {
        val textEditor = editors(0).asInstanceOf[TextEditor]
        val document = textEditor.getEditor.getDocument
        val documentText = document.getText
        
        // 在文件中查找匹配的单词
        val matchResult = wordPattern.findFirstMatchIn(documentText)
        if (matchResult.isDefined) {
          // 找到匹配项，跳转到该文件和位置
          fileEditorManager.openFile(file, true)
          val targetEditor = fileEditorManager.getSelectedTextEditor
          if (targetEditor != null) {
            targetEditor.getCaretModel.moveToOffset(matchResult.get.start)
            targetEditor.getScrollingModel.scrollToCaret(ScrollType.CENTER)
          }
          return
        }
      }
      
      filesSearched += 1
    }
  }
}