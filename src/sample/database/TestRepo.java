package sample.database;

import sample.database.lamda.OnError;
import sample.database.lamda.OnSucceed;

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
            String str = result.getString("");
            return str;
        }
        return "";
    }

//    public boolean auth() {
//        String sql = "";
//        try {
//            PreparedStatement stm = getConnection().prepareStatement(sql);
//            ResultSet result = stm.executeQuery();
//            while (result.next()) {
//                String str = result.getString("");
//                onSucceed.operate(str);
//            }
//        } catch (SQLException e) {
//            onError.operate(e.getMessage());
//        }
//    }
}
