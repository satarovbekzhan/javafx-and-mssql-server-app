package sample.database;

import sample.database.lamda.OnError;
import sample.database.lamda.OnSucceed;
import sample.model.*;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    public void getOrdersByStatus(Status status, OnSucceed<List<Order>> onSucceed, OnError onError) {
        List<Order> list = new ArrayList<>();
        String sql = "";
        if (status == null) sql = "SELECT O.[id], O.[ordered],\n" +
                "\tS.[id] AS s_id, S.[name] AS s_name, \n" +
                "\tU.[id] AS u_id, U.[email] AS u_email\n" +
                "FROM [dbo].[order] AS O\n" +
                "JOIN [dbo].[status] AS S ON S.[id] = O.[status]\n" +
                "JOIN [dbo].[user] AS U ON U.[id] = O.[user];";
        else sql = "SELECT O.[id], O.[ordered],\n" +
                "\tS.[id] AS s_id, S.[name] AS s_name, \n" +
                "\tU.[id] AS u_id, U.[email] AS u_email\n" +
                "FROM [dbo].[order] AS O\n" +
                "JOIN [dbo].[status] AS S ON S.[id] = O.[status]\n" +
                "JOIN [dbo].[user] AS U ON U.[id] = O.[user]\n" +
                "WHERE S.[id] = ?;";
        try {
            PreparedStatement stm = getConnection().prepareStatement(sql);
            if (status != null) stm.setInt(1, status.getId());
            ResultSet result = stm.executeQuery();
            while (result.next()) {
                Integer id = result.getInt("id");
                Integer s_id = result.getInt("s_id");
                String s_name = result.getString("s_name");
                String u_email = result.getString("u_email");
                String ordered = result.getString("ordered");
                list.add(new Order(id, new Status(s_id, s_name), u_email, getDateFromString(ordered)));
            }
            onSucceed.operate(list);
        } catch (SQLException e) {
            onError.operate(e.getMessage());
        }
    }

    public void getItemsByOrder(Order order, OnSucceed<List<Item>> onSucceed, OnError onError) {
        List<Item> list = new ArrayList<>();
        String sql = "SELECT I.[id], I.[order], P.[id] AS 'p_id', P.[title] AS 'p_title', I.[amount]\n" +
                "FROM [dbo].[item] AS I\n" +
                "JOIN [dbo].[product] AS P ON P.[id] = I.[product]\n" +
                "WHERE I.[order] = ?;";
        try {
            PreparedStatement stm = getConnection().prepareStatement(sql);
            stm.setInt(1, order.getId());
            ResultSet result = stm.executeQuery();
            while (result.next()) {
                Integer id = result.getInt("id");
                Integer orderId = result.getInt("order");
                Integer p_id = result.getInt("p_id");
                String p_title = result.getString("p_title");
                Integer amount = result.getInt("amount");
                list.add(new Item(id, orderId, new Product(p_id, p_title, "", "", ""), amount));
            }
            onSucceed.operate(list);
        } catch (SQLException e) {
            onError.operate(e.getMessage());
        }
    }

    public void updateOrderStatus(Order order, OnSucceed<Order> onSucceed, OnError onError) {

        String sql = "UPDATE [dbo].[order] SET [status] = ?\n" +
                "WHERE [id] = ?;";
        try {
            PreparedStatement stm = getConnection().prepareStatement(sql);
            stm.setInt(1, order.getStatus().getId());
            stm.setInt(2, order.getId());
            int count = stm.executeUpdate();
            if (count > 0) onSucceed.operate(order);
            if (count == 0) onError.operate("Keine Änderungen vorgenommen!");
        } catch (SQLException e) {
            onError.operate(e.getMessage());
        }
    }

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

    private static Date getDateFromString(String datetime) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(datetime);
        } catch (ParseException e) {
            return new Date();
        }
    }

}
