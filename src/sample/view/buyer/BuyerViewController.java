package sample.view.buyer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.WindowEvent;
import sample.database.DB;
import sample.model.Category;
import sample.view.Controller;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class BuyerViewController extends Controller {
    @FXML private ListView<Category> categoryListView;

    private final ObservableList<Category> categories = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        categoryListView.setItems(categories);
        categoryListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Category category, boolean empty) {
                super.updateItem(category, empty);
                if (empty || category == null) {
                    setText(null);
                } else {
                    setText(category.getName());
                }
            }
        });

        afterInitialized();
    }

    private void afterInitialized() {
        loadCategories();
    }

    private void loadCategories() {
        Task<Boolean> task = new Task<>() {
            @Override
            public Boolean call() {
                AtomicBoolean noError = new AtomicBoolean(true);
                Optional<List<Category>> optionalList = DB.categoryRepo.getAllCategories();
                optionalList.ifPresentOrElse(categories::addAll, () -> noError.set(false));
                return noError.get();
            }
        };
//        task.setOnSucceeded(e -> {
//            if (task.getValue()) {
//
//            }
//        });
        new Thread(task).start();
    }
}
