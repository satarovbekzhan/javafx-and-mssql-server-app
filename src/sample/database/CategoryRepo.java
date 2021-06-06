package sample.database;

import sample.model.Category;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryRepo {
    private final DB db;

    public CategoryRepo(DB db) {
        this.db = db;
    }

    public Optional<List<Category>> getAllCategories() {
        List<Category> list = new ArrayList<>();
        String query = "SELECT TOP (1000) [id]\n" +
                "      ,[name]\n" +
                "  FROM [makk].[dbo].[category]";
        try {
            ResultSet result = db.query(query);
            while (result.next()) {
                Integer id = result.getInt("id");
                String name = result.getString("name");
                list.add(new Category(id, name));
            }
        } catch (SQLException e) {
            return Optional.empty();
        }
        return Optional.of(list);
    }
}
