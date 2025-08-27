package com.randomuser.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestConfig {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = TestConfig.class.getClassLoader()
                .getResourceAsStream("test-config.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find test-config.properties");
            } else {
                properties.load(input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getBaseUrl() {
        return properties.getProperty("api.base.url", "https://randomuser.me/api");
    }

    public static int getTimeout() {
        return Integer.parseInt(properties.getProperty("api.timeout", "5000"));
    }
}