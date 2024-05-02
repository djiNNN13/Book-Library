package integration;

import dao.DBUtil;
import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;

public abstract class IntegrationTestBase {

  private static final String CLEAN_SQL =
            """
                    DELETE FROM book;
                    DELETE FROM reader;
                    """;
  @BeforeEach
  void cleanData() throws SQLException {
    try (var connection = DBUtil.getConnection();
        var statement = connection.createStatement()) {
      statement.execute(CLEAN_SQL);
    }
  }
}
