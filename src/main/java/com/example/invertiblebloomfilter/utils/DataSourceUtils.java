package com.example.invertiblebloomfilter.utils;

import oracle.jdbc.pool.OracleDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

public class DataSourceUtils {

    private static DataSource dataSource = null;

    public static DataSource buildDataSource() {
        if (dataSource == null) {
            String url = PropertyUtils.get("spring.datasource.url");
            String username = PropertyUtils.get("spring.datasource.username");
            String password = PropertyUtils.get("spring.datasource.password");
            return DataSourceUtils.createDataSource(url, username, password);
        }
        return dataSource;
    }
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
