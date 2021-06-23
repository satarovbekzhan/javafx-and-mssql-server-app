package sample.view;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.database.DB;

import java.io.IOException;
import java.sql.SQLException;

public class LoginViewController {
    @FXML private TextField emailTextField;
    @FXML private TextField passwordTextField;
    @FXML private Button authorizeButton;
    @FXML private Text infoText;

    private Stage app;
    private String authUserRole = "";

    @FXML
    private void initialize() {

//        emailTextField.setText("admin");
//        passwordTextField.setText("admin");
        emailTextField.setText("mila");
        passwordTextField.setText("111");

        info("Authorization required! Give your credentials to get an access.", null);
    }

    @FXML
    private void handleAuthorizeButtonClicked() {
        if (emailTextField.getText().isBlank()) {
            info("E-Mail is blank! Please, give your e-mail to proceed!", Color.ORANGE);
            return;
        } else if (passwordTextField.getText().isBlank()) {
            info("Password is blank! Please, give your password to proceed!", Color.ORANGE);
            return;
        }
        info("Loading!\nPlease, wait a moment.", null);
        setDisableControls(true);
        Timeline timelineLoading = new Timeline(
                new KeyFrame(Duration.seconds(0.33), ev -> authorizeButton.setText("·")),
                new KeyFrame(Duration.seconds(0.66), ev -> authorizeButton.setText("· ·")),
                new KeyFrame(Duration.seconds(0.99), ev -> authorizeButton.setText("· · ·"))
        );
        timelineLoading.setCycleCount(Animation.INDEFINITE);
        timelineLoading.play();
        Task<Boolean> task = new Task<>() {
            @Override
            public Boolean call() {
                try {
                    String message = authorize();
                    if (message.equals("")) return true;
                    else {
                        info(message, Color.RED);
                        return false;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        };
        task.setOnSucceeded(e -> {
            timelineLoading.stop();
            setDisableControls(false);
            authorizeButton.setText("Authorize");
            if (task.getValue()) proceed();
        });
        new Thread(task).start();
    }

    private String authorize() throws InterruptedException {
        try {
            DB.initializeDatabaseModels(emailTextField.getText(), passwordTextField.getText());
            authUserRole = DB.testRepo.getUserRole();
            if (!(authUserRole.equals("ADMIN") || authUserRole.equals("STAFF") || authUserRole.equals("BUYER")))
                return "You do not have any role to access the system!";
        } catch (SQLException throwable) {
            return throwable.getLocalizedMessage();
        }
        return "";
    }

    private void proceed() {
        String pathToView;
        switch (authUserRole) {
            case "ADMIN":
                pathToView = "admin/AdminView.fxml";
                break;
            case "STAFF":
                pathToView = "staff/StaffView.fxml";
                break;
            case "BUYER":
                pathToView = "buyer/BuyerView.fxml";
                break;
            default:
                pathToView = "LoginView.fxml";
        }

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource(pathToView));
            Parent root = loader.load();
            Controller controller = loader.getController();
            controller.setStage(app);
            this.app.setTitle("MAKK | " + authUserRole + " | " + emailTextField.getText());
            this.app.setScene(new Scene(root));
            this.app.show();
        } catch (IOException e) {
            info(e.getMessage(), Color.RED);
            e.printStackTrace();
        }
    }

    private void setDisableControls(boolean disable) {
        passwordTextField.setDisable(disable);
        authorizeButton.setDisable(disable);
        emailTextField.setDisable(disable);
    }

    private void info(String text, Color color) {
        if (color == null) infoText.setFill(Color.BLACK);
        else infoText.setFill(color);
        infoText.setText(text);
    }

    public void setApp(Stage app) {
        this.app = app;
    }
}
