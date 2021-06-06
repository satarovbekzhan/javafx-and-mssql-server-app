package sample.view;

import javafx.stage.Stage;

public abstract class Controller {
    private Stage stage;

    protected Stage getStage() {
        return stage;
    }

    protected void setStage(Stage stage) {
        this.stage = stage;
    }
}
