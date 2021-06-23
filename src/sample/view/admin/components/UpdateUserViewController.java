package sample.view.admin.components;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import sample.database.DB;
import sample.model.Role;
import sample.model.User;

public class UpdateUserViewController extends AdminComponentController {

    private User user;

    @FXML
    private Label idLabel;
    @FXML
    private ChoiceBox<Role> roleChoiceBox;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField passwordTextField;

    @FXML
    public void initialize() {
        roleChoiceBox.getItems().add(Role.BUYER);
        roleChoiceBox.getItems().add(Role.STAFF);
    }

    @FXML
    public void updateNewUser() {
        String newEmail = emailTextField.getText();
        String newPassword = passwordTextField.getText();
        if (newEmail.isBlank()) {
            new Alert(Alert.AlertType.WARNING, "E-Mail-Feld ist leer!", ButtonType.OK).showAndWait();
            return;
        }
        if (newPassword.isBlank()) {
            new Alert(Alert.AlertType.WARNING, "Password-Feld ist leer!", ButtonType.OK).showAndWait();
            return;
        }

        if (newEmail.equals(user.getEmail()) && newPassword.equals(user.getPass()) &&
                roleChoiceBox.getSelectionModel().getSelectedItem() == user.getRole()) {
            new Alert(Alert.AlertType.WARNING, "Keine Änderungen vorgenommen! ", ButtonType.OK).showAndWait();
            getStage().close();
            return;
        }

        DB.userRepo.updateUser(new User(user.getId(), newEmail, newPassword, roleChoiceBox.getSelectionModel().getSelectedItem()),
                result -> {
                    new Alert(Alert.AlertType.WARNING, "Erfolgreich geändert!", ButtonType.OK).showAndWait();
                    getObjectObservableList().remove(user);
                    getObjectObservableList().add(result);
                    getStage().close();
                }, error -> new Alert(Alert.AlertType.ERROR, error, ButtonType.OK).showAndWait());
    }

    public void setUser(User user) {
        this.user = user;
        idLabel.setText("Benutzer mit id " + user.getId());
        roleChoiceBox.setValue(user.getRole());
        emailTextField.setPromptText(user.getEmail());
        emailTextField.setText(user.getEmail());
        passwordTextField.setPromptText(user.getPass());
        passwordTextField.setText(user.getPass());
    }
}
