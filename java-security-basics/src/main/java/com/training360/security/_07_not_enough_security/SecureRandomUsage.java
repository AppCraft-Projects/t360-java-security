package com.training360.security._07_not_enough_security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SecureRandomUsage {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstanceStrong();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);
        int i =random.nextInt(100); //random int in range [0 .. 100)
        double d = random.nextDouble(); //random double in range [0.0 .. 1.0)
        boolean b = random.nextBoolean(); // random bool, true or false
    }
}
