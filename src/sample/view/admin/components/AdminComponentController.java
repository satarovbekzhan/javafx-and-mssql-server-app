package sample.view.admin.components;

import javafx.collections.ObservableList;

public abstract class AdminComponentController {
    private ObservableList<Object> objectObservableList;

    protected ObservableList<Object> getObjectObservableList() {
        return objectObservableList;
    }

    public void setObjectObservableList(ObservableList<Object> objectObservableList) {
        this.objectObservableList = objectObservableList;
    }
}
