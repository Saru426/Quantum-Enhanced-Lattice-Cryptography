package main;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import security.FileEncryption;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class DecryptPane extends SecurityPane {

    public DecryptPane(Stage primaryStage) {
        super(primaryStage);
    }

    @Override
    protected void createDoButton() {
        doButton = new Button();
        doButton.setText("Decrypt...");
        doButton.setOnAction(event -> decryptFile());
    }

    private void decryptFile() {
        File file = new File(pathTextField.getText());
        if (!file.exists()) {
            showAlert("File does not exist.");
            return;
        }

        PasswordField keyField = new PasswordField();
        keyField.setPromptText("Enter decryption key");

        Alert keyDialog = new Alert(Alert.AlertType.NONE);
        keyDialog.getDialogPane().setContent(keyField);
        keyDialog.setTitle("Decryption Key");
        keyDialog.setHeaderText(null);
        keyDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String key = keyField.getText();
                if (!key.isEmpty()) {
                    try {
                        // Convert key to bytes using UTF-8 encoding
                        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
                        FileEncryption fileEncryption = new FileEncryption();
                        fileEncryption.decryptFile(file, keyBytes);
                    } catch (Exception e) {
                        showAlert("Error decrypting file: " + e.getMessage());
                    }
                } else {
                    showAlert("Please enter a valid decryption key.");
                }
            }
        });
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    protected void addExtensionFilters(ObservableList<FileChooser.ExtensionFilter> extensionFilters) {
        extensionFilters.add(new FileChooser.ExtensionFilter("Encrypted files (*.enc)", "*.enc"));
    }
}
