package sample.view.admin.components;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import sample.database.DB;
import sample.model.Category;

public class CreateCategoryViewController extends AdminComponentController {

    @FXML
    private TextField nameTextField;

    @FXML
    public void initialize() {}

    @FXML
    public void createCategory() {
        String categoryName = nameTextField.getText();
        if (!categoryName.isBlank()) {
            DB.categoryRepo.createCategory(new Category(0, categoryName), result -> {
                getObjectObservableList().add(result);
                new Alert(Alert.AlertType.INFORMATION, "Geschafft!", ButtonType.OK).showAndWait();
                getStage().close();
            }, error -> new Alert(Alert.AlertType.ERROR, error, ButtonType.OK).showAndWait());
        } else new Alert(Alert.AlertType.WARNING, "Namensfeld ist leer!", ButtonType.OK).showAndWait();
    }
}
