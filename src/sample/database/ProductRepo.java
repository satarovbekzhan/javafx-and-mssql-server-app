package sample.database;

import sample.database.lamda.OnError;
import sample.database.lamda.OnSucceed;
import sample.model.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductRepo extends Repository {

    public ProductRepo(DB db) {
        super(db);
    }

    public void getProductsByCategoryId(Integer categoryId, OnSucceed<List<Product>> onSucceed, OnError onError) {
        List<Product> list = new ArrayList<>();
        String query = "SELECT P.[id], P.[title], P.[description], P.[picture], P.[ingredients]\n" +
                "FROM [dbo].[product] AS P\n" +
                "INNER JOIN [dbo].[ref_product_category] AS R ON R.[product] = P.[id]\n" +
                "INNER JOIN [dbo].[category] AS C ON C.[id] = R.[category]\n" +
                "WHERE C.[id] = " + categoryId + ";";
        try {
            ResultSet result = query(query);
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
//        List<Product> products = new ArrayList<>();
////        products.add()
//        onSucceed.operate(products);
    }
}
