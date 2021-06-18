package sample.view.admin.tabs;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import sample.database.DB;
import sample.model.Role;
import sample.model.User;

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
            private final TextField userEmailTextField = new TextField("");
            @Override
            protected void updateItem(User item, boolean empty) {
                if (item == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.getEmail());
                    userEmailTextField.setText(item.getId() + " - " + item.getEmail());
                    setGraphic(userEmailTextField);
                }
            }
        });

        roleChoiceBox.getItems().add(Role.ADMIN);
        roleChoiceBox.getItems().add(Role.STAFF);
        roleChoiceBox.getItems().add(Role.BUYER);
        roleChoiceBox.setValue(Role.BUYER);

        postInitialize();
    }

    private void postInitialize() {
        refreshUserObservableList();
    }

    private void refreshUserObservableList() {
        userObservableList.clear();
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
                result -> refreshUserObservableList(), this::alert);
    }

    private void alert(String text) {
        Alert alert = new Alert(Alert.AlertType.NONE,
                text, ButtonType.CLOSE);
        alert.showAndWait();
    }
}
