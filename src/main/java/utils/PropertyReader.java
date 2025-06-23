package utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {

   public static final Properties properties = new Properties();

    static {
        try {
            String path = System.getProperty("user.dir") + "/config.properties";
            System.out.println("Loading config.properties from: " + path);
            InputStream input = new FileInputStream(path);
            properties.load(input);
        } catch (Exception e) {
            System.err.println("Failed to load config.properties");
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        String value = properties.getProperty(key);
        if (value == null || value.trim().isEmpty()) {
            System.out.println("Missing or empty property: " + key);
        }
        return value;
    }
}
