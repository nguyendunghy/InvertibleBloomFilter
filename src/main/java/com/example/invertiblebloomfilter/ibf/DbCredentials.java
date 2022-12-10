package com.example.invertiblebloomfilter.ibf;

import java.util.Optional;

public class DbCredentials {
    public String host;
    public Integer port;
    public Optional<String> database;
    public Object publicKey;
    public Optional<String> tunnelUser;
    public Optional<String> tunnelHost;
    public String user;
    public String password;
    public String alwaysEncrypted;
    private ConnectionType connectionType;

    public Optional<String> tunnelPort;

    public <T> DbCredentials(Object host, Object port, Optional<String> database, String user, Object password, Optional<T> empty, Optional<T> empty1, Optional<T> empty2, Object alwaysEncrypted) {

    }

    public DbCredentials(String host, int port, Optional<String> database, String user, String password, Optional<Object> empty, Optional<Object> empty1, Optional<Object> empty2) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    public DbCredentials() {
    }

    public DbCredentials(String host, int port, Optional<String> database, String user, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    public DbCredentials(String host, Integer port, Optional<String> database, String user, String password, Optional<String> tunnelHost, Optional<Integer> tunnelPort, Optional<String> tunnelUser, Boolean alwaysEncrypted) {

    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }
}
