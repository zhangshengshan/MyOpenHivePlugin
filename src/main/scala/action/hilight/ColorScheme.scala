package action.hilight

import java.awt.Color

object ColorScheme {

  // 定义一个私有的颜色数组，用于存储字体颜色
  val FONT_COLORS: Array[Color] = Array(
    new Color(255, 255, 255), // White
    new Color(0, 0, 0), // Black
    new Color(255, 0, 0), // Red
    new Color(0, 255, 0), // Green
    new Color(0, 0, 255), // Blue
    new Color(255, 255, 0), // Yellow
    new Color(0, 255, 255), // Cyan
    new Color(255, 0, 255), // Magenta
    new Color(128, 0, 0), // Maroon
    new Color(0, 128, 0), // Green (Dark)
    new Color(0, 0, 128), // Navy
    new Color(128, 128, 0), // Olive
    new Color(0, 128, 128), // Teal
    new Color(128, 0, 128), // Purple
    new Color(192, 192, 192) // Silver
  )

  // 定义一个私有的颜色数组，用于存储背景颜色，其中的颜色是根据字体颜色的补色原则选择的
  val BACKGROUD_COLORS: Array[Color] = Array(
    new Color(0, 0, 0), // Black (complement of White)
    new Color(255, 255, 255), // White (complement of Black)
    new Color(0, 255, 255), // Cyan (complement of Red)
    new Color(255, 0, 255), // Magenta (complement of Green)
    new Color(255, 255, 0), // Yellow (complement of Blue)
    new Color(0, 0, 255), // Blue (complement of Yellow)
    new Color(255, 0, 0), // Red (complement of Cyan)
    new Color(0, 255, 0), // Green (complement of Magenta)
    new Color(128, 128, 128), // Gray (complement of Maroon)
    new Color(128, 255, 128), // Light Green (complement of Dark Green)
    new Color(128, 128, 255), // Light Blue (complement of Navy)
    new Color(128, 0, 128), // Purple (complement of Olive)
    new Color(255, 128, 128), // Light Red (complement of Teal)
    new Color(128, 128, 0), // Olive (complement of Purple)
    new Color(64, 64, 64) // Dark Gray (complement of Silver)
  )
}
