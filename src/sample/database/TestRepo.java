package sample.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestRepo extends DB {

    public TestRepo(String url) {
        super(url);
    }

    public String getUserRole() throws SQLException {
        String sql = "EXEC pr_user_role;";
        PreparedStatement stm = getConnection().prepareStatement(sql);
        ResultSet result = stm.executeQuery();
        if (result.next()) {
            return result.getString("");
        }
        return "";
    }
}
