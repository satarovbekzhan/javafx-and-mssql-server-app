package sample.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Repository {
    private final DB db;

    protected Repository(DB db) {
        this.db = db;
    }

    protected ResultSet query(String sql) throws SQLException {
        PreparedStatement preparedStatement = db.getConnection().prepareStatement(sql);
        return preparedStatement.executeQuery();
    }
}
