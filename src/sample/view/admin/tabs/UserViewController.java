package sample.view.admin.tabs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import sample.database.DB;
import sample.model.Role;
import sample.model.User;
import sample.view.admin.components.UserItemViewController;

import java.io.IOException;

public class UserViewController {

    @FXML
    private ListView<User> userListView;
    @FXML private TextField emailTextField;
    @FXML private TextField passwordTextField;
    @FXML private ChoiceBox<Role> roleChoiceBox;

    private final ObservableList<User> userObservableList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        userListView.setItems(userObservableList);
        userListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(null);
                    try {
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(this.getClass().getResource("../components/UserItemView.fxml"));
                        Parent userView = loader.load();
                        UserItemViewController userController = loader.getController();
                        userController.construct(item, userObservableList);
                        setGraphic(userView);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        roleChoiceBox.getItems().add(Role.ADMIN);
        roleChoiceBox.getItems().add(Role.STAFF);
        roleChoiceBox.getItems().add(Role.BUYER);
        roleChoiceBox.setValue(Role.BUYER);

        DB.userRepo.getAllUsers(userObservableList::addAll, System.out::println);
    }

    @FXML
    public void createNewUser() {
        String email = emailTextField.getText();
        String password = passwordTextField.getText();
        if (email.isBlank()) {
            alert("Geben Sie zuerst eine E-Mail ein!");
            return;
        }
        if (password.isBlank()) {
            alert("Geben Sie zuerst ein Passwort ein!");
            return;
        }
        DB.userRepo.createUser(new User(0, email, password, roleChoiceBox.getValue()),
                userObservableList::add, this::alert);
    }

    private void alert(String text) {
        Alert alert = new Alert(Alert.AlertType.NONE,
                text, ButtonType.CLOSE);
        alert.showAndWait();
    }
}
