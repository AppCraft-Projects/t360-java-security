package com.training360.security._03_jaas;

import javax.security.auth.Subject;
import javax.security.auth.callback.*;
import javax.security.auth.spi.LoginModule;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Provider implements LoginModule {

    private static final Logger LOGGER = Logger.getLogger(Provider.class.getName());

    private Subject subject;
    private CallbackHandler callbackHandler;

    // not used in this simple LoginModule but can be useful when LoginModule are stacked
    private Map<String, ?> sharedState;
    private Map<String, ?> options;

    protected List<UserPrincipal> userPrincipals;
    protected List<RolePrincipal> rolePrincipals;

    @Override
    public void initialize(
            final Subject subject,
            final CallbackHandler callbackHandler,
            final Map<String, ?> sharedState,
            final Map<String, ?> options
    ) {
        this.subject = subject;
        this.callbackHandler = callbackHandler;
        this.sharedState = sharedState;
        this.options = options;

        this.userPrincipals = new ArrayList<>();
        this.rolePrincipals = new ArrayList<>();
    }

    @Override
    public boolean login() {
        final Callback[] callbacks = new Callback[]{
                new NameCallback("username"),
                new PasswordCallback("password", false)
        };
        try {
            callbackHandler.handle(callbacks);
        } catch (IOException | UnsupportedCallbackException e) {
            LOGGER.log(Level.SEVERE, "Can not authenticate user.", e);
            return false;
        }

        final String username = ((NameCallback) callbacks[0]).getName();
        final char[] password = ((PasswordCallback) callbacks[1]).getPassword();

        if (!UserData.USERNAME.equals(username) || !UserData.PASSWORD.equals(new String(password))) {
            return false;
        }

        userPrincipals.add(new UserPrincipal(UserData.USERNAME));
        return true;
    }

    @Override
    public boolean commit() {
        rolePrincipals.add(new RolePrincipal("Trainee"));
        rolePrincipals.add(new RolePrincipal("Programmer"));
        rolePrincipals.add(new RolePrincipal("Coffee Lover"));

        this.subject.getPrincipals().addAll(userPrincipals);
        this.subject.getPrincipals().addAll(rolePrincipals);

        return true;
    }

    @Override
    public boolean abort() {
        clear();
        return true;
    }

    private void clear() {
        if (rolePrincipals != null) {
            rolePrincipals.clear();
            rolePrincipals = null;
        }

        if (userPrincipals != null) {
            userPrincipals.clear();
            userPrincipals = null;
        }
    }

    @Override
    public boolean logout() {
        clear();
        return true;
    }
}
