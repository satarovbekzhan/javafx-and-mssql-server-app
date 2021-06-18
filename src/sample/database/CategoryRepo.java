package sample.database;

import sample.database.lamda.OnError;
import sample.database.lamda.OnSucceed;
import sample.model.Category;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepo extends DB {

    public CategoryRepo(String url) {
        super(url);
    }

    public void getAllCategories(OnSucceed<List<Category>> onSucceed, OnError onError) {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT [id]\n" +
                "      ,[name]\n" +
                "  FROM [dbo].[category]";
        try {
            PreparedStatement stm = getConnection().prepareStatement(sql);
            ResultSet result = stm.executeQuery();
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
