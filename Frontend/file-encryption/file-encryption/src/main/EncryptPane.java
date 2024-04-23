package main;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import security.FileEncryption;
import java.io.File;
import java.nio.charset.StandardCharsets;

public class EncryptPane extends SecurityPane {

    public EncryptPane(Stage primaryStage) {
        super(primaryStage);
    }

    @Override
    protected void createDoButton() {
        doButton = new Button();
        doButton.setText("Encrypt...");
        doButton.setOnAction(event -> encryptFile());
    }

    private void encryptFile() {
        File file = new File(pathTextField.getText());
        if (!file.exists()) {
            showAlert("File does not exist.");
            return;
        }

        PasswordField keyField = new PasswordField();
        keyField.setPromptText("Enter encryption key");

        Alert keyDialog = new Alert(Alert.AlertType.NONE);
        keyDialog.getDialogPane().setContent(keyField);
        keyDialog.setTitle("Encryption Key");
        keyDialog.setHeaderText(null);
        keyDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String key = keyField.getText();
                if (!key.isEmpty()) {
                    try {
                        // Convert key to bytes using UTF-8 encoding
                        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
                        FileEncryption fileEncryption = new FileEncryption();
                        fileEncryption.encryptFile(file, keyBytes);
                    } catch (Exception e) {
                        showAlert("Error encrypting file: " + e.getMessage());
                    }
                } else {
                    showAlert("Please enter a valid encryption key.");
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
}
