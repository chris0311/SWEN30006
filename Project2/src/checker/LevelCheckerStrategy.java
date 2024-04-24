package src.checker;

import src.file.Loader;

import java.io.File;

public interface LevelCheckerStrategy {
    int NB_HORZ_CELLS = 20;
    int NB_VERT_CELLS = 11;
    boolean checkLevel(char[][] mazeArray, String levelName);
    default boolean checkLevel(File file) {
        Loader loader = new Loader(file);
        char[][] mazeArray = loader.loadFileToArray();
        return checkLevel(mazeArray, file.getName());
    }
}
