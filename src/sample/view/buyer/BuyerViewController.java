package sample.view.buyer;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import sample.Main;
import sample.database.DB;
import sample.model.Category;
import sample.model.Product;
import sample.view.Controller;
import sample.view.LoginViewController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class BuyerViewController extends Controller {
    @FXML private ListView<Object> elementsListView;
    @FXML private Pane productDetailsPane;

    @FXML private Text titleText;
    @FXML private ImageView productImageView;
    @FXML private Text descriptionText;
    @FXML private Text ingredientsText;
    @FXML private VBox nutrientsVBox;

    @FXML private TextField orderAmountField;
    @FXML private Label productPriceLabel;

    private ArrayList<Category> categories;
    private HashMap<Integer, List<Product>> productsByCategory;
    private Product selectedProduct;
    private final ObservableList<Object> listItems = FXCollections.observableArrayList();
    private HashMap<Integer, Integer> orderItems;

    @FXML
    private void initialize() {
        Application.setUserAgentStylesheet(Objects.requireNonNull(getClass().getResource("main.css")).toExternalForm());
        orderAmountField.setText("0");
        productDetailsPane.setVisible(false);
        categories = new ArrayList<>();
        productsByCategory = new HashMap<>();
        orderItems = new HashMap<>();
        elementsListView.setItems(listItems);
        elementsListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Object element, boolean empty) {
                super.updateItem(element, empty);
                if (empty || element == null) setText(null);
                else {
                    if (element instanceof Category) setText(((Category) element).getName());
                    else {
                        if (((Product) element).getId() == -1) setText("<<<");
                        else setText(((Product) element).getTitle());
                    }
                }
            }
        });
        elementsListView.setOnMouseClicked(event -> {
            Object o = elementsListView.getSelectionModel().getSelectedItem();
            if (o != null) {
                if (o instanceof Category) {
                    changeElementsListViewToProducts(((Category) o).getId());
                } else {
                    handleOnProductClicked(((Product) o));
                }
            }
        });
        disOrEnableElementsListView();
        fetchCategoriesAndSetToList();
    }

    private void fetchCategoriesAndSetToList() {
        DB.categoryRepo.getAllCategories(result -> {
            categories.addAll(result);
            listItems.clear();
            listItems.addAll(categories);
            disOrEnableElementsListView();
        }, System.out::println);
    }

    private void handleOnProductClicked(Product product) {
        if (product == selectedProduct) return;
        disOrEnableElementsListView();
        if (product.getId() == -1) {
            selectedProduct = null;
            listItems.clear();
            listItems.addAll(categories);
        } else {
            productDetailsPane.setVisible(true);
            selectedProduct = product;
            // show product details
            nutrientsVBox.getChildren().clear();
            inflateProductNutrients(selectedProduct.getId());
            titleText.setText(selectedProduct.getTitle());
            try {
                Image img = new Image(selectedProduct.getPicture());
                productImageView.setImage(img);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getCause().getMessage());
            }
            String description = selectedProduct.getDescription();
            if (description.length() > 365) descriptionText.setText(description.substring(0, 365) + "...");
            else descriptionText.setText(description);
//            ingredientsText.setText(selectedProduct.getIngredients());
            ingredientsText.setText("MAKK\"BURGERS\"TM");
            updateOrderAmountValueField();
            DB.productRepo.getNewestPriceByProduct(product,
                    result -> productPriceLabel.setText(result.getValue() + " €"),
                    error -> productPriceLabel.setText("-.--" + " €"));
        }
        disOrEnableElementsListView();
    }

    private void inflateProductNutrients(Integer productId) {
        DB.compositionRepo.getCompositionsByProductId(productId, result -> {
            addRowToNutrients("Nährstoffname", "Pro 100g", "Pro Portion");
            result.forEach(composition -> {
                addRowToNutrients(composition.getNutrient().getName() + " " + composition.getUnit().getName(),
                        composition.getPro_100().toString(), composition.getPro_por().toString());
            });
        }, System.out::println);
    }

    private void addRowToNutrients(String nutrient, String pro_100, String pro_por) {
        Text nutrientText = new Text();
        nutrientText.setWrappingWidth(206);
        nutrientText.setText(nutrient);
        Text pro_100Text = new Text();
        pro_100Text.setWrappingWidth(110);
        pro_100Text.setTextAlignment(TextAlignment.CENTER);
        pro_100Text.setText(pro_100);
        Text pro_porText = new Text();
        pro_porText.setTextAlignment(TextAlignment.CENTER);
        pro_porText.setWrappingWidth(110);
        pro_porText.setText(pro_por);
        if (nutrient.equals("Nährstoffname")) {
            nutrientText.setStyle("-fx-font-weight: bold");
            pro_100Text.setStyle("-fx-font-weight: bold");
            pro_porText.setStyle("-fx-font-weight: bold");
        }
        HBox row = new HBox();
        row.getChildren().add(nutrientText);
        row.getChildren().add(pro_100Text);
        row.getChildren().add(pro_porText);
        nutrientsVBox.getChildren().add(row);
    }

    private void changeElementsListViewToProducts(Integer categoryId) {
        disOrEnableElementsListView();
        listItems.clear();
        listItems.add(new Product(-1, "", "", "", ""));
        if (productsByCategory.containsKey(categoryId)) {
            listItems.addAll(productsByCategory.get(categoryId));
            disOrEnableElementsListView();
        } else {
            DB.productRepo.getProductsByCategoryId(categoryId, result -> {
                productsByCategory.put(categoryId, result);
                listItems.addAll(productsByCategory.get(categoryId));
                disOrEnableElementsListView();
            }, error -> {
                System.out.println(error);
                disOrEnableElementsListView();
            });
        }
    }

    private void disOrEnableElementsListView() {
        this.elementsListView.setDisable(!this.elementsListView.isDisabled());
    }

    @FXML
    public void addProductToCart() {
        if (selectedProduct == null) return;
        Integer productId = selectedProduct.getId();
        if (orderItems.containsKey(productId)) {
            if (orderItems.get(productId) < 100) {
                orderItems.put(productId, orderItems.get(productId) + 1);
                updateOrderAmountValueField();
            }
        } else {
            orderItems.put(productId, 1);
            updateOrderAmountValueField();
        }
    }

    @FXML
    public void removeProductFromCart() {
        if (selectedProduct == null) return;
        Integer productId = selectedProduct.getId();
        if (orderItems.containsKey(productId)) {
            if (orderItems.get(productId) != 0) {
                orderItems.put(productId, orderItems.get(productId) - 1);
                updateOrderAmountValueField();
            }
        }
    }

    private void updateOrderAmountValueField() {
        if (selectedProduct != null) {
            if (orderItems.containsKey(selectedProduct.getId()))
                orderAmountField.setText(String.valueOf(orderItems.get(selectedProduct.getId())));
            else orderAmountField.setText("0");
        }
    }

    @FXML
    public void goToCart() {
        //
    }

    @FXML
    public void goToPayment() {
        //
    }

    @FXML
    public void logoutFromSystem() {
        try {
            Application.setUserAgentStylesheet(Application.STYLESHEET_MODENA);
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/LoginView.fxml"));
            Parent root = loader.load();
            LoginViewController lvc = loader.getController();
            lvc.setApp(getStage());
            getStage().setTitle("MAKK");
            getStage().setScene(new Scene(root));
            getStage().setResizable(false);
            getStage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
