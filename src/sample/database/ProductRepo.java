package sample.database;

import sample.database.lamda.OnError;
import sample.database.lamda.OnSucceed;
import sample.model.Price;
import sample.model.Product;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductRepo extends DB {

    public ProductRepo(String url) {
        super(url);
    }

    public boolean hasItemsIn(int offset, int limit) {
        try {
            PreparedStatement stm = getConnection()
                    .prepareStatement("SELECT * FROM [product] ORDER BY [id] OFFSET ? ROWS FETCH NEXT ? ROWS ONLY;");
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

    public void getProducts(int offset, int limit, OnSucceed<List<Product>> onSucceed, OnError onError) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT P.[id], P.[title], P.[description], P.[picture], P.[ingredients]\n" +
                "FROM [dbo].[product] AS P\n" +
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

    public void getProductByTitle(Product product, OnSucceed<Product> onSucceed, OnError onError) {
        onSucceed.operate(product);
    }

    public void createProduct(Product product, OnSucceed<Product> onSucceed, OnError onError) {
        onSucceed.operate(product);
    }

    public void updateProduct(Product product, OnSucceed<Product> onSucceed, OnError onError) {
        onSucceed.operate(product);
    }

    public void deleteProduct(Product product, OnSucceed<Product> onSucceed, OnError onError) {
        onSucceed.operate(product);
    }

    public void getNewestPriceByProduct(Product product, OnSucceed<Price> onSucceed, OnError onError) {
        String sql = "SELECT [id], [product], [value], [starting]\n" +
                "FROM [dbo].[price]\n" +
                "WHERE starting IN (SELECT max(starting) FROM [dbo].[price] WHERE [product] = ?);";
        try {
            PreparedStatement stm = getConnection().prepareStatement(sql);
            stm.setInt(1, product.getId());
            ResultSet result = stm.executeQuery();
            if (result.next()) {
                Integer id = result.getInt("id");
                Integer product_id = result.getInt("product");
                BigDecimal value = result.getBigDecimal("value");
                String starting = result.getString("starting");
                onSucceed.operate(new Price(id, product_id, value, starting));
            }
        } catch (SQLException e) {
            onError.operate(e.getMessage());
        }
    }
}
