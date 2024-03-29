package dao;

import exception.DBConfigurationError;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {
  private static final Properties properties = loadProperties();

  private static Properties loadProperties() {
    Properties properties = new Properties();
    try (InputStream inputStream = new FileInputStream("src/main/resources/db/db.properties")) {
      properties.load(inputStream);
    } catch (IOException e) {
      throw new DBConfigurationError("Unable to read properties from database file, please come later", e);
    }
    return properties;
  }

  public static Connection getConnection() {
    String url = properties.getProperty("db.conn.url");
    String userName = properties.getProperty("db.username");
    String password = properties.getProperty("db.password");

    try {
      return DriverManager.getConnection(url, userName, password);
    } catch (SQLException e) {
      throw new DBConfigurationError(
          "Unable to establish database connection, please come later", e);
    }
  }
}
