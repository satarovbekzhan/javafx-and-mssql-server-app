package sample.view.admin.components;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import sample.database.DB;
import sample.model.Role;
import sample.model.User;

public class UserItemViewController {
    @FXML
    private Label userIdLabel;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private ChoiceBox<Role> roleChoiceBox;

    private ObservableList<User> userObservableList;
    private User user;

    @FXML
    public void initialize() {
        roleChoiceBox.getItems().add(Role.STAFF);
        roleChoiceBox.getItems().add(Role.BUYER);
    }

    public void construct(User user, ObservableList<User> userObservableList) {
        this.user = user;
        userIdLabel.setText(user.getId().toString());
        emailTextField.setText(user.getEmail());
        emailTextField.setPromptText(user.getEmail());
        passwordTextField.setText(user.getPass());
        passwordTextField.setPromptText(user.getPass());
        roleChoiceBox.setValue(user.getRole());

        this.userObservableList = userObservableList;
    }

    @FXML
    public void updateUser() {
        if (emailTextField.getText().equals(user.getEmail()) &&
                passwordTextField.getText().equals(user.getPass()) &&
                roleChoiceBox.getSelectionModel().getSelectedItem() == user.getRole()) {
            new Alert(Alert.AlertType.WARNING, "User data has no changes!", ButtonType.OK).showAndWait();
            return;
        }
        DB.userRepo.updateUser(new User(user.getId(), emailTextField.getText(), passwordTextField.getText(),
                        roleChoiceBox.getSelectionModel().getSelectedItem()),
                result -> {
                    userObservableList.remove(user);
                    userObservableList.add(result);
                    new Alert(Alert.AlertType.CONFIRMATION, "User data has been updated!", ButtonType.OK).showAndWait();
                },
                error -> new Alert(Alert.AlertType.ERROR, error, ButtonType.OK).showAndWait());
    }

    @FXML
    public void deleteUser() {
        DB.userRepo.deleteUser(user,
                result -> {
                    userObservableList.remove(user);
                    new Alert(Alert.AlertType.CONFIRMATION, "User deleted!", ButtonType.OK).showAndWait();
                },
                error -> new Alert(Alert.AlertType.ERROR, error, ButtonType.OK).showAndWait());
    }
}
