package src;

import src.file.ArgsHandler;
import src.mode.EmptyEditMode;
import src.mode.MapEditMode;
import src.mode.Mode;
import src.mode.TestMode;
import src.utility.GameCallback;
import src.utility.PropertiesLoader;

import java.io.File;
import java.util.Properties;

public class Driver {
    public static final String DEFAULT_PROPERTIES_PATH = "properties/test5.properties";
    /**
     * Starting point
     * @param args the command line arguments
     */

    public static void main(String args[]) throws InterruptedException {
        String propertiesPath = DEFAULT_PROPERTIES_PATH;
        final Properties properties = PropertiesLoader.loadPropertiesFile(propertiesPath);
        GameCallback gameCallback = new GameCallback();
        Mode mode = ArgsHandler.getInstance().getInputType(args);
        if (mode == null) {
            System.out.println("Invalid input");
        }
        else if (mode instanceof EmptyEditMode) {
            mode.start(gameCallback, properties, null);
        }
        else {
            mode.start(gameCallback, properties, new File(args[0]));
        }
    }
}
