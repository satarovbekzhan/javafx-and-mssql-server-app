package sample.database;

import sample.database.lamda.OnError;
import sample.database.lamda.OnSucceed;
import sample.model.Composition;
import sample.model.Nutrient;
import sample.model.Unit;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompositionRepo extends DB {

    protected CompositionRepo(String url) {
        super(url);
    }

    public void getCompositionsByProductId(Integer productId, OnSucceed<List<Composition>> onSucceed, OnError onError) {
        List<Composition> list = new ArrayList<>();
        String sql = "SELECT C.[id], N.[id] as n_id , N.[name] as nutrient, " +
                "U.[id] as u_id, U.[name] AS unit, C.[pro_100], C.[pro_por]\n" +
                "FROM [dbo].[composition] AS C\n" +
                "INNER JOIN [dbo].[product] AS P ON P.[id] = C.[product]\n" +
                "INNER JOIN [dbo].[nutrient] AS N ON N.[id] = C.[nutrient]\n" +
                "INNER JOIN [dbo].[unit] AS U ON U.[id] = C.[unit]\n" +
                "WHERE P.[id] = ?;";
        try {
            PreparedStatement stm = getConnection().prepareStatement(sql);
            stm.setInt(1, productId);
            ResultSet result = stm.executeQuery();
            while (result.next()) {
                Integer id = result.getInt("id");
                Integer n_id = result.getInt("n_id");
                String nutrient = result.getString("nutrient");
                Integer u_id = result.getInt("u_id");
                String unit = result.getString("unit");
                BigDecimal pro_100 = result.getBigDecimal("pro_100");
                BigDecimal pro_por = result.getBigDecimal("pro_por");
                list.add(new Composition(id, new Nutrient(n_id, nutrient), new Unit(u_id, unit), pro_100, pro_por));
            }
            onSucceed.operate(list);
        } catch (SQLException e) {
            onError.operate(e.getMessage());
        }
    }
}
