package com.training360.security._02_encryption;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class HMAC {

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        String algorithm = "HmacSHA256";

        Mac mac = Mac.getInstance(algorithm);
        byte[] keyBytes = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        SecretKeySpec key = new SecretKeySpec(keyBytes, algorithm);

        mac.init(key);

        String msg = "This is a message";

        byte[] data  = msg.getBytes(StandardCharsets.UTF_8);
        byte[] macBytes = mac.doFinal(data);

        System.out.println(Arrays.toString(macBytes));

    }

}
