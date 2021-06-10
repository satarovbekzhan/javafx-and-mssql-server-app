package sample.database;

import sample.database.lamda.OnError;
import sample.database.lamda.OnSucceed;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TestRepo extends Repository {

    public TestRepo(DB db) {
        super(db);
    }

    public void getLoginData(OnSucceed<String> onSucceed, OnError onError) {
        String sql = "EXEC pr_loginData;";
        try {
            ResultSet result = query(sql);
            while (result.next()) {
                String str = result.getString("");
                onSucceed.operate(str);
            }
        } catch (SQLException e) {
            onError.operate(e.getMessage());
        }
    }
}
