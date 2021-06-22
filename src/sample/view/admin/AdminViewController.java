package sample.view.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Main;
import sample.database.DB;
import sample.model.Category;
import sample.model.Product;
import sample.model.User;
import sample.view.Controller;
import sample.view.LoginViewController;
import sample.view.admin.components.AdminComponentController;
import sample.view.admin.components.UpdateCategoryViewController;
import sample.view.admin.components.UpdateProductViewController;
import sample.view.admin.components.UpdateUserViewController;

import java.io.IOException;

public class AdminViewController extends Controller {

    @FXML
    private ListView<Object> listView;
    @FXML
    private Button prevButton;
    @FXML
    private Button nextButton;
    @FXML
    private Button createButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;

    private ObservableList<Object> objectsList = FXCollections.observableArrayList();
    private Class currentWorkingClass = null;
    private int offset = 0;
    private int limit = 17;

    @FXML
    public void initialize() {
        listView.setItems(objectsList);

        prevButton.setDisable(true);
        nextButton.setDisable(true);
        createButton.setDisable(true);
        updateButton.setDisable(true);
        deleteButton.setDisable(true);

        listView.setOnMouseClicked(event -> {
            Object o = listView.getSelectionModel().getSelectedItem();
            if (o != null) {
                updateButton.setDisable(false);
                deleteButton.setDisable(false);
            } else {
                updateButton.setDisable(true);
                deleteButton.setDisable(true);
            }
        });

        nextButton.setOnAction(event -> {
            updateButton.setDisable(true);
            deleteButton.setDisable(true);
            offset = offset + limit;
            if (User.class.equals(currentWorkingClass))
                DB.userRepo.getUsers(offset, limit, result -> {
                    nextButton.setDisable(!DB.userRepo.hasItemsIn(offset + limit, 1));
                    prevButton.setDisable(false);
                    objectsList.clear();
                    objectsList.addAll(result);
                }, error -> new Alert(Alert.AlertType.ERROR, error, ButtonType.OK).showAndWait());
            else if (Category.class.equals(currentWorkingClass))
                DB.categoryRepo.getCategories(offset, limit, result -> {
                    nextButton.setDisable(!DB.categoryRepo.hasItemsIn(offset + limit, 1));
                    prevButton.setDisable(false);
                    objectsList.clear();
                    objectsList.addAll(result);
                }, error -> new Alert(Alert.AlertType.ERROR, error, ButtonType.OK).showAndWait());
            else if (Product.class.equals(currentWorkingClass))
                DB.productRepo.getProducts(offset, limit, result -> {
                    nextButton.setDisable(!DB.productRepo.hasItemsIn(offset + limit, 1));
                    prevButton.setDisable(false);
                    objectsList.clear();
                    objectsList.addAll(result);
                }, error -> new Alert(Alert.AlertType.ERROR, error, ButtonType.OK).showAndWait());
        });

        prevButton.setOnAction(event -> {
            updateButton.setDisable(true);
            deleteButton.setDisable(true);
            offset = offset - limit;
            if (offset == 0) prevButton.setDisable(true);
            if (User.class.equals(currentWorkingClass))
                DB.userRepo.getUsers(offset, limit, result -> {
                    nextButton.setDisable(false);
                    objectsList.clear();
                    objectsList.addAll(result);
                }, error -> new Alert(Alert.AlertType.ERROR, error, ButtonType.OK).showAndWait());
            else if (Category.class.equals(currentWorkingClass))
                DB.categoryRepo.getCategories(offset, limit, result -> {
                    nextButton.setDisable(false);
                    objectsList.clear();
                    objectsList.addAll(result);
                }, error -> new Alert(Alert.AlertType.ERROR, error, ButtonType.OK).showAndWait());
            else if (Product.class.equals(currentWorkingClass))
                DB.productRepo.getProducts(offset, limit, result -> {
                    nextButton.setDisable(false);
                    objectsList.clear();
                    objectsList.addAll(result);
                }, error -> new Alert(Alert.AlertType.ERROR, error, ButtonType.OK).showAndWait());
        });
    }

    private void loadElements() {
        prevButton.setDisable(true);
        nextButton.setDisable(true);
        updateButton.setDisable(true);
        deleteButton.setDisable(true);
        objectsList.clear();
        offset = 0;

        if (User.class.equals(currentWorkingClass))
            DB.userRepo.getUsers(offset, limit, result -> {
                nextButton.setDisable(!DB.userRepo.hasItemsIn(offset + limit, 1));
                objectsList.addAll(result);
                createButton.setDisable(false);
            }, error -> new Alert(Alert.AlertType.ERROR, error, ButtonType.OK).showAndWait());
        else if (Category.class.equals(currentWorkingClass))
            DB.categoryRepo.getCategories(offset, limit, result -> {
                nextButton.setDisable(!DB.categoryRepo.hasItemsIn(offset + limit, 1));
                objectsList.addAll(result);
                createButton.setDisable(false);
            }, error -> new Alert(Alert.AlertType.ERROR, error, ButtonType.OK).showAndWait());
        else if (Product.class.equals(currentWorkingClass))
            DB.productRepo.getProducts(offset, limit, result -> {
                nextButton.setDisable(!DB.productRepo.hasItemsIn(offset + limit, 1));
                objectsList.addAll(result);
                createButton.setDisable(false);
            }, error -> new Alert(Alert.AlertType.ERROR, error, ButtonType.OK).showAndWait());
    }

    @FXML
    public void loadUsers() {
        currentWorkingClass = User.class;
        loadElements();
    }

    @FXML
    public void loadCategories() {
        currentWorkingClass = Category.class;
        loadElements();
    }

    @FXML
    public void loadProducts() {
        currentWorkingClass = Product.class;
        loadElements();
    }

    @FXML
    public void logoutFromSystem() {
        try {
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

    @FXML
    public void createNew() {
        String fxmlPath = "";
        if (currentWorkingClass.equals(User.class)) fxmlPath = "CreateUserView.fxml";
        else if (currentWorkingClass.equals(Category.class)) fxmlPath = "CreateCategoryView.fxml";
        else if (currentWorkingClass.equals(Product.class)) fxmlPath = "CreateProductView.fxml";
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("components/" + fxmlPath));
            Parent root = loader.load();
            AdminComponentController acc = loader.getController();
            acc.setObjectObservableList(objectsList);
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(getStage());
            if (currentWorkingClass.equals(User.class))
                stage.setTitle("Erstellung eines neuen Benutzers");
            else if (currentWorkingClass.equals(Category.class))
                stage.setTitle("Erstellung einer neuen Kategorie");
            else if (currentWorkingClass.equals(Product.class))
                stage.setTitle("Erstellung eines neuen Produkts");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void updateOne() {
        Object o = listView.getSelectionModel().getSelectedItem();
        if (o == null) {
            new Alert(Alert.AlertType.WARNING, "Nichts ausgewählt! Versuchen Sie zuerst, etwas auszuwählen.",
                    ButtonType.OK).showAndWait();
            return;
        }
        String fxmlPath = "";
        if (o instanceof User) fxmlPath = "UpdateUserView.fxml";
        else if (o instanceof Category) fxmlPath = "UpdateCategoryView.fxml";
        else if (o instanceof Product) fxmlPath = "UpdateProductView.fxml";
        try {
            String windowTitle = "";
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("components/" + fxmlPath));
            Parent root = loader.load();
            AdminComponentController acc = loader.getController();
            acc.setObjectObservableList(objectsList);
            if (o instanceof User) {
                ((UpdateUserViewController) acc).setUser((User) o);
                windowTitle = "Änderung eines Benutzers mit der ID " + ((User) o).getId();
            } else if (o instanceof Category) {
                ((UpdateCategoryViewController) acc).setCategory((Category) o);
                windowTitle = "Änderung einer Kategorie mit der ID " + ((Category) o).getId();
            } else if (o instanceof Product) {
                ((UpdateProductViewController) acc).setProduct((Product) o);
                windowTitle = "Änderung eines Produkts mit der ID " + ((Product) o).getId();
            }
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(getStage());
            stage.setTitle(windowTitle);
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void deleteOne() {
        Object o = listView.getSelectionModel().getSelectedItem();
        if (o == null) {
            new Alert(Alert.AlertType.WARNING, "Nichts ausgewählt! Versuchen Sie zuerst, etwas auszuwählen.",
                    ButtonType.OK).showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        ButtonType okButton = new ButtonType("Entfernen", ButtonBar.ButtonData.YES);
        ButtonType noButton = new ButtonType("Abbrechen", ButtonBar.ButtonData.NO);
        alert.getButtonTypes().setAll(okButton, noButton);

        if (o instanceof User) {
            User u = (User) o;
            alert.setContentText("Sind Sie absolut sicher, dass Sie den Benutzer mit der ID " + u.getId() + " entfernen möchten?");
            alert.showAndWait().ifPresent(type -> {
                if (type.getButtonData() == ButtonBar.ButtonData.YES) {
                    DB.userRepo.deleteUser(u, result -> {
                        objectsList.remove(u);
                        updateButton.setDisable(true);
                        deleteButton.setDisable(true);
                        new Alert(Alert.AlertType.CONFIRMATION, "Entfernt Benutzer mit ID " + result.getId(), ButtonType.OK).showAndWait();
                            }, error -> new Alert(Alert.AlertType.ERROR, error, ButtonType.OK).showAndWait());
                } else alert.close();
            });
        } else if (o instanceof Category) {
            Category c = (Category) o;
            alert.setContentText("Sind Sie absolut sicher, dass Sie den Kategorie mit der ID " + c.getId() + " entfernen möchten?");
            alert.showAndWait().ifPresent(type -> {
                if (type.getButtonData() == ButtonBar.ButtonData.YES) {
                    DB.categoryRepo.deleteCategory(c, result -> {
                        objectsList.remove(c);
                        updateButton.setDisable(true);
                        deleteButton.setDisable(true);
                        new Alert(Alert.AlertType.CONFIRMATION, "Entfernt Kategorie mit ID " + result.getId(), ButtonType.OK).showAndWait();
                    }, error -> new Alert(Alert.AlertType.ERROR, error, ButtonType.OK).showAndWait());
                } else alert.close();
            });
        } else if (o instanceof Product) {
            Product p = (Product) o;
            alert.setContentText("Sind Sie absolut sicher, dass Sie den Produkt mit der ID " + p.getId() + " entfernen möchten?");
            alert.showAndWait().ifPresent(type -> {
                if (type.getButtonData() == ButtonBar.ButtonData.YES) {
                    DB.productRepo.deleteProduct(p, result -> {
                        objectsList.remove(p);
                        updateButton.setDisable(true);
                        deleteButton.setDisable(true);
                        new Alert(Alert.AlertType.CONFIRMATION, "Entfernt Produkt mit ID " + result.getId(), ButtonType.OK).showAndWait();
                    }, error -> new Alert(Alert.AlertType.ERROR, error, ButtonType.OK).showAndWait());
                } else alert.close();
            });
        }
    }
}
