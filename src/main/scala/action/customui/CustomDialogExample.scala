package action.customui

import javax.swing._


object CustomDialogExample {
  def showDialog(): JDialog = {
    // 创建一个顶级菜单
    val menu = new JMenu("请选择替换模式")

    // 创建子菜单
    val normalMode = new JMenuItem("普通替换")
    val regexMode = new JMenuItem("正则替换")
    val anotherExtract = new JMenuItem("另一种提取方式")
    val clipboardWrap = new JMenu("剪切板包裹")

    // 创建剪切板包裹的子菜单
    val clipboardWrapOption1 = new JMenuItem("剪切板包裹选项1")
    val clipboardWrapOption2 = new JMenuItem("剪切板包裹选项2")

    // 将剪切板包裹的子菜单添加到剪切板包裹菜单
    clipboardWrap.add(clipboardWrapOption1)
    clipboardWrap.add(clipboardWrapOption2)

    // 将子菜单添加到顶级菜单
    menu.add(normalMode)
    menu.add(regexMode)
    menu.add(anotherExtract)
    menu.add(clipboardWrap)

    // 创建一个菜单栏并将顶级菜单添加到菜单栏
    val menuBar = new JMenuBar()
    menuBar.add(menu)

    // 创建一个对话框并将菜单栏添加到对话框
    val dialog = new JDialog()
    dialog.setJMenuBar(menuBar)
    dialog.setSize(300, 200)
    dialog.setVisible(true)

    dialog
  }
}