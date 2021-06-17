package sample.database;

import sample.database.lamda.OnError;
import sample.database.lamda.OnSucceed;
import sample.model.Role;
import sample.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepo extends DB {

    public UserRepo(String url) {
        super(url);
    }

    public void getAllUsers(OnSucceed<List<User>> onSucceed, OnError onError) {
        List<User> list = new ArrayList<>();
        String sql = "SELECT TOP (1000) [id]\n" +
                "      ,[firstname]\n" +
                "      ,[lastname]\n" +
                "      ,[email]\n" +
                "      ,[password]\n" +
                "      ,[role]\n" +
                "  FROM [makk].[dbo].[user]";
        try {
            PreparedStatement stm = getConnection().prepareStatement(sql);
            ResultSet result = stm.executeQuery();
            while (result.next()) {
                Integer id = result.getInt("id");
                String firstname = result.getString("firstname");
                String lastname = result.getString("lastname");
                String email = result.getString("email");
                String password = result.getString("password");
                Role role = null;
                switch (result.getString("role")) {
                    case "ADMIN": role = Role.ADMIN;
                    break;
                    case "STAFF": role = Role.STAFF;
                    break;
                    case "BUYER": role = Role.BUYER;
                }
                list.add(new User(id, firstname, lastname, email, password, role));
            }
        } catch (SQLException e) {
            onError.operate(e.getMessage());
        }
        onSucceed.operate(list);
    }
}
