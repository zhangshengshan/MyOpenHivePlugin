package util

import com.intellij.openapi.util.SystemInfo

object OpenFileUtil {

  def openFileDispatchOsSystem(excelFilePath: String) = {
    if (SystemInfo.isWindows) {
      Runtime.getRuntime.exec("cmd /c start " + excelFilePath)
    } else if (SystemInfo.isMac) {
      Runtime.getRuntime.exec("open " + excelFilePath)
    } else if (SystemInfo.isLinux) {
      Runtime.getRuntime.exec("xdg-open " + excelFilePath)
    }
  }

}
