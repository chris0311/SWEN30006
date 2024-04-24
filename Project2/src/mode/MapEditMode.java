package src.mode;

import src.checker.CompositeLevelChecker;
import src.checker.LevelCheckerStrategy;
import src.file.Loader;
import src.mapeditor.editor.Controller;
import src.Game;
import src.utility.GameCallback;

import java.io.File;
import java.util.Properties;

public class MapEditMode implements Mode{

    @Override
    public void start(GameCallback gameCallback, Properties properties, File file) throws InterruptedException {
        Controller lc = new Controller(file);
        // do nothing and wait lc running
        while (lc.isRunning()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        LevelCheckerStrategy levelStrategy = new CompositeLevelChecker();
        Loader loader = new Loader(lc.getFile());
        if (levelStrategy.checkLevel(lc.getFile())){
            Game game = new Game(gameCallback, properties, loader.getMapString());
            Thread.sleep(1000);
            game.getFrame().dispose();
        } else {
            System.out.println("Invalid map, exit");
        }
    }
}
