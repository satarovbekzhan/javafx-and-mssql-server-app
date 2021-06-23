package sample.view.admin.components;

import javafx.collections.ObservableList;
import javafx.stage.Stage;

public abstract class AdminComponentController {
    private ObservableList<Object> objectObservableList;
    private Stage stage;

    protected ObservableList<Object> getObjectObservableList() {
        return objectObservableList;
    }

    public void setObjectObservableList(ObservableList<Object> objectObservableList) {
        this.objectObservableList = objectObservableList;
    }

    protected Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
