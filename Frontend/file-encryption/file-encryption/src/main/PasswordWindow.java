package main;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import security.EncryptionProgress;
import security.FileEncryption;
import security.FileEncryptionException;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by lbsilva on 02-Nov-17.
 */
public class PasswordWindow extends Stage {

    private final static String ALLOWED_SYMBOLS = "|\\!\"@#£$§%€&/{([)]=}?'»«*+ªº_-:.;,><";

    public final static int GENERAL_SPACING = 30;
    public final static int GENERAL_PADDING = 30;

    private PasswordField passwordTextField;
    private Button okButton;

    private String path;
    private byte mode;

    public PasswordWindow(String path, byte mode) {
        super();
        this.path = path;
        this.mode = mode;
        getIcons().add(new Image(getClass().getResourceAsStream(".." + File.separator + "images" + File.separator + "logo.png")));
        createPasswordTextField();
        createOkButton();
        createWindow();
        show();
    }

    private void createPasswordTextField() {
        passwordTextField = new PasswordField();
        passwordTextField.setPromptText("Password");
        passwordTextField.setAlignment(Pos.CENTER);
        passwordTextField.setMaxWidth(150);
    }


    private static boolean isPasswordValid(String password) {
        if (password.length() > 16 || password.length() < 3) {
            return false;
        }
        for (int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if (!(Character.isAlphabetic(ch) || Character.isDigit(ch) || ALLOWED_SYMBOLS.contains(String.valueOf(ch)))) {
                return false;
            }
        }
        return true;
    }

    private void createOkButton() {
        okButton = new Button(mode == FileEncryption.ENCRYPT_MODE ? "Encrypt" : "Decrypt");
        okButton.setOnAction(event -> {
            String password = passwordTextField.getText();
            if (!isPasswordValid(password)) {
                close();
                new AlertWindow("Invalid password", "Please make sure your password length is 16 or less.\nOnly numbers, words, and special characters are allowed.");
            } else {
                try {
                    if (mode == FileEncryption.ENCRYPT_MODE) {
                        FileEncryption.encryptFile(new File(path), password.getBytes());
                    } else {
                        FileEncryption.decryptFile(new File(path), password.getBytes());
                    }
                    close();
                    new EncryptionProgress(new File(path), password.getBytes()).start();
                } catch (IOException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException ex) {
                    new AlertWindow("Error", "An error occurred: " + ex.getMessage());
                }
            }
        });
    }


    private void createWindow() {
        VBox root = new VBox();
        Label label = new Label("Please enter your password\nMax: 16 chars");
        root.getChildren().addAll(label, passwordTextField, okButton);
        root.setSpacing(GENERAL_SPACING);
        root.setPadding(new Insets(GENERAL_PADDING));
        root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource(".." + File.separator + "styles" + File.separator + "Style.css").toExternalForm());
        setScene(scene);
        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);
        setTitle("FileEncryption - " + (mode == FileEncryption.ENCRYPT_MODE ? "Encrypt" : "Decrypt"));
        show();
    }
}
