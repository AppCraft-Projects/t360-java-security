package com.training360.security._02_encryption;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class DigitalSignature {

    public static void main(String[] args) {

        String plainText = "This is the message";
        try {
            // Generate keypair
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA", "SUN");
            SecureRandom random = SecureRandom.getInstanceStrong();
            keyPairGenerator.initialize(1024, random);

            KeyPair pair = keyPairGenerator.generateKeyPair();
            PrivateKey privateKey = pair.getPrivate();
            PublicKey publicKey = pair.getPublic();

            Signature sigWriter = Signature.getInstance("SHA1withDSA", "SUN");
            sigWriter.initSign(privateKey);
            sigWriter.update(plainText.getBytes(StandardCharsets.UTF_8));

            byte[] sigToVerify = sigWriter.sign();

            // Verify
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
            KeyFactory keyFactory = KeyFactory.getInstance("DSA", "SUN");
            PublicKey pubKey = keyFactory.generatePublic(pubKeySpec);
            Signature sigReader = Signature.getInstance("SHA1withDSA", "SUN");
            sigReader.initVerify(pubKey);
            sigReader.update(plainText.getBytes(StandardCharsets.UTF_8));
            boolean verifies = sigReader.verify(sigToVerify);
            System.out.println("signature verifies: " + verifies);

        } catch (NoSuchAlgorithmException | InvalidKeyException | InvalidParameterException | SignatureException | NoSuchProviderException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }
}
