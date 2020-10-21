package com.training360.security._02_encryption;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

public class AsymmetricEncryption {

    public static void main(String[] args) {

        String plainText = "This is the message";
        try {
            // Generate keypair
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstanceStrong();
            keyPairGenerator.initialize(4096, random);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            // Encryption
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());

            byte[] cipherTextBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            // Raw bytes to base64
            String cipherText = Base64.getEncoder().encodeToString(cipherTextBytes);

            // Decryption
            cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
            byte[] decryptedCipherTextBytes = cipher.doFinal(Base64.getDecoder().decode(cipherText));
            String decryptedCipherText = new String(decryptedCipherTextBytes, StandardCharsets.UTF_8);

            System.out.println(plainText);
            System.out.println(decryptedCipherText);


        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidParameterException e) {
            e.printStackTrace();
        }
    }
}
