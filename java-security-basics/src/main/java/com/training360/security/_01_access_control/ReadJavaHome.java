package com.training360.security._01_access_control;

import java.io.File;

public class ReadJavaHome {

    public static void main(String[] args) {
        // no SecurityManager is defined by default
        System.out.printf("java.home is : %s%n", System.getProperty("java.home"));
        // we define one
        System.setSecurityManager(new SecurityManager());
        // java.home can't be read by default
        System.out.printf("java.home is : %s%n", System.getProperty("java.home"));

        // solution?

        File policy = new File("src/main/resources/home.policy");
        // we set the policy
        System.setProperty("java.security.policy", policy.getAbsolutePath());
        // we create the security manager that will use the policy
        System.setSecurityManager(new SecurityManager());
        // now we can rea java.home
        System.out.printf("java.home is : %s%n", System.getProperty("java.home"));
    }
}
