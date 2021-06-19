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
                "      ,[email]\n" +
                "      ,[pass]\n" +
                "      ,[role]\n" +
                "  FROM [dbo].[user]";
        try {
            PreparedStatement stm = getConnection().prepareStatement(sql);
            ResultSet result = stm.executeQuery();
            while (result.next()) {
                Integer id = result.getInt("id");
                String email = result.getString("email");
                String password = result.getString("pass");
                Role role = null;
                switch (result.getString("role")) {
                    case "ADMIN": role = Role.ADMIN;
                    break;
                    case "STAFF": role = Role.STAFF;
                    break;
                    case "BUYER": role = Role.BUYER;
                }
                list.add(new User(id, email, password, role));
            }
        } catch (SQLException e) {
            onError.operate(e.getMessage());
        }
        onSucceed.operate(list);
    }

    public void createUser(User user, OnSucceed<User> onSucceed, OnError onError) {
        String sql = "EXEC sp_create_user @email = ?, @pass = ?, @role = ?;";
        try {
            PreparedStatement stm = getConnection().prepareStatement(sql);
            stm.setString(1, user.getEmail());
            stm.setString(2, user.getPass());
            stm.setString(3, user.getRole().toString());
            int i = stm.executeUpdate();
            if (i > 0) onSucceed.operate(user);
            else onError.operate("Error while creating a user");
        } catch (SQLException e) {
            onError.operate(e.getMessage());
        }
    }
}
