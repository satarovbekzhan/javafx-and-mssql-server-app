package sample.view.admin.components;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import sample.database.DB;
import sample.model.Role;
import sample.model.User;

public class CreateUserViewController extends AdminComponentController {

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
        roleChoiceBox.setValue(Role.BUYER);
    }

    @FXML
    public void createNewUser() {
        String email = emailTextField.getText();
        String password = passwordTextField.getText();
        if (email.isBlank()) {
            new Alert(Alert.AlertType.WARNING, "E-Mail-Feld ist leer!", ButtonType.OK).showAndWait();
            return;
        }
        if (password.isBlank()) {
            new Alert(Alert.AlertType.WARNING, "Password-Feld ist leer!", ButtonType.OK).showAndWait();
            return;
        }
        DB.userRepo.createUser(new User(0, email, password, roleChoiceBox.getSelectionModel().getSelectedItem()),
                result -> {
                    new Alert(Alert.AlertType.WARNING, "Geschafft! ", ButtonType.OK).showAndWait();
                    getObjectObservableList().add(result);
                    getStage().close();
                }, error -> new Alert(Alert.AlertType.ERROR, error, ButtonType.OK).showAndWait());
    }
}
