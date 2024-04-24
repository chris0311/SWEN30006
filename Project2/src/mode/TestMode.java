package src.mode;

import src.file.Loader;
import src.mapeditor.editor.Controller;
import src.Game;
import src.checker.CompositeGameChecker;
import src.checker.CompositeLevelChecker;
import src.checker.GameCheckerStrategy;
import src.checker.LevelCheckerStrategy;
import src.utility.GameCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

public class TestMode implements Mode{
    private Controller controller;
    private Game game;
    private Loader loader;
    @Override
    public void start(GameCallback gameCallback, Properties properties, File file) throws InterruptedException {
        // perform Gamecheck
        GameCheckerStrategy gameCheckerStrategy = new CompositeGameChecker();
        LevelCheckerStrategy levelStrategy = new CompositeLevelChecker();
        boolean valid = gameCheckerStrategy.checkGame(file.getName());
        boolean result = true;
        if (valid) {
            loader = new Loader(file);
            ArrayList<File> maps = loader.getFiles();
            int mapCount = maps.size();
            int currentMap = 0;
            for (File map : maps) {
                currentMap++;
                result = levelStrategy.checkLevel(map);
                if (!result) {
                    controller = new Controller(map);
                    break;
                }
                game = new Game(gameCallback, properties, loader.getMapString(map));
                if (!game.getWin()) {
                    Thread.sleep(1000);
                    game.getFrame().dispose();
                    break;
                }
                if (currentMap == mapCount){
                    // wait 1 sec before dispose
                    Thread.sleep(1000);
                }
                game.getFrame().dispose();
            }
        }
        if (result)
            controller = new Controller(true);

        while (controller.isRunning()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (levelStrategy.checkLevel(controller.getFile())) {
            game = new Game(gameCallback, properties, loader.getMapString(controller.getFile()));
            Thread.sleep(1000);
            game.getFrame().dispose();
        } else {
            System.out.println("Invalid map, exit");
        }
    }
}
