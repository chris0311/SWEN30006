package src;

import ch.aplu.jgamegrid.Location;
import src.utility.GameCallback;

import java.util.Properties;

public class SimpleGame implements PacVersion {

    @Override
    public void setupGame(Game game) {
        game.setUpSimpleVerActor();
    }

    /*
    no effect in simple version
    */
    @Override
    public void eatPill(Game game, Location location) {
        return;
    }

    /*
    no effect in simple version
    */
    @Override
    public void eatGold(Game game, Location location) {
        return;
    }

    /*
    no effect in simple version
    */
    @Override
    public void eatIce(Game game, Location location) {
        return;
    }
}
