package sample.database;

import sample.database.lamda.OnError;
import sample.database.lamda.OnSucceed;
import sample.model.Product;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductRepo extends DB {

    public ProductRepo(String url) {
        super(url);
    }

    public void getProductsByCategoryId(Integer categoryId, OnSucceed<List<Product>> onSucceed, OnError onError) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT P.[id], P.[title], P.[description], P.[picture], P.[ingredients]\n" +
                "FROM [dbo].[product] AS P\n" +
                "INNER JOIN [dbo].[ref_product_category] AS R ON R.[product] = P.[id]\n" +
                "INNER JOIN [dbo].[category] AS C ON C.[id] = R.[category]\n" +
                "WHERE C.[id] = ?;";
        try {
            PreparedStatement stm = getConnection().prepareStatement(sql);
            stm.setInt(1, categoryId);
            ResultSet result = stm.executeQuery();
            while (result.next()) {
                Integer id = result.getInt("id");
                String title = result.getString("title");
                String description = result.getString("description");
                String picture = result.getString("picture");
                String ingredients = result.getString("ingredients");
                list.add(new Product(id, title, description, picture, ingredients));
            }
            onSucceed.operate(list);
        } catch (SQLException e) {
            onError.operate(e.getMessage());
        }
    }
}
