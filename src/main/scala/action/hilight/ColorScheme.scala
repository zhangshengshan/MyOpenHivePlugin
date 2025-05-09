package action.hilight

import java.awt.Color

object ColorScheme {

  // 定义一个私有的颜色数组，用于存储字体颜色
  val FONT_COLORS = Array(
    Color.RED,
    Color.GREEN,
    Color.BLUE,
    Color.YELLOW,
    Color.CYAN,
    Color.MAGENTA,
    Color.ORANGE,
    new Color(255, 215, 0), // Gold color
    new Color(218, 165, 32), // Goldenrod color
    new Color(210, 105, 30), // Chocolate color
    new Color(205, 133, 63), // Peru color
    new Color(244, 164, 96), // Sandy Brown color
    new Color(210, 180, 140), // Tan color
    new Color(222, 184, 135), // Burlywood color
    new Color(139, 0, 139), // Dark Magenta color
    new Color(148, 0, 211), // Dark Violet color
    new Color(153, 50, 204), // Dark Orchid color
    new Color(186, 85, 211), // Medium Orchid color
    new Color(218, 112, 214), // Orchid color
    new Color(221, 160, 221), // Plum color
    new Color(238, 130, 238), // Violet color
    new Color(255, 0, 255), // Fuchsia color
    new Color(255, 105, 180), // Hot Pink color
    new Color(255, 20, 147), // Deep Pink color
    new Color(255, 192, 203), // Pink color
    new Color(255, 182, 193), // Light Pink color
    new Color(219, 112, 147) // Pale Violet Red color
  )

  // 定义一个私有的颜色数组，用于存储背景颜色，其中的颜色是根据字体颜色的补色原则选择的
  val BACKGROUD_COLORS = Array(
    Color.CYAN, // Complement of RED
    Color.MAGENTA, // Complement of GREEN
    Color.YELLOW, // Complement of BLUE
    Color.BLUE, // Complement of YELLOW
    Color.RED, // Complement of CYAN
    Color.GREEN, // Complement of MAGENTA
    Color.BLUE, // Complement of ORANGE
    Color.GREEN, // Complement of PINK
    new Color(170, 148, 208), // Complement of Olive Drab color
    new Color(116, 186, 236), // Complement of Saddle Brown color
    new Color(0, 40, 255), // Complement of Gold color
    new Color(37, 90, 223), // Complement of Goldenrod color
    new Color(116, 255, 116), // Complement of Dark Magenta color
    new Color(107, 255, 44), // Complement of Dark Violet color
    new Color(102, 205, 51), // Complement of Dark Orchid color
    new Color(69, 170, 44), // Complement of Medium Orchid color
    new Color(37, 143, 41), // Complement of Orchid color
    new Color(34, 95, 34), // Complement of Plum color
    new Color(17, 125, 17), // Complement of Violet color
    new Color(0, 150, 75), // Complement of Fuchsia color
    new Color(150, 150, 75), // Complement of Hot Pink color
    new Color(108, 235, 108), // Complement of Deep Pink color
    new Color(36, 143, 108) // Complement of Pale Violet Red color
  )
}
