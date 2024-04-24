package src.mode;

import src.utility.GameCallback;

import java.io.File;
import java.util.Properties;

public interface Mode {
    void start(GameCallback gameCallback, Properties properties, File file) throws InterruptedException;
}
