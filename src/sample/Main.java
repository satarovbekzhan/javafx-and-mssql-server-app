package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.database.DB;
import sample.database.UserDB;

import java.util.Objects;

public class Main extends Application {

//    public static DB db;
    public static UserDB userDB;

    @Override
    public void start(Stage primaryStage) throws Exception {
        initializeDatabaseModels();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("LoginView.fxml")));
        primaryStage.setTitle("MAKK");
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private static void initializeDatabaseModels() {
        DB db = new DB(
                "127.0.0.1",
                "1433",
                "makk_user",
                "123",
                "makk");
        userDB = new UserDB(db);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
