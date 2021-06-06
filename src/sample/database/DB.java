package sample.database;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import java.sql.*;

public class DB {
    public static UserRepo userRepo;
    public static CategoryRepo categoryRepo;

    private final String url;

    public DB(String host, String port, String username, String password, String database) throws SQLException {
        url = "jdbc:sqlserver://" + host + ":" + port + ";databaseName=" + database + ";user=" + username + ";password=" + password + ";";
        getConnection();
    }

    private Connection getConnection() throws SQLException {
        DriverManager.registerDriver(new SQLServerDriver());
        return DriverManager.getConnection(url);
    }

    public ResultSet query(String sql) throws SQLException {
        PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
        return preparedStatement.executeQuery();
    }

    public static void initializeDatabaseModels(String username, String password) throws SQLException {
        DB db = new DB("127.0.0.1", "1433", username, password, "makk");
        userRepo = new UserRepo(db);
        categoryRepo = new CategoryRepo(db);
    }
}
