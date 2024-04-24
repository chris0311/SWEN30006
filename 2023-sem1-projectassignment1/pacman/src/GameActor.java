package src;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGBackground;
import ch.aplu.jgamegrid.Location;

/*
A controller class for all game actors
Relay all the methods to the Actor class
 */
public class GameActor extends Actor {
    public GameActor() {
        super();
    }
    public GameActor(String fileName) {
        super(fileName);
    }

    public GameActor(boolean isRotatable, String fileName, int nbSprites) {
        super(isRotatable, fileName, nbSprites);
    }

    @Override
    public void act() {
    }

    @Override
    public void show() {

    }

    @Override
    public double getDirection() {
        return super.getDirection();
    }

    @Override
    public void setDirection(double direction) {
        super.setDirection(direction);
    }

    @Override
    public void setLocation(Location location) {
        super.setLocation(location);
    }

    @Override
    public Location getNextMoveLocation() {
        return super.getNextMoveLocation();
    }

    @Override
    public void turn(double angle) {
        super.turn(angle);
    }

    @Override
    public Location getLocation() {
        return super.getLocation();
    }

    @Override
    public GGBackground getBackground() {
        return super.getBackground();
    }

}
