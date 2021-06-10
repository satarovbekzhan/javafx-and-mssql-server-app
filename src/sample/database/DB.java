package sample.database;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;

import java.sql.*;

public class DB {
    public static TestRepo testRepo;
    public static UserRepo userRepo;
    public static CategoryRepo categoryRepo;
    public static ProductRepo productRepo;
    public static CompositionRepo compositionRepo;

    private final String url;

    public DB(String host, String port, String username, String password, String database) throws SQLException {
        url = "jdbc:sqlserver://" + host + ":" + port + ";databaseName=" + database + ";user=" + username + ";password=" + password + ";";
    }

    public Connection getConnection() throws SQLException {
        DriverManager.registerDriver(new SQLServerDriver());
        return DriverManager.getConnection(url);
    }

    public static void initializeDatabaseModels(String username, String password) throws SQLException {
        DB db = new DB("127.0.0.1", "1433", username, password, "makk");
        db.getConnection();
        userRepo = new UserRepo(db);
        categoryRepo = new CategoryRepo(db);
        testRepo = new TestRepo(db);
        productRepo = new ProductRepo(db);
        compositionRepo = new CompositionRepo(db);
    }
}
