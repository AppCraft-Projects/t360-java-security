package com.training360.security._03_jaas;

import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import java.security.Principal;
import java.util.HashMap;

public class JAASLogin {

    public static void main(String[] args) throws Exception {

        final CallbackHandler callbackHandler = callbacks -> {
            for (final Callback callback : callbacks) {
                if (callback instanceof NameCallback) {
                    ((NameCallback) callback).setName(UserData.USERNAME);

                } else if (callback instanceof PasswordCallback) {
                    ((PasswordCallback) callback).setPassword(UserData.PASSWORD.toCharArray());

                } else {
                    throw new UnsupportedCallbackException(callback);
                }
            }
        };

        final Configuration config = new Configuration() {
            @Override
            public AppConfigurationEntry[] getAppConfigurationEntry(String name) {

                return new AppConfigurationEntry[]{
                        new AppConfigurationEntry(
                                Provider.class.getName(),
                                AppConfigurationEntry.LoginModuleControlFlag.REQUIRED,
                                new HashMap<>()
                        )
                };
            }
        };

        // if the config is not provided then, it looks for the system property java.security.auth.login.config
        // and loads the configuration from the file
        // System.setProperty("java.security.auth.login.config", Thread.currentThread().getContextClassLoader().getResource("jaas.config").toExternalForm());
        // final LoginContext loginContext = new LoginContext("example", new Subject(), callbackHandler);

        final LoginContext loginContext = new LoginContext("example", new Subject(), callbackHandler, config);
        loginContext.login();
        // after a successful login this will contain the principals
        final Subject subject = loginContext.getSubject();

        System.out.println("=== User principals ===");
        for (final Principal principal : subject.getPrincipals()) {
            System.out.println(principal);
        }

    }
}
