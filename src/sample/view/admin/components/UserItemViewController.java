package sample.view.admin.components;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import sample.database.DB;
import sample.model.Role;
import sample.model.User;

public class UserItemViewController {
    @FXML private TextField emailTextField;
    @FXML private TextField passwordTextField;
    @FXML private ChoiceBox<Role> roleChoiceBox;

    private ObservableList<User> userObservableList;
    private User user;

    @FXML
    public void initialize() {
        roleChoiceBox.getItems().add(Role.ADMIN);
        roleChoiceBox.getItems().add(Role.STAFF);
        roleChoiceBox.getItems().add(Role.BUYER);
    }

    public void construct(User user, ObservableList<User> userObservableList) {
        this.user = user;
        emailTextField.setText(user.getEmail());
        emailTextField.setPromptText(user.getEmail());
        passwordTextField.setText(user.getPass());
        passwordTextField.setPromptText(user.getPass());
        roleChoiceBox.setValue(user.getRole());

        this.userObservableList = userObservableList;
    }

    public void updateUser() {
        //
    }

    public void deleteUser() {
        userObservableList.remove(user);
    }
}
