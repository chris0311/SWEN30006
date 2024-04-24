package src.autoplay;

import ch.aplu.jgamegrid.Location;
import src.Game;
import src.PacActor;

import java.util.LinkedList;
import java.util.List;

public interface PacAutoPlayStrategy {
    public LinkedList<Location> getWalkPath(PacActor pacActor, Game game);
}
