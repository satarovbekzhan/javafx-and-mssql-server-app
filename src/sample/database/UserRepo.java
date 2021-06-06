package sample.database;

import sample.model.Role;
import sample.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepo {
    private final DB db;

    public UserRepo(DB db) {
        this.db = db;
    }

    public Optional<List<User>> getAllUsers() {
        List<User> list = new ArrayList<>();
        String query = "SELECT TOP (1000) [id]\n" +
                "      ,[firstname]\n" +
                "      ,[lastname]\n" +
                "      ,[email]\n" +
                "      ,[password]\n" +
                "      ,[role]\n" +
                "  FROM [makk].[dbo].[user]";
        try {
            ResultSet result = db.query(query);
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
            return Optional.empty();
        }
        return Optional.of(list);
    }
}
