package app;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import service.CryptoManager;
import util.*;
import java.util.Objects;

public class App extends Application {
    private final CryptoManager cryptoManager = new CryptoManager(); // Manages cryptographic actions

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Caesar Crypto"); // Set the title of the application window

        // Load and set the application icon
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/icon.png")));
        primaryStage.getIcons().add(image);

        // Create and style the main title label
        Label title = new Label("CAESAR CRYPTO");
        title.getStyleClass().add("label");

        VBox titleBox = new VBox(title);
        titleBox.getStyleClass().add("border-label");

        // Create buttons for different actions
        Button btnEncrypt = new Button("Cipher");
        Button btnDecrypt = new Button("Decrypt");
        Button btnBruteForce = new Button("Brute Force");
        Button btnExit = new Button("Exit");

        // Add hover animations to buttons
        addAnimation(btnEncrypt);
        addAnimation(btnDecrypt);
        addAnimation(btnBruteForce);
        addAnimation(btnExit);
        // Set button actions with sound effects

        AudioClip cipherAudio = new AudioClip((Objects.requireNonNull(getClass().getResource("/buttons/audio/cipher.mp3")).toExternalForm()));
        AudioClip decryptAudio = new AudioClip((Objects.requireNonNull(getClass().getResource("/buttons/audio/decrypt.mp3")).toExternalForm()));
        AudioClip bruteForceAudio = new AudioClip((Objects.requireNonNull(getClass().getResource("/buttons/audio/brute_force.mp3")).toExternalForm()));
        AudioClip exitAudio = new AudioClip((Objects.requireNonNull(getClass().getResource("/buttons/audio/exit.mp3")).toExternalForm()));

        btnEncrypt.setOnAction(_ -> {
            cipherAudio.play();
            openActionWindow(Action.ENCRYPT);
        });
        btnDecrypt.setOnAction(_ -> {
            decryptAudio.play();
            openActionWindow(Action.DECRYPT);
        });
        btnBruteForce.setOnAction(_ -> {
            bruteForceAudio.play();
            openActionWindow(Action.BRUTEFORCE);
        });
        btnExit.setOnAction(_ -> {
            exitAudio.play();
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.exit(0);
        });

        // Arrange the layout of buttons and title
        VBox vbox = new VBox(20, titleBox, btnEncrypt, btnDecrypt, btnBruteForce, btnExit);
        vbox.setStyle("-fx-alignment: center;");

        // Set the scene and apply stylesheets
        Scene scene = new Scene(vbox, 800, 800);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/buttons.css")).toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show(); // Display the main window
    }
    // Adds a hover animation to the button text
    private void addAnimation(Button button) {
        final String originalText = button.getText();
        final int textLength = originalText.length();
        final Timeline timeline = new Timeline();

        for (int i = 0; i < textLength; i++) {
            final int index = i;
            timeline.getKeyFrames().add(new KeyFrame(
                    Duration.seconds(i * 0.2),
                    _ -> {
                        StringBuilder text = new StringBuilder(originalText);
                        text.setCharAt(index, '%');
                        button.setText(text.toString());
                    }
            ));
        }

        timeline.getKeyFrames().add(new KeyFrame(
                Duration.seconds(textLength * 0.2),
                _ -> button.setText(originalText)
        ));

        timeline.setCycleCount(Timeline.INDEFINITE);
        button.setOnMouseEntered(_ -> timeline.play());
        button.setOnMouseExited(_ -> {
            timeline.stop();
            button.setText(originalText);
        });
    }
    // Opens a new window for the selected action (Encrypt, Decrypt, Brute Force)
    private void openActionWindow(Action action) {
        Stage actionStage = new Stage();

        actionStage.initModality(Modality.APPLICATION_MODAL);
        actionStage.setTitle("Action");
        actionStage.setResizable(false);

        Label fileToActionLabel = new Label(Note.SELECT_ACTION_FILE.tag());
        Label fileToUploadLabel = new Label(Note.SELECT_UPLOAD_FILE.tag());

        Button actionFileBtn = new Button("Browse:");
        Button uploadFileBtn = new Button("Browse:");
        Button proceedButton = new Button("Proceed");

        Label errorLabel = new Label();
        errorLabel.getStyleClass().add("error-label");

        // Set up file selection buttons
        actionFileBtn.setOnAction(_ -> cryptoManager.handleFileSelection(fileToActionLabel, errorLabel));
        uploadFileBtn.setOnAction(_ -> cryptoManager.handleFileSelection(fileToUploadLabel, errorLabel));

        // Create and style the layout for the action window
        VBox vBox = cryptoManager.createActionVBox(action, fileToActionLabel, fileToUploadLabel, actionFileBtn, uploadFileBtn, proceedButton, errorLabel, actionStage);

        vBox.getStyleClass().add("cipher-window");
        vBox.setStyle("-fx-alignment: center;");

        // Set the scene and apply stylesheets
        Scene scene = new Scene(vBox, 450, 450);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/actionWindow.css")).toExternalForm());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/buttons.css")).toExternalForm());
        actionStage.setScene(scene);
        actionStage.showAndWait(); // Display the action window and wait for it to close
    }

    // Launch the JavaFX application
    public static void main(String[] args) {
        launch(args);
    }
}
