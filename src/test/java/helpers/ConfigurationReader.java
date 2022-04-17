package helpers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationReader {

    private static Properties properties;
    static {
        String path = "C:\\Users\\green\\IdeaProjects\\SAS_challenge\\configuration.properties";
        try{
            FileInputStream fileInputStream = new FileInputStream(path);
            System.out.println(path + " no error");
            properties = new Properties();
            properties.load(fileInputStream);
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println(path + " caught FileNotFoundException error");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(path + " caught IO error");
        }
    }

    public static String getProperty(String key){
        return properties.getProperty(key);
    }
}
