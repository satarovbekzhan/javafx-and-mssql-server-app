package sample.view.admin.components;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import sample.model.Category;
import sample.model.Composition;
import sample.model.Nutrient;
import sample.model.Unit;

import java.util.ArrayList;
import java.util.List;

public class CreateProductViewController extends AdminComponentController {

    @FXML
    private TextField productTitleTextField;
    @FXML
    private TextField productPictureTextField;
    @FXML
    private TextField productPriceTextField;
    @FXML
    private TextArea productDescriptionTextArea;

    @FXML
    private ChoiceBox<Nutrient> nutrientChoiceBox;
    @FXML
    private ChoiceBox<Unit> unitChoiceBox;
    @FXML
    private TextField proHunTextField;
    @FXML
    private TextField proPorTextField;

    @FXML
    private ChoiceBox<Category> categoryChoiceBox;

    private List<Composition> productCompositions;
    private List<Category> productCategories;

    @FXML
    public void initialize() {
        productCompositions = new ArrayList<>();
        productCategories = new ArrayList<>();
        //
    }

    @FXML
    public void addCompositionProduct() {}

    @FXML
    public void addProductToCategory() {}

    @FXML
    public void createProduct() {}

}
