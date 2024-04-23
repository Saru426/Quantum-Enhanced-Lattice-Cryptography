package security;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class FileEncryption {

    private volatile boolean isRunning;
    private volatile double progress;
    private volatile String status;

    public static final byte ENCRYPT_MODE = 0;
    public static final byte DECRYPT_MODE = 1;

    public void start(File file, byte[] key, boolean isEncryptMode) {
        isRunning = true;
        progress = 0.0;
        status = isEncryptMode ? "Encrypting..." : "Decrypting...";

        try {
            if (isEncryptMode) {
                encryptFile(file, key);
            } else {
                decryptFile(file, key);
            }
            status = "Successfully " + (isEncryptMode ? "encrypted" : "decrypted") + " " + file.getName();
        } catch (Exception e) {
            status = "Error: " + e.getMessage();
        } finally {
            isRunning = false;
            progress = 1.0;
        }
    }

    public void abort() {
        isRunning = false;
        status = "Process aborted";
    }

    public double getProgress() {
        return progress;
    }

    public String getStatus() {
        return status;
    }

    public static void encryptFile(File file, byte[] key) throws IOException, NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        FileInputStream fis = new FileInputStream(file);
        byte[] fileContent = new byte[(int) file.length()];
        fis.read(fileContent);
        fis.close();

        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedContent = cipher.doFinal(fileContent);

        FileOutputStream fos = new FileOutputStream(file.getAbsolutePath() + ".enc");
        fos.write(encryptedContent);
        fos.close();
    }

    public static void decryptFile(File file, byte[] key) throws IOException, NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        FileInputStream fis = new FileInputStream(file);
        byte[] encryptedContent = new byte[(int) file.length()];
        fis.read(encryptedContent);
        fis.close();

        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedContent = cipher.doFinal(encryptedContent);

        String decryptedFilePath = file.getAbsolutePath().replace(".enc", "");
        FileOutputStream fos = new FileOutputStream(decryptedFilePath);
        fos.write(decryptedContent);
        fos.close();
    }
}
