package com.training360.security._04_jgss;

import org.ietf.jgss.*;

public class JGSS {


    /**
     * The full example won't work unless we have a Kerberos KDC (Key Distribution Center)
     */
    public static void main(String[] args) throws GSSException {
        System.setProperty("java.security.krb5.conf","src/main/resources/krb5.conf");
        System.setProperty("java.security.krb5.realm","EXAMPLE.COM");
        System.setProperty("java.security.krb5.kdc","example.com");
        System.setProperty("javax.security.auth.useSubjectCredsOnly","false");
        System.setProperty("java.security.auth.login.config","src/main/resources/login.conf");

        GSSContext clientContext = clientContext();
        GSSContext serverContext = serverContext();

        byte[] clientToken = clientContext.initSecContext(new byte[0], 0, 0);
        byte[] serverToken = serverContext.acceptSecContext(clientToken, 0, clientToken.length);

        clientContext.initSecContext(serverToken, 0, serverToken.length);

        // Send from client
        byte[] messageBytes = "Test Message".getBytes();
        MessageProp clientProp = new MessageProp(0, true);
        byte[] clientSendToken = clientContext.wrap(messageBytes, 0, messageBytes.length, clientProp);

        // receive on server
        MessageProp serverProp = new MessageProp(0, false);
        byte[] bytes = serverContext.unwrap(clientSendToken, 0, clientSendToken.length, serverProp);
        System.out.println(new String(bytes));
    }

    private static GSSContext clientContext() throws GSSException {
        GSSManager manager = GSSManager.getInstance();
        String serverPrinciple = "HTTP/localhost@EXAMPLE.COM";
        GSSName serverName = manager.createName(serverPrinciple, null);
        Oid krb5Oid = new Oid("1.2.840.113554.1.2.2");
        GSSContext clientContext = manager.createContext(
                serverName, krb5Oid, null, GSSContext.DEFAULT_LIFETIME);
        clientContext.requestMutualAuth(true);
        clientContext.requestConf(true);
        clientContext.requestInteg(true);
        return clientContext;
    }

    private static GSSContext serverContext() throws GSSException {
        GSSManager manager = GSSManager.getInstance();
        return manager.createContext((GSSCredential) null);
    }
}
