package com.training360.security._08_code_quality;

import java.util.ArrayList;
import java.util.List;

public class SecretData {


    public static void main(String[] args) throws CloneNotSupportedException {
        SecretData original = new SecretData(new ArrayList<>());
        original.putSecretPassword("secret");
        System.out.println(original.toString());

        SecretData clone = (SecretData) original.clone();
        System.out.println(clone.toString());
    }

    private boolean shared = true;
    private List<String> passwords;

    public SecretData(List<String> passwords) {
        this.passwords = passwords;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new SecretData(new ArrayList<>(passwords));
    }

    final void putSecretPassword(String password) {
        shared = false;
        passwords.add(password);
    }

    final public String toString() {
        return shared ? String.join(", ", passwords) : "";
    }

    final void setPassword(String password) { passwords.add(password); }
}
