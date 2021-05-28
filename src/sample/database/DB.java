package sample.database;

import java.sql.*;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import sample.model.User;

public class DB {
    private final String dbHost;
    private final String dbPort;
    private final String dbUser;
    private final String dbPass;
    private final String dbName;

    public DB(String host, String port, String user, String pass, String name) {
        this.dbHost = host;
        this.dbPort = port;
        this.dbUser = user;
        this.dbPass = pass;
        this.dbName = name;
    }

    private Connection getConnection() throws SQLException {
        DriverManager.registerDriver(new SQLServerDriver());
        String connectionUrl = "jdbc:sqlserver://" + dbHost + ":" + dbPort +
                ";databaseName=" + dbName + ";user=" + dbUser + ";password=" + dbPass + ";";
        return DriverManager.getConnection(connectionUrl);
    }

    public ResultSet query(String sql) throws SQLException {
        PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
        return preparedStatement.executeQuery();
    }
}
