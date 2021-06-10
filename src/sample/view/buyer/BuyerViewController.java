package sample.view.buyer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sample.database.DB;
import sample.model.Category;
import sample.model.Product;
import sample.view.Controller;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BuyerViewController extends Controller {
    @FXML private ListView<Object> elementsListView;
    @FXML private ImageView productImageView;

    private ArrayList<Category> categories;
    private HashMap<Integer, List<Product>> productsByCategory;
    private Product selectedProduct;
    private final ObservableList<Object> listItems = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        categories = new ArrayList<>();
        productsByCategory = new HashMap<>();
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
            disOrEnableElementsListView();
        } else {
            selectedProduct = product;
            // show product details
            try {
                Image img = new Image(selectedProduct.getPicture());
                productImageView.setImage(img);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getCause().getMessage());
            }
            System.out.println(product.getTitle());
            disOrEnableElementsListView();
        }
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
    public void goToCart() {
        DB.testRepo.getLoginData(System.out::println, System.out::println);
    }

    @FXML
    public void goToPayment() {
        DB.testRepo.getLoginData(System.out::println, System.out::println);
    }
}
