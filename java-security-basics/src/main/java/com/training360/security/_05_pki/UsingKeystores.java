package com.training360.security._05_pki;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class UsingKeystores {

    private static final char[] PASSWORD = "password".toCharArray();
    private static final String KEYSTORE_FILE = "my-keystore.jks";

    public static void main(String[] args) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        createKeystore();
        KeyStore ks = loadKeystore();

        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        SecretKey secretKey = keyGen.generateKey();

        KeyStore.SecretKeyEntry secret = new KeyStore.SecretKeyEntry(secretKey);
        KeyStore.ProtectionParameter password = new KeyStore.PasswordProtection(PASSWORD);
        ks.setEntry("my-secret", secret, password);

    }

    private static KeyStore loadKeystore() throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(KEYSTORE_FILE), PASSWORD);
        return ks;
    }

    private static void createKeystore() throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException {
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());

        // this creates a new keystore (null)
        ks.load(null, PASSWORD);

        try (FileOutputStream fos = new FileOutputStream(KEYSTORE_FILE)) {
            ks.store(fos, PASSWORD);
        }
    }
}
