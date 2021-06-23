package sample.database;

import sample.database.lamda.OnError;
import sample.database.lamda.OnSucceed;
import sample.model.Role;
import sample.model.User;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserRepo extends DB {

    public UserRepo(String url) {
        super(url);
    }

    public boolean hasItemsIn(int offset, int limit) {
        try {
            PreparedStatement stm = getConnection()
                    .prepareStatement("SELECT * FROM [user] ORDER BY [id] OFFSET ? ROWS FETCH NEXT ? ROWS ONLY;");
            stm.setInt(1, offset);
            stm.setInt(2, limit);
            ResultSet result = stm.executeQuery();
            if (result.next()) {
                return true;
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }

    public void getUsers(int offset, int limit, OnSucceed<List<User>> onSucceed, OnError onError) {
        List<User> list = new ArrayList<>();
        String sql = "SELECT [id], [email], [pass], [role]\n" +
                "FROM [dbo].[user]\n" +
                "ORDER BY [id]\n" +
                "OFFSET ? ROWS\n" +
                "FETCH NEXT ? ROWS ONLY;";
        try {
            PreparedStatement stm = getConnection().prepareStatement(sql);
            stm.setInt(1, offset);
            stm.setInt(2, limit);
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

    public void getUserByEmail(String email, OnSucceed<User> onSucceed, OnError onError) {
        String sql = "SELECT [id]\n" +
                "      ,[email]\n" +
                "      ,[pass]\n" +
                "      ,[role]\n" +
                "  FROM [dbo].[user]\n" +
                "  WHERE [email] = ?";
        try {
            PreparedStatement stm = getConnection().prepareStatement(sql);
            stm.setString(1, email);
            ResultSet result = stm.executeQuery();
            if (result.next()) {
                Integer userId = result.getInt("id");
                String userEmail = result.getString("email");
                String userPassword = result.getString("pass");
                Role userRole = null;
                switch (result.getString("role")) {
                    case "ADMIN": userRole = Role.ADMIN;
                        break;
                    case "STAFF": userRole = Role.STAFF;
                        break;
                    case "BUYER": userRole = Role.BUYER;
                }
                onSucceed.operate(new User(userId, userEmail, userPassword, userRole));
            }
        } catch (SQLException e) {
            onError.operate(e.getMessage());
        }
    }

    public void createUser(User user, OnSucceed<User> onSucceed, OnError onError) {
        String sql = "{call dbo.sp_create_user(?, ?, ?)}";

        try {
            CallableStatement stm = getConnection().prepareCall(sql);
            stm.setString(1, user.getEmail());
            stm.setString(2, user.getPass());
            stm.setString(3, user.getRole().toString());

            stm.execute();
            getUserByEmail(user.getEmail(), onSucceed, onError);
        } catch (SQLException e) {
            onError.operate(e.getMessage());
        }
    }

    public void deleteUser(User user, OnSucceed<User> onSucceed, OnError onError) {
        String sql = "{call dbo.sp_delete_user(?)}";
        try {
            CallableStatement stm = getConnection().prepareCall(sql);
            stm.setString(1, user.getEmail());

            stm.execute();

            onSucceed.operate(user);
        } catch (SQLException e) {
            onError.operate(e.getMessage());
        }
    }

    public void updateUser(User user, OnSucceed<User> onSucceed, OnError onError) {
        String sql = "{call dbo.sp_update_user(?, ?, ?, ?)}";
        try {
            CallableStatement stm = getConnection().prepareCall(sql);
            stm.setInt(1, user.getId());
            stm.setString(2, user.getEmail());
            stm.setString(3, user.getPass());
            stm.setString(4, user.getRole().toString());

            stm.execute();
            getUserByEmail(user.getEmail(), onSucceed, onError);
        } catch (SQLException e) {
            onError.operate(e.getMessage());
        }
    }
}
