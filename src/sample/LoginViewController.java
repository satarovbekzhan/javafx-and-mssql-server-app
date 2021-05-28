package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import sample.model.User;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class LoginViewController {
    @FXML private Text infoText;
    @FXML private TextField emailTextField;
    @FXML private TextField passwordTextField;
    @FXML private Button authorizeButton;

    @FXML
    private void initialize() {
        info("Authorization required!\nGive your credentials to get an access.", null);
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
                new KeyFrame(Duration.seconds(0.33), ev -> authorizeButton.setText("*")),
                new KeyFrame(Duration.seconds(0.66), ev -> authorizeButton.setText("* *")),
                new KeyFrame(Duration.seconds(0.99), ev -> authorizeButton.setText("* * *"))
        );
        timelineLoading.setCycleCount(Animation.INDEFINITE);
        timelineLoading.play();
        Task<Boolean> task = new Task<>() {
            @Override
            public Boolean call() {
                try {
                    // check for credentials
                    TimeUnit.SECONDS.sleep(3);
                    // set info text
                    return true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        };
        task.setOnSucceeded(e -> {
            if (task.getValue()) {
                proceedToHomePage();
            } else {
                timelineLoading.stop();
                setDisableControls(false);
                authorizeButton.setText("Authorize");
            }
        });
        new Thread(task).start();
    }

    private void proceedToHomePage() {
        Optional<List<User>> users = Main.userDB.getAllUsers();

        users.ifPresentOrElse(list -> {
            list.forEach(System.out::println);
        }, () -> System.out.println("No content"));
    }


    private void setDisableControls(boolean disable) {
        emailTextField.setDisable(disable);
        passwordTextField.setDisable(disable);
        authorizeButton.setDisable(disable);
    }

    private void info(String text, Color color) {
        if (color == null) infoText.setFill(Color.BLACK);
        else infoText.setFill(color);
        infoText.setText(text);
    }
}
