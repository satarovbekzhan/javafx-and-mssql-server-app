package sample.view.admin.components;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.util.StringConverter;
import sample.database.DB;
import sample.model.*;

import java.math.BigDecimal;
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
    private VBox compositionVBox;

    @FXML
    private ChoiceBox<Category> categoryChoiceBox;
    @FXML
    private VBox categoryVBox;

    private List<Composition> productCompositions;
    private List<Category> productCategories;

    @FXML
    public void initialize() {
        productCompositions = new ArrayList<>();
        productCategories = new ArrayList<>();

        nutrientChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Nutrient object) {
                return object.getName();
            }

            @Override
            public Nutrient fromString(String string) {
                return null;
            }
        });
        unitChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Unit object) {
                return object.getName();
            }

            @Override
            public Unit fromString(String string) {
                return null;
            }
        });
        categoryChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Category object) {
                return object.getName();
            }

            @Override
            public Category fromString(String string) {
                return null;
            }
        });

        DB.compositionRepo.getAllNutrient(result -> {
            for (Nutrient n:result) {
                nutrientChoiceBox.getItems().add(n);
            }
        }, System.out::println);
        DB.compositionRepo.getAllUnits(result -> {
            for (Unit u:result) {
                unitChoiceBox.getItems().add(u);
            }
        }, System.out::println);
        DB.categoryRepo.getAllCategories(result -> {
            for (Category c:result) {
                categoryChoiceBox.getItems().add(c);
            }
        }, System.out::println);
    }

    @FXML
    public void addCompositionToProduct() {
        Nutrient nutrient = nutrientChoiceBox.getSelectionModel().getSelectedItem();
        Unit unit = unitChoiceBox.getSelectionModel().getSelectedItem();
        String proHun = proHunTextField.getText();
        String proPor = proPorTextField.getText();
        if (nutrient == null) {
            new Alert(Alert.AlertType.WARNING, "Kein Nährstoff ausgewählt!", ButtonType.OK).showAndWait();
            return;
        }
        if (unit == null) {
            new Alert(Alert.AlertType.WARNING, "Keine Einheit für Nährstoff gewählt!", ButtonType.OK).showAndWait();
            return;
        }
        if (proHun.isBlank() || proPor.isBlank()) {
            new Alert(Alert.AlertType.WARNING, "Einige Eingaben sind leer!", ButtonType.OK).showAndWait();
            return;
        }
        try {
            double ph = Double.parseDouble(proHun);
            double pp = Double.parseDouble(proPor);
            Composition composition = new Composition(0, nutrient, unit, new BigDecimal(ph), new BigDecimal(pp));
            productCompositions.add(composition);
            renderCompositions();
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "Eingabewerte sind ungültig!", ButtonType.OK).showAndWait();
        }
    }

    @FXML
    public void addProductToCategory() {
        Category category = categoryChoiceBox.getSelectionModel().getSelectedItem();
        if (category == null) {
            new Alert(Alert.AlertType.WARNING, "Keine Kategorie ausgewählt!", ButtonType.OK).showAndWait();
            return;
        }
        productCategories.add(category);
        renderCategories();
    }

    @FXML
    public void createProduct() {
        String title = productTitleTextField.getText();
        String picture = productPictureTextField.getText();
        String price = productPriceTextField.getText();
        String description = productDescriptionTextArea.getText();
        if (title.isBlank() || picture.isBlank() || price.isBlank() || description.isBlank()) {
            new Alert(Alert.AlertType.WARNING, "Einige Eingaben sind leer!", ButtonType.OK).showAndWait();
            return;
        }
        try {
            Double pr = Double.valueOf(price);
            Product product = new Product(0, title, description, picture, "MAKK\"BURGERS\"TM");
//            create product
//            add new price
//            add compositions
//            add categories
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.WARNING, "Eingabewerte sind ungültig!", ButtonType.OK).showAndWait();
        }
    }

    private void renderCompositions() {
        compositionVBox.getChildren().clear();
        for (Composition c:productCompositions) {
            Label nuLabel = new Label(c.getNutrient().getName());
            nuLabel.setTextAlignment(TextAlignment.CENTER);
            nuLabel.setPrefWidth(136);
            Label unLabel = new Label(c.getUnit().getName());
            unLabel.setTextAlignment(TextAlignment.CENTER);
            unLabel.setPrefWidth(42);
            Label phLabel = new Label(c.getPro_100().toString());
            phLabel.setTextAlignment(TextAlignment.CENTER);
            phLabel.setPrefWidth(55);
            Label ppLabel = new Label(c.getPro_por().toString());
            ppLabel.setTextAlignment(TextAlignment.CENTER);
            ppLabel.setPrefWidth(55);
            Button remButton = new Button("-");
            remButton.setPrefWidth(29);
            remButton.setOnAction(event -> {
                productCompositions.remove(c);
                renderCompositions();
            });
            HBox hBox = new HBox(nuLabel, unLabel, phLabel, ppLabel, remButton);
            hBox.setSpacing(20);
            compositionVBox.getChildren().add(hBox);
        }
    }

    private void renderCategories() {
        categoryVBox.getChildren().clear();
        for (Category c:productCategories) {
            Label caLabel = new Label(c.getName());
            caLabel.setTextAlignment(TextAlignment.CENTER);
            caLabel.setPrefWidth(368);
            Button remButton = new Button("-");
            remButton.setPrefWidth(29);
            remButton.setOnAction(event -> {
                productCategories.remove(c);
                renderCategories();
            });
            HBox hBox = new HBox(caLabel, remButton);
            hBox.setSpacing(20);
            categoryVBox.getChildren().add(hBox);
        }
    }
}
