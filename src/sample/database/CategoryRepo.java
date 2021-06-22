package sample.database;

import sample.database.lamda.OnError;
import sample.database.lamda.OnSucceed;
import sample.model.Category;
import sample.model.User;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepo extends DB {

    public CategoryRepo(String url) {
        super(url);
    }

    public boolean hasItemsIn(int offset, int limit) {
        try {
            PreparedStatement stm = getConnection()
                    .prepareStatement("SELECT * FROM [category] ORDER BY [id] OFFSET ? ROWS FETCH NEXT ? ROWS ONLY;");
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

    public void getCategories(int offset, int limit, OnSucceed<List<Category>> onSucceed, OnError onError) {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT [id], [name]\n" +
                "FROM [dbo].[category]" +
                "ORDER BY [id]\n" +
                "OFFSET ? ROWS\n" +
                "FETCH NEXT ? ROWS ONLY;";;
        try {
            PreparedStatement stm = getConnection().prepareStatement(sql);
            stm.setInt(1, offset);
            stm.setInt(2, limit);
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

    public void deleteCategory(Category category, OnSucceed<Category> onSucceed, OnError onError) {
        onSucceed.operate(category);
    }
}
