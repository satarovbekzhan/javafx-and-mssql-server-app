package sample.view.buyer;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.database.DB;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicReference;

public class CartViewController {

    private Stage stage;
    private HashMap<Integer, Integer> orderItems;

    @FXML
    private VBox itemsVBox;
    @FXML
    private Label totalPriceLabel;
    @FXML
    private Button orderButton;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setOrderItems(HashMap<Integer, Integer> orderItems) {
        this.orderItems = orderItems;
        countTotalPrice();
        renderCartItems();
    }

    private void renderCartItems() {
        if (orderItems.isEmpty()) itemsVBox.getChildren().add(new Label("Warenkorb ist noch leer!"));
        itemsVBox.getChildren().clear();
        for (Entry<Integer, Integer> entry : orderItems.entrySet()) {
            Integer id = entry.getKey();
            Integer amount = entry.getValue();
            DB.productRepo.getProductById(id, result -> {
                Label t = new Label(result.getTitle());
                t.setStyle("-fx-font-weight: bold;-fx-padding: 30 0 0 0;");
                Button minus = new Button("verringern");
                minus.setPrefWidth(100);
                minus.setStyle("-fx-font-size: 14;");
                Label p = new Label();
                p.setStyle("-fx-alignment: center;");
                DB.productRepo.getNewestPriceByProductId(id, price -> {
                    p.setText(amount + "x" + price.getValue() + "=" +
                            price.getValue().multiply(new BigDecimal(amount)) + "€");
                }, System.out::println);
                Button plus = new Button("erhöhen");
                plus.setPrefWidth(100);
                plus.setStyle("-fx-font-size: 14;");
                BorderPane bp = new BorderPane(p, null, plus, null, minus);
                itemsVBox.getChildren().add(t);
                itemsVBox.getChildren().add(bp);
                minus.setOnAction(event -> decreaseAmount(id));
                plus.setOnAction(event -> increaseAmount(id));
            }, System.out::println);
        }
    }

    private void countTotalPrice() {
        if (orderItems.isEmpty()) totalPriceLabel.setText("0 €");
        AtomicReference<BigDecimal> totalPrice = new AtomicReference<>(BigDecimal.ZERO);
        for (Entry<Integer, Integer> entry : orderItems.entrySet()) {
            Integer id = entry.getKey();
            Integer amount = entry.getValue();
            DB.productRepo.getNewestPriceByProductId(id, result -> {
                if (orderItems.containsKey(result.getProduct())) {
                    totalPrice.set(totalPrice.get()
                            .add(result.getValue()
                                    .multiply(new BigDecimal(amount))));
                    totalPriceLabel.setText(totalPrice.get().toString() + " €");
                }
            }, System.out::println);
        }
    }

    private void increaseAmount(Integer id) {
        orderItems.put(id, orderItems.get(id) + 1);
        renderCartItems();
        countTotalPrice();
    }

    private void decreaseAmount(Integer id) {
        if (orderItems.get(id) > 1) orderItems.put(id, orderItems.get(id) - 1);
        else orderItems.remove(id);
        renderCartItems();
        countTotalPrice();
        if (orderItems.isEmpty()) stage.close();
    }

    @FXML
    public void orderProducts() {
        if (orderItems.isEmpty()) return;
        orderButton.setDisable(true);
        DB.orderRepo.makeOrder(orderId -> {
            Iterator it = orderItems.entrySet().iterator();
            while (it.hasNext()) {
                Entry<Integer, Integer> entry = (Entry<Integer, Integer>) it.next();
                Integer p_id = entry.getKey();
                Integer amount = entry.getValue();
                DB.orderRepo.addItemToOrder(orderId, p_id, amount, result -> {}, System.out::println);
                it.remove();
            }
            new Alert(Alert.AlertType.INFORMATION, "Vielen Dank für Ihre Bestellung!", ButtonType.OK).showAndWait();
            orderButton.setDisable(false);
            renderCartItems();
        }, error -> new Alert(Alert.AlertType.ERROR, error, ButtonType.OK).showAndWait());
    }
}
