package integration;

import dao.DBUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.sql.SQLException;

public abstract class IntegrationTestBase {

  private static final String CLEAN_SQL =
            """
                    DELETE FROM book;
                    DELETE FROM reader;
                    """;
  private static final String CREATE_SQL =
      """
            CREATE TABLE IF NOT EXISTS reader
            (
                id   BIGSERIAL,
                name VARCHAR(255) NOT NULL,
                CONSTRAINT readers_PK PRIMARY KEY (id)
            );
            CREATE TABLE IF NOT EXISTS book
            (
                id        BIGSERIAL,
                name      VARCHAR(255) NOT NULL,
                author    VARCHAR(255) NOT NULL,
                reader_id BIGINT,
                CONSTRAINT book_PK PRIMARY KEY (id),
                CONSTRAINT book_readers_FK FOREIGN KEY (reader_id) REFERENCES reader ON UPDATE CASCADE ON DELETE RESTRICT
            );
            """;

  @BeforeAll
  static void prepareDatabase() throws SQLException {
    try (var connection = DBUtil.getConnection();
        var statement = connection.createStatement()) {
      statement.execute(CREATE_SQL);
    }
  }

  @BeforeEach
  void cleanData() throws SQLException {
    try (var connection = DBUtil.getConnection();
        var statement = connection.createStatement()) {
      statement.execute(CLEAN_SQL);
    }
  }
}
