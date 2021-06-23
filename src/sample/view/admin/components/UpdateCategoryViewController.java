package sample.view.admin.components;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import sample.database.DB;
import sample.model.Category;

public class UpdateCategoryViewController extends AdminComponentController {

    private Category category;

    @FXML
    private TextField nameTextField;

    public void setCategory(Category category) {
        this.category = category;
        nameTextField.setPromptText(category.getName());
        nameTextField.setText(category.getName());
    }

    @FXML
    public void updateCategoryName() {
        String categoryNewName = nameTextField.getText();
        if (categoryNewName.isBlank()) {
            new Alert(Alert.AlertType.WARNING, "Namensfeld ist leer!", ButtonType.OK).showAndWait();
            return;
        }

        if (categoryNewName.equals(category.getName())) {
            new Alert(Alert.AlertType.WARNING, "Keine Änderungen vorgenommen!", ButtonType.OK).showAndWait();
        } else {
            DB.categoryRepo.updateCategory(new Category(category.getId(), categoryNewName), result -> {
                getObjectObservableList().remove(category);
                getObjectObservableList().add(result);
                new Alert(Alert.AlertType.INFORMATION, "Geschafft!", ButtonType.OK).showAndWait();
                getStage().close();
            }, error -> new Alert(Alert.AlertType.ERROR, error, ButtonType.OK).showAndWait());
        }
    }
}
