package com.example.statrystesting.ibf;

import java.util.Optional;

public class OracleCredentials extends DbCredentials {

    public <T> OracleCredentials(String host, int hostPort, Optional<String> defaultDatabase, String user, String password, Optional<T> empty, Optional<T> empty1, Optional<T> empty2, Optional<T> orclpdb1, boolean tlsFlagRollout) {
        this.host = host;
        this.database = defaultDatabase;
        this.port = hostPort;
        this.user = user;
        this.password = password;
    }
}
