package action.hilight

import java.awt.Color

object ColorScheme {

  // 定义一个私有的颜色数组，用于存储字体颜色
  val fontColors = Array(
    Color.RED,
    Color.GREEN,
    Color.BLUE,
    Color.YELLOW,
    Color.CYAN,
    Color.MAGENTA,
    Color.ORANGE,
    Color.PINK
  )

  // 定义一个私有的颜色数组，用于存储背景颜色，其中的颜色是根据字体颜色的补色原则选择的
  val backgroundColors = Array(
    Color.CYAN, // Complement of RED
    Color.MAGENTA, // Complement of GREEN
    Color.YELLOW, // Complement of BLUE
    Color.BLUE, // Complement of YELLOW
    Color.RED, // Complement of CYAN
    Color.GREEN, // Complement of MAGENTA
    Color.BLUE, // Complement of ORANGE
    Color.GREEN // Complement of PINK
  )
}
