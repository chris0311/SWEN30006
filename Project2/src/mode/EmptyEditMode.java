package src.mode;

import src.Game;
import src.checker.CompositeLevelChecker;
import src.checker.LevelCheckerStrategy;
import src.file.Loader;
import src.mapeditor.editor.Controller;
import src.utility.GameCallback;

import java.io.File;
import java.util.Properties;

public class EmptyEditMode implements Mode{
    private Controller controller;
    private Game game;
    private Loader loader = new Loader();
    @Override
    public void start(GameCallback gameCallback, Properties properties, File file) throws InterruptedException {
        controller = new Controller(true);
        while (controller.isRunning()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        LevelCheckerStrategy levelStrategy = new CompositeLevelChecker();
        if (!levelStrategy.checkLevel(controller.getFile())){
            System.out.println("Invalid map, exit");
            return;
        }
        game = new Game(gameCallback, properties, loader.getMapString(controller.getFile()));
        Thread.sleep(1000);
        game.getFrame().dispose();
    }
}
