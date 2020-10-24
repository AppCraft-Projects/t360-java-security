package com.training360.security._08_code_quality;

import java.lang.reflect.Field;

public class UnsafeReflection {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        User user = new User("password");
        final Field password = user.getClass().getDeclaredField("password");
        password.setAccessible(true);
        System.out.println(password.get(user));
    }

    static class User {

        private String password;

        public User(String password) {
            this.password = password;
        }
    }
}
