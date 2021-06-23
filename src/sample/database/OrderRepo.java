package sample.database;

import sample.database.lamda.OnError;
import sample.database.lamda.OnSucceed;
import sample.model.Item;
import sample.model.Order;
import sample.model.Product;
import sample.model.Status;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderRepo extends DB {

    protected OrderRepo(String url) {
        super(url);
    }

    public void getAllStatuses(OnSucceed<List<Status>> onSucceed, OnError onError) {
        List<Status> list = new ArrayList<>();
        String sql = "SELECT [id], [name] FROM [dbo].[status];";
        try {
            PreparedStatement stm = getConnection().prepareStatement(sql);
            ResultSet result = stm.executeQuery();
            while (result.next()) {
                Integer id = result.getInt("id");
                String name = result.getString("name");
                list.add(new Status(id, name));
            }
            onSucceed.operate(list);
        } catch (SQLException e) {
            onError.operate(e.getMessage());
        }
    }

//    public void getOrdersByStatus(Status status, OnSucceed<List<Product>> onSucceed, OnError onError) {
//        String sql = "";
//        if (status == null) sql = "";
//        else sql = "SELECT P.[id], P.[title], P.[description], P.[picture], P.[ingredients]\n" +
//                "FROM [dbo].[product] AS P\n" +
//                "ORDER BY [id]\n" +
//                "OFFSET ? ROWS\n" +
//                "FETCH NEXT ? ROWS ONLY;";
//        try {
//            PreparedStatement stm = getConnection().prepareStatement(sql);
//            stm.setInt(1, offset);
//            stm.setInt(2, limit);
//            ResultSet result = stm.executeQuery();
//            while (result.next()) {
//                Integer id = result.getInt("id");
//                String title = result.getString("title");
//                String description = result.getString("description");
//                String picture = result.getString("picture");
//                String ingredients = result.getString("ingredients");
//                list.add(new Product(id, title, description, picture, ingredients));
//            }
//            onSucceed.operate(list);
//        } catch (SQLException e) {
//            onError.operate(e.getMessage());
//        }
//    }

    public void makeOrder(OnSucceed<Integer> onSucceed, OnError onError) {
        String sql = "{call dbo.sp_make_order}";

        try {
            CallableStatement stm = getConnection().prepareCall(sql);
            ResultSet result = stm.executeQuery();
            if (result.next()) {
                Integer id = result.getInt("id");
                onSucceed.operate(id);
            }
        } catch (SQLException e) {
            onError.operate(e.getMessage());
        }
    }

    public void addItemToOrder(int o_id, int p_id, int a, OnSucceed<Boolean> onSucceed, OnError onError) {
        String sql = "INSERT INTO [dbo].[item] ([order], [product], [amount])\n" +
                "VALUES (?, ?, ?);";
        try {
            PreparedStatement stm = getConnection().prepareStatement(sql);
            stm.setInt(1, o_id);
            stm.setInt(2, p_id);
            stm.setInt(3, a);
            int count = stm.executeUpdate();
            if (count > 0) onSucceed.operate(true);
        } catch (SQLException e) {
            onError.operate(e.getMessage());
        }
    }

}
