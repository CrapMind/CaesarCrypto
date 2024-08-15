package service;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import util.*;

import java.io.*;
import java.util.Objects;

public class CryptoManager {
    private final FileService fileService = new FileService(); // Handles file operations
    private final CryptoService cryptoService = new CryptoService(); // Handles cryptographic operations

    // Creates the layout for the action window based on the selected action (Encrypt, Decrypt, Brute Force)
    public VBox createActionVBox(Action action, Label fileToActionLabel, Label fileToUploadLabel, Button browseActionFileBtn, Button browseUploadFileBtn, Button proceedButton, Label errorLabel, Stage actionStage) {

        Label keyLabel = new Label(Note.ENTER_KEY.tag() + (Cryptographer.getAlphabetLength() - 1));
        TextField keyField = createNumericField(); // TextField for key input

        switch (action) {
            case ENCRYPT -> {
                Button generateKeyButton = new Button("Generate Key"); // Button to generate a random key

                generateKeyButton.setOnAction(_ -> {  // Set the generated key in the keyField
                    int key = (int) (Math.random() * 87) + 1;
                    keyField.setText(String.valueOf(key));
                });

                handleKeyEntry(action, proceedButton, errorLabel, actionStage, keyField); // Handle key entry and action execution

                return new VBox(10, fileToActionLabel, browseActionFileBtn, fileToUploadLabel, browseUploadFileBtn,
                        keyLabel, keyField, proceedButton, generateKeyButton, errorLabel); // Return the layout with key generation button
            }
            case DECRYPT -> {
                handleKeyEntry(action, proceedButton, errorLabel, actionStage, keyField);  // Handle key entry and action execution

                return new VBox(10, fileToActionLabel, browseActionFileBtn, fileToUploadLabel, browseUploadFileBtn,
                        keyLabel, keyField, proceedButton, errorLabel); // Return the layout for decryption
            }
            default -> {
                proceedButton.setOnAction(_ -> {
                    if (fileService.getFileToLoad() != null && fileService.getFileToUpload() != null) {
                        if (!fileService.filesAreIdentical()) {
                            cryptoService.bruteForce(fileService.getFileToLoad(), fileService.getFileToUpload()); // Perform brute force decryption
                            showSuccessMessage(actionStage, action); // Show success message
                            fileService.resetFiles(); // Reset the files after operation
                        } else errorLabel.setText("You have selected the same file. \n" + Note.INVALID_FILE.tag());
                    } else errorLabel.setText(Note.INVALID_FILE.tag());
                });
                return new VBox(10, fileToActionLabel, browseActionFileBtn, fileToUploadLabel,
                        browseUploadFileBtn, proceedButton, errorLabel); // Return the layout for brute force
            }
        }
    }
    // Creates the layout for the success message window
    public VBox createSucessVBox(Action action, Stage successStage) {
        Label successLabel = new Label("The file was processed successfully!");
        Button okButton = new Button("Proceed");
        okButton.setOnAction(_ -> successStage.close()); // Close the success window on button click

        VBox vBox;
        if (Objects.requireNonNull(action) == Action.BRUTEFORCE) {
            Label keyLabel;
            keyLabel = new Label("Key: " + cryptoService.getBruteForceKey()); // Display the found key after brute force
            vBox = new VBox(10, successLabel, keyLabel, okButton); // Include the key label in the layout
        } else {
            vBox = new VBox(10, successLabel, okButton); // Simple success message layout
        }
        return vBox;
    }
    // Handles the key entry and performs the selected action (Encrypt/Decrypt)
    private void handleKeyEntry(Action action, Button proceedButton, Label errorLabel, Stage actionStage, TextField keyField) {

        proceedButton.setOnAction(_ -> {
            if (!(fileService.filesAreIdentical())) {
                try {
                    int key = Integer.parseInt(keyField.getText());
                    if (key > 0 && key < 88) {
                        cryptoService.performAction(fileService.getFileToLoad(), fileService.getFileToUpload(), action, key); // Perform encryption/decryption
                        showSuccessMessage(actionStage, action); // Show success message
                        fileService.resetFiles(); // Reset the files after operation
                    } else errorLabel.setText(Note.INVALID_KEY.tag());
                } catch (NumberFormatException ex) {
                    errorLabel.setText(Note.INVALID_KEY.tag());
                } catch (NullPointerException ex) {
                    errorLabel.setText(Note.INVALID_FILE.tag());
                }
            } else errorLabel.setText("You have selected the same file." + Note.INVALID_FILE.tag());
        });
    }
    // Handles file selection and validation
    public void handleFileSelection(Label fileLabel, Label errorLabel) {
        String tag = fileLabel.getText();
        try {
            File file = fileService.chooseUserFile(tag); // Open file chooser
            File validFile = fileService.getValidFile(file); // Validate the selected file
            if (tag.equals(Note.SELECT_ACTION_FILE.tag())) {
                fileService.setFileToLoad(null);
                fileService.setFileToLoad(validFile); // Set the selected file as the input file
            } else {
                fileService.setFileToUpload(null);
                fileService.setFileToUpload(validFile); // Set the selected file as the output file
            }
            fileLabel.setText(validFile.getAbsolutePath()); // Display the selected file path
        } catch (NullPointerException ex) {
            errorLabel.setText(Note.INVALID_FILE.tag());
        }
    }
    // Creates a TextField that only accepts numeric input
    private TextField createNumericField() {
        TextField textField = new TextField();

        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.length() > 3 || !newText.matches("\\d*")) {
                return null; // Limit the input to numbers with a maximum length of 3
            }
            return change;
        });

        textField.setTextFormatter(textFormatter);

        return textField;
    }
    // Displays the success message after an action is completed
    private void showSuccessMessage(Stage actionStage, Action action) {

        actionStage.close(); // Close the action window

        Stage successStage = new Stage();
        successStage.initModality(Modality.APPLICATION_MODAL);
        successStage.setTitle("Success");
        successStage.setResizable(false);

        VBox layout = createSucessVBox(action, successStage);
        layout.getStyleClass().add("success-window");

        Scene scene = new Scene(layout, 300, 200);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/actionWindow.css")).toExternalForm());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/buttons.css")).toExternalForm());
        successStage.setScene(scene);
        successStage.showAndWait(); // Display the success message window and wait for it to close
        cryptoService.setBruteForceKey(0); // Reset the brute force key after displaying the message
    }
}
