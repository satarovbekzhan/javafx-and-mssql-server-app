package sample.database;

import sample.database.lamda.OnError;
import sample.database.lamda.OnSucceed;
import sample.model.Category;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepo extends Repository {

    public CategoryRepo(DB db) {
        super(db);
    }

    public void getAllCategories(OnSucceed<List<Category>> onSucceed, OnError onError) {
        List<Category> list = new ArrayList<>();
        String query = "SELECT [id]\n" +
                "      ,[name]\n" +
                "  FROM [makk].[dbo].[category]";
        try {
            ResultSet result = query(query);
            while (result.next()) {
                Integer id = result.getInt("id");
                String name = result.getString("name");
                list.add(new Category(id, name));
            }
            onSucceed.operate(list);
        } catch (SQLException e) {
            onError.operate(e.getMessage());
        }
    }
}
