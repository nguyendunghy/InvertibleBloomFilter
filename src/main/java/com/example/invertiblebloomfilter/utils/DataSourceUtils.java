package com.example.invertiblebloomfilter.utils;

import oracle.jdbc.pool.OracleDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

public class DataSourceUtils {

    public static DataSource createDataSource(String url, String user, String password) {
        try {
            OracleDataSource dataSource = new OracleDataSource();
            dataSource.setURL(url);
            dataSource.setUser(user);
            dataSource.setPassword(password);
            return dataSource;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
