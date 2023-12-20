package openbrowser

import hierachyconfig.MyConfigurable

import scala.language.postfixOps

object HierachyConfigStrategy {
  private val configurable: MyConfigurable = MyConfigurable.getInstance()

  def showFields: Boolean = {
    configurable.isFields显示
  }

  def bathProcessFieldHierach: Boolean = {
    configurable.isBatchFieldHierachy
  }

  def showUpStream: Boolean = {
    configurable.isUpStream
  }

  def showDownStream: Boolean = {
    configurable.isDownStream
  }

  def getIconByNodeType(typeName: Option[String]): String = {

    val Icon = typeName match {
      case Some("AeolusDashboard")           => "dashboard.png"
      case Some("AeolusDashboardReportTask") => "other.png"
      case Some("AeolusDataset")             => "dataset.png"
      case Some("AeolusDatasetColumnTask")   => "other.png"
      case Some("ClickhouseTable")           => "clickhouse.png"
      case Some("DummyTask")                 => "other.png"
      case Some("HiveTable")                 => "hive.png"
      case Some("MySQLTable")                => "mysql.png"
      case _                                 => "other.png"
    }
    //    Messages.showInfoMessage(configurable.getParam, "svgresource")
    configurable.getParam + "/" + Icon
  }

  def getBGColorForNodeType(typeName: Option[String]): String = {

    configurable.getHIVEBG
    rgb2hex(configurable.getHIVEBG)
    /*
      "typeName": "AeolusDashboard",
      "typeName": "AeolusDashboardReportTask",
      "typeName": "AeolusDataset",
      "typeName": "AeolusDatasetColumnTask",
      "typeName": "ClickhouseTable",
      "typeName": "DummyTask",
      "typeName": "HiveTable",
     */
    val color = typeName match {
      case Some("AeolusDashboard")           => rgb2hex(configurable.getDASHBOARDBG)
      case Some("AeolusDashboardReportTask") => "#ffffff"
      case Some("AeolusDataset")             => rgb2hex(configurable.getDataSetBG)
      case Some("AeolusDatasetColumnTask")   => "#ffffff"
      case Some("ClickhouseTable")           => rgb2hex(configurable.getCLICKHOUSEBG)
      case Some("DummyTask")                 => "#ffffff"
      case Some("HiveTable")                 => rgb2hex(configurable.getHIVEBG)
      case Some("MySQLTable")                => rgb2hex(configurable.getMYSQLBG)
      case _                                 => "#0F0F0F"
    }
    color
  }

  private def rgb2hex(rgb: Int): String = {
    f"#${rgb & 0xffffff}%06X"
  }

  def getColorConfig: Map[String, String] = {
    val config = scala.collection.mutable.Map[String, String]()
    config put ("bgcolor1", rgb2hex(configurable.getColor1))
    config put ("bgcolor2", rgb2hex(configurable.getColor2))
    config put ("bgcolor3", rgb2hex(configurable.getColor3))
    config put ("bgcolor4", rgb2hex(configurable.getColor4))
    println(config)
    config.toMap
  }

  def getFontColorForNodeType(typeName: String): String = ???

  def getBGColorForHiveLayer(layer: String): String = ???

  def getFontColorForHiveLayer(layer: String): String = ???

}
