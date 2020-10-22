package com.training360.security._06_validation;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class Passwords {

    public static void main(String[] args) {
        System.out.println(argon2("password".toCharArray()));
    }

    private static String argon2(char[] password) {
        Argon2 argon2 = Argon2Factory.create();
        String hash = argon2.hash(10, 65536, 1, password);
        if (argon2.verify(hash, password)) {
            System.out.println("Password ok");
        } else {
            System.err.println("Password not ok");
        }
        argon2.wipeArray(password);
        return hash;
    }
}
