import java.sql.{Connection, DriverManager, ResultSet}

class DorisClient(
    jdbcUrl: String,
    user: String,
    password: String,
    driverName: String
) {
  private var connection: Connection = _

  def connect(): Unit = {
    Class.forName(driverName)
    connection = DriverManager.getConnection(jdbcUrl, user, password)
  }

  def executeQuery(query: String): ResultSet = {
    val statement = connection.createStatement()
    statement.executeQuery(query)
  }

  def close(): Unit = {
    if (connection != null && !connection.isClosed) {
      connection.close()
    }
  }
}
