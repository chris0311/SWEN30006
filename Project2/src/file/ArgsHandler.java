package src.file;

import src.mode.EmptyEditMode;
import src.mode.MapEditMode;
import src.mode.Mode;
import src.mode.TestMode;

import java.io.File;

public class ArgsHandler {
    private static final ArgsHandler INSTANCE = new ArgsHandler();
    private String[] args;

    private ArgsHandler() {
    }

    public static ArgsHandler getInstance() {
        return INSTANCE;
    }

    public Mode getInputType(String... input) {
        // check if the input is a directory or a file
        if (input.length == 0) {
            return new EmptyEditMode();
        }
        File file = new File(input[0]);
        if (file.isDirectory()) {
            return new TestMode();
        } else if (file.isFile()) {
            return new MapEditMode();
        } else {
            return null;
        }
    }
}
