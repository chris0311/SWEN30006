package src;

import ch.aplu.jgamegrid.Location;
import src.utility.GameCallback;

import java.util.Properties;

public class MultiverseGame implements PacVersion {
    @Override
    public void setupGame(Game game) {
        game.setUpSimpleVerActor();
        game.setUpMultiverseVerActor();
    }

    /*
    no effect in multiverse version
     */
    @Override
    public void eatPill(Game game, Location location) {
        return;
    }

    /*
    make all monster furious
     */
    @Override
    public void eatGold(Game game, Location location) {
        game.notifyMonsterEatGold();
    }

    /*
    make all monster freeze
     */
    @Override
    public void eatIce(Game game, Location location) {
        game.notifyMonsterEatIce();
    }

}
