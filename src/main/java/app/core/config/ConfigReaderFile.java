package app.core.config;

import java.io.Reader;
import java.io.*;
import java.util.Properties;

/**
 * Created by Ebrahim with ❤️ on 14 December 2019.
 */


public class ConfigReaderFile {

    private static final String address = "src/main/resources/application.properties";
    private static final String fileName = "application.properties";

    public ConfigReaderFile() {
    }


    private static synchronized Properties getProperties() {
        Properties properties = new Properties();
        try {
            FileInputStream inputStream = new FileInputStream(address);
            Reader reader = new InputStreamReader(inputStream, "UTF-8");
            Throwable var5 = null;

            try {
                properties.load(reader);
            } catch (Throwable var15) {
                var5 = var15;
                throw var15;
            } finally {
                if (reader != null) {
                    if (var5 != null) {
                        try {
                            reader.close();
                        } catch (Throwable var14) {
                            var5.addSuppressed(var14);
                        }
                    } else {
                        reader.close();
                    }
                }
            }
        } catch (Exception var17) {
            var17.printStackTrace();
        }

        properties.put(fileName, properties);
        return properties;
    }


    public static synchronized String getString(String key) {
        Properties properties = new Properties();
        InputStream inputStream = ConfigReaderFile.class.getClassLoader().getResourceAsStream(fileName);
        if (inputStream != null) {
            try {
                properties.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                throw new FileNotFoundException("property file '" + fileName + "' not found in the classpath");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return properties.getProperty(key);
    }

    public static synchronized Properties getConfig() {
        return getProperties();
    }
}
