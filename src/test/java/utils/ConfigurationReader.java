package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationReader {
    private static final Properties properties;

    static {
        try {
            try (FileInputStream fileInputStream = new FileInputStream("src/test/resources/application.properties")) {
                properties = new Properties();
                properties.load(fileInputStream);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
            throw new RuntimeException("Failed to load application.properties");
        }
    }

    public static String getProperty(String propertyKey) {
        return properties.getProperty(propertyKey);
    }

}
