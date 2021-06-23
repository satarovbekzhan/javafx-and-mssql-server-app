package sample.view.staff;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import sample.database.DB;
import sample.model.Item;
import sample.model.Order;
import sample.model.Status;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

public class OrderDetailsViewController {

    private Stage stage;
    private Order order;
    private final ObservableList<Item> itemObservableList = FXCollections.observableArrayList();

    @FXML
    private ListView<Item> itemListView;
    @FXML
    private Label priceLabel;
    @FXML
    private ChoiceBox<Status> statusChoiceBox;

    @FXML
    public void initialize() {
        itemListView.setItems(itemObservableList);
        itemListView.setCellFactory(param -> new ListCell<>() {
            @Override
            public void updateItem(Item item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(null);
                if (item ==null || empty) {
                    setText(null);
                } else {
                    DB.productRepo.getNewestPriceByProductId(item.getProduct().getId(), result -> {
                        setText(item.getProduct().getTitle() + "   •   " + item.getAmount() + "x" + result.getValue() +
                                "=" + result.getValue().multiply(new BigDecimal(item.getAmount())));
                    }, error -> setText(item.getProduct().getTitle() + "   •   " + item.getAmount() + "x0=0"));
                }
            }
        });
        statusChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Status object) {
                return object.getName();
            }

            @Override
            public Status fromString(String string) {
                return null;
            }
        });
    }

    public void setOrder(Order order, Stage stage) {
        this.stage = stage;
        this.order = order;
        DB.orderRepo.getItemsByOrder(order, result -> {
            itemObservableList.clear();
            itemObservableList.addAll(result);
            countTotalPrice();
        }, System.out::println);

        DB.orderRepo.getAllStatuses(result -> {
            for (Status s:result) {
                if (!s.getName().equals(this.order.getStatus().getName()))
                    statusChoiceBox.getItems().add(s);
            }
        }, System.out::println);
    }

    @FXML
    public void updateOrderStatus() {
        Status status = statusChoiceBox.getSelectionModel().getSelectedItem();
        if (status == null) {
            new Alert(Alert.AlertType.WARNING, "Neuer Status nicht ausgewählt!", ButtonType.OK).showAndWait();
            return;
        }
        DB.orderRepo.updateOrderStatus(new Order(order.getId(), status, null, null),
                result -> {
                    new Alert(Alert.AlertType.INFORMATION,
                            "Neuer Status erfolgreich gesetzt!!", ButtonType.OK).showAndWait();
                    stage.close();
                }, error -> new Alert(Alert.AlertType.ERROR, error, ButtonType.OK).showAndWait());
    }

    private void countTotalPrice() {
        if (itemObservableList.isEmpty()) priceLabel.setText("0 €");
        AtomicReference<BigDecimal> totalPrice = new AtomicReference<>(BigDecimal.ZERO);
        for (Item i:itemObservableList) {
            DB.productRepo.getNewestPriceByProductId(i.getProduct().getId(), result -> {
                totalPrice.set(totalPrice.get()
                        .add(result.getValue()
                                .multiply(new BigDecimal(i.getAmount()))));
                priceLabel.setText(totalPrice.get().toString() + " €");
            }, System.out::println);
        }
    }
}
