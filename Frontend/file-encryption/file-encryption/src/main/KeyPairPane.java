package main;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KeyPairPane extends VBox {

    private static final Logger logger = Logger.getLogger(KeyPairPane.class.getName());

    public KeyPairPane(Stage primaryStage) {
        super(10); // Vertical spacing between nodes

        try {
            // Generate key pair
            KeyPairGenerator keyPairGenerator = new KeyPairGenerator();
            keyPairGenerator.generateKeyPair();

            // Labels to display the keys
            Label publicKeyLabel = new Label("Public Key: " + keyPairGenerator.getPublicKey());
            Label privateKeyLabel = new Label("Private Key: " + keyPairGenerator.getPrivateKey());

            // Button to close the window
            Button closeButton = new Button("Close");
            closeButton.setOnAction(event -> primaryStage.close());

            // Add nodes to the pane
            getChildren().addAll(publicKeyLabel, privateKeyLabel, closeButton);
            setPadding(new Insets(10));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "An error occurred while generating key pair", e);
            // Handle exception appropriately
        }
    }
}
