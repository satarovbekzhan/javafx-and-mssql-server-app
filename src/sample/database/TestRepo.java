package sample.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestRepo extends DB {

    public TestRepo(String url) {
        super(url);
    }

    public String getLoginData() throws SQLException {
        String sql = "EXEC pr_loginData;";
        PreparedStatement stm = getConnection().prepareStatement(sql);
        ResultSet result = stm.executeQuery();
        while (result.next()) {
            return result.getString("");
        }
        return "";
    }
}
