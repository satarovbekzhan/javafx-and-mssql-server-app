package sample.view.staff;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import sample.Main;
import sample.database.DB;
import sample.model.Order;
import sample.model.Status;
import sample.view.Controller;
import sample.view.LoginViewController;

import java.io.IOException;

public class StaffViewController extends Controller {

    @FXML
    private ChoiceBox<Status> statusChoiceBox;
    @FXML
    private ListView<Order> orderListView;

    private final ObservableList<Order> orderObservableList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
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
        statusChoiceBox.setOnAction(this::onStatusChoiceChanged);
        orderListView.setItems(orderObservableList);
        orderListView.setCellFactory(param -> new ListCell<>() {
            @Override
            public void updateItem(Order order, boolean empty) {
                super.updateItem(order, empty);
                setGraphic(null);
                if (empty) {
                    setText(null);
                } else {
                    setText(" " + order.getId() + "   •   " + order.getUser() +
                            "   •   " + order.getStatus().getName() + "   •   " + order.getOrdered());
                }
            }
        });
        orderListView.setOnMouseClicked(click -> {
            if (click.getClickCount() == 2) {
                Order order = orderListView.getSelectionModel()
                        .getSelectedItem();
                try {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(this.getClass().getResource("OrderDetailsView.fxml"));
                    Parent root = loader.load();
                    Stage stage = new Stage();
                    OrderDetailsViewController odc = loader.getController();
                    odc.setOrder(order, stage);
                    stage.setTitle("Order#" + order.getId());
                    stage.setScene(new Scene(root));
                    stage.setResizable(false);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        loadStatuses();
    }

    @FXML
    public void onStatusChoiceChanged(ActionEvent event) {
        Status status = statusChoiceBox.getSelectionModel().getSelectedItem();
        DB.orderRepo.getOrdersByStatus(status, orders -> {
            orderObservableList.clear();
            orderObservableList.addAll(orders);
        }, System.out::println);
    }

    private void loadStatuses() {
        DB.orderRepo.getAllStatuses(result -> {
            for (Status s:result) {
                statusChoiceBox.getItems().add(s);
            }
            DB.orderRepo.getOrdersByStatus(null, orders -> {
                orderObservableList.clear();
                orderObservableList.addAll(orders);
            }, System.out::println);
        }, System.out::println);
    }

    @FXML
    public void logout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/LoginView.fxml"));
            Parent root = loader.load();
            LoginViewController lvc = loader.getController();
            lvc.setApp(getStage());
            getStage().setTitle("MAKK");
            getStage().setScene(new Scene(root));
            getStage().setResizable(false);
            getStage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
