package security;

import java.security.*;

public class KeyPairGenerator {
    private final java.security.KeyPairGenerator keyPairGenerator;
    private KeyPair keyPair;

    public KeyPairGenerator() throws NoSuchAlgorithmException {
        keyPairGenerator = java.security.KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048); // You can adjust the key size as needed
    }

    public void generateKeyPair() {
        keyPair = keyPairGenerator.generateKeyPair();
    }

    public String getPublicKey() {
        PublicKey publicKey = keyPair.getPublic();
        return bytesToHex(publicKey.getEncoded());
    }

    public String getPrivateKey() {
        PrivateKey privateKey = keyPair.getPrivate();
        return bytesToHex(privateKey.getEncoded());
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02X", b));
        }
        return result.toString();
    }
}
