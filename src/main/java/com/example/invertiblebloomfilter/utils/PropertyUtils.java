package com.example.invertiblebloomfilter.utils;

import lombok.extern.log4j.Log4j2;

import java.util.Properties;

@Log4j2
public class PropertyUtils {

    private static Properties properties = null;

    public static String get(String key) {
        if (properties == null) {
            properties = new Properties();
            java.net.URL url = ClassLoader.getSystemResource("application.properties");
            try {
                properties.load(url.openStream());
            } catch (Exception ex) {
                log.error("reading application property error ",ex);
            }
        }

        return properties.getProperty(key);
    }
}
