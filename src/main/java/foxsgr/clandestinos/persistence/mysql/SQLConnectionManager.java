package foxsgr.clandestinos.persistence.mysql;

import foxsgr.clandestinos.application.config.ConfigManager;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.function.Function;

import static java.lang.String.format;

public final class SQLConnectionManager {

    private static Connection connection;

    private static String host = "localhost";
    private static String port = "3306";
    private static String database = "clandestinos";
    private static String username = "root";
    private static String password = "root";

    private static final Object LOCK = new Object();

    public static void init() {
        ConfigManager config = ConfigManager.getInstance();
        host = config.getString(ConfigManager.MYSQL_HOST);
        port = config.getString(ConfigManager.MYSQL_PORT);
        database = config.getString(ConfigManager.MYSQL_DATABASE);
        username = config.getString(ConfigManager.MYSQL_USERNAME);
        password = config.getString(ConfigManager.MYSQL_PASSWORD);
    }

    public static Statement createStatement() {
        Connection connection = getConnection();
        try {
            return connection.createStatement();
        } catch (SQLException e) {
            throw new IllegalStateException("Error while connecting to database", e);
        }
    }

    public static <T> T execute(String sql, List<Object> params, @Nullable Function<Statement, T> onExecute) {
        try (Statement statement = createStatement()) {
            for (int i = 0; i < params.size(); i++) {
                sql = sql.replace(format(":%d", i + 1), params.get(i).toString());
            }

            statement.execute(sql);

            if (onExecute != null) {
                return onExecute.apply(statement);
            }

            return null;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public static void execute(String sql, List<Object> params) {
        execute(sql, params, null);
    }

    public static Connection getConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            }

            synchronized (LOCK) {
                if (connection != null && !connection.isClosed()) {
                    return connection;
                }

                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection(
                        format("jdbc:mysql://%s:%s/%s", host, port, database), username, password
                );
            }

            return connection;
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not access the database", e);
        }
    }

    public static String nullableParam(String param) {
        return param == null ? "NULL" : format("'%s'", param);
    }

    public static String arrayParam(List<Object> list) {
        StringBuilder builder = new StringBuilder()
                .append('\'');

        for (int i = 0; i < list.size(); i++) {
            builder.append(list.get(i));

            if (i != list.size() - 1) {
                builder.append(',');
            }
        }

        return builder.append('\'').toString();
    }
}
