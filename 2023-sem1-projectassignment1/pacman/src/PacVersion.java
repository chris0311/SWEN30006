package src;

import ch.aplu.jgamegrid.Location;

/*
An interface to control different behaviors in different versions
 */
public interface PacVersion {
    void setupGame(Game game);

    void eatPill(Game game, Location location);
    void eatGold(Game game, Location location);
    void eatIce(Game game, Location location);
}
