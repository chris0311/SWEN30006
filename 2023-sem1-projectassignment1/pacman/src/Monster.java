// Monster.java
// Used for PacMan
package src;

import ch.aplu.jgamegrid.*;

import java.awt.Color;
import java.util.*;

public abstract class Monster extends GameActor implements Freezable, Furious {
    protected Game game;
    private ArrayList<Location> visitedList = new ArrayList<Location>();
    private final int listLength = 10;
    private boolean stopMoving = false;
    private int seed = 0;
    protected boolean isFurious = false;
    private boolean isFrozen = false;
    protected Random randomiser = new Random(0);

    public Monster(Game game, String fileName) {
        super("sprites/" + fileName);
        this.game = game;
    }

    /*
    Make a monster stop moving for a certain amount of time
     */
    public void stopMoving(int seconds) {
        this.stopMoving = true;
        Timer timer = new Timer(); // Instantiate Timer Object
        int SECOND_TO_MILLISECONDS = 1000;
        final Monster monster = this;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                monster.stopMoving = false;
            }
        }, (long) seconds * SECOND_TO_MILLISECONDS);
    }

    public void setSeed(int seed) {
        this.seed = seed;
        randomiser.setSeed(seed);
    }

    public void setStopMoving(boolean stopMoving) {
        this.stopMoving = stopMoving;
    }

    @Override
    public void act() {
        if (stopMoving) {
            return;
        }
        walkApproach();
        if (getDirection() > 150 && getDirection() < 210)
            setHorzMirror(false);
        else
            setHorzMirror(true);
    }

    public abstract MonsterType getType();

    protected void addVisitedList(Location location) {
        visitedList.add(location);
        if (visitedList.size() == listLength)
            visitedList.remove(0);
    }

    protected boolean isVisited(Location location) {
        for (Location loc : visitedList)
            if (loc.equals(location))
                return true;
        return false;
    }

    protected boolean canMove(Location location) {
        Color c = getBackground().getColor(location);
        if (c.equals(Color.gray) || location.getX() >= game.getNumHorzCells()
                || location.getX() < 0 || location.getY() >= game.getNumVertCells() || location.getY() < 0)
            return false;
        else
            return true;
    }

    /*
    Defines how a monster moves
     */
    public abstract void walkApproach();

    protected void notifyMonsterLocationChanged(Location newLocation) {
        game.getGameCallback().monsterLocationChanged(this);
        addVisitedList(newLocation);
    }

    /*
    Make a monster walk randomly
     */
    protected void randomWalk() {
        double oldDirection = getDirection();
        Location next = getNextLocation(oldDirection);
        setLocation(next);
        notifyMonsterLocationChanged(next);
    }

    /*
    returns the next location of a monster
     */
    private Location getNextLocation(double oldDirection) {
        Location next;
        int sign = randomiser.nextDouble() < 0.5 ? 1 : -1;
        setDirection(oldDirection);
        turn(sign * 90);  // Try to turn left/right
        if (isFurious) {
            next = getFuriousLocation(oldDirection, sign);
            if (next == null)
                next = getNormalLocation(oldDirection, sign);
        }
        else {
            next = getNormalLocation(oldDirection, sign);
        }
        return next;
    }

    /*
    returns the next location of a monster when it is not furious
     */
    private Location getNormalLocation(double oldDirection, int sign) {
        Location next;
        next = getNextMoveLocation();
        if (canMove(next)) {
            setLocation(next);
        } else {
            setDirection(oldDirection);
            next = getNextMoveLocation();
            if (canMove(next)) // Try to move forward
            {
                setLocation(next);
            } else {
                setDirection(oldDirection);
                turn(-sign * 90);  // Try to turn right/left
                next = getNextMoveLocation();
                if (canMove(next)) {
                    setLocation(next);
                } else {
                    setDirection(oldDirection);
                    turn(180);  // Turn backward
                    next = getNextMoveLocation();
                    setLocation(next);
                }
            }
        }
        return next;
    }

    /*
    returns the next location of a monster when it is furious
     */
    private Location getFuriousLocation(double oldDirection, int sign) {
        // check if the monster can move by 2 cells
        Location next;
        Location middle = getNextMoveLocation();
        next = middle.getNeighbourLocation(getDirection());
        if (canMove(next) && canMove(middle)) {
            setLocation(next);
        } else {
            setDirection(oldDirection);
            middle = getNextMoveLocation();
            next = middle.getNeighbourLocation(getDirection());
            if (canMove(next) && canMove(middle)) // Try to move forward
            {
                setLocation(next);
            } else {
                setDirection(oldDirection);
                turn(-sign * 90);  // Try to turn right/left
                middle = getNextMoveLocation();
                next = middle.getNeighbourLocation(getDirection());
                if (canMove(next) && canMove(middle)) {
                    setLocation(next);
                } else {
                    setDirection(oldDirection);
                    turn(180);  // Turn backward
                    next = getNextMoveLocation().getNeighbourLocation(getDirection());
                    setLocation(next);
                }
            }
        }
        if (!canMove(next)){
            // return null if monster cannot walk 2 cells
            return null;
        }
        return next;
    }

    /*
    returns all the neighbour cells of a monster
     */
    protected ArrayList<Location> getNeighbours() {
        ArrayList<Location> neighbours = new ArrayList<>();
        Location currentLocation = getLocation();
        if (!isFurious) {
            neighbours.add(currentLocation.getNeighbourLocation(Location.CompassDirection.NORTH));
            neighbours.add(currentLocation.getNeighbourLocation(Location.CompassDirection.SOUTH));
            neighbours.add(currentLocation.getNeighbourLocation(Location.CompassDirection.EAST));
            neighbours.add(currentLocation.getNeighbourLocation(Location.CompassDirection.WEST));
        } else {
            if (canMove(currentLocation.getNeighbourLocation(Location.CompassDirection.NORTH)))
                neighbours.add(currentLocation.getNeighbourLocation(Location.CompassDirection.NORTH).getNeighbourLocation(Location.CompassDirection.NORTH));
            if (canMove(currentLocation.getNeighbourLocation(Location.CompassDirection.SOUTH)))
                neighbours.add(currentLocation.getNeighbourLocation(Location.CompassDirection.SOUTH).getNeighbourLocation(Location.CompassDirection.SOUTH));
            if (canMove(currentLocation.getNeighbourLocation(Location.CompassDirection.EAST)))
                neighbours.add(currentLocation.getNeighbourLocation(Location.CompassDirection.EAST).getNeighbourLocation(Location.CompassDirection.EAST));
            if (canMove(currentLocation.getNeighbourLocation(Location.CompassDirection.WEST)))
                neighbours.add(currentLocation.getNeighbourLocation(Location.CompassDirection.WEST).getNeighbourLocation(Location.CompassDirection.WEST));
//            neighbours.add(currentLocation.getNeighbourLocation(Location.CompassDirection.NORTH).getNeighbourLocation(Location.CompassDirection.NORTH));
//            neighbours.add(currentLocation.getNeighbourLocation(Location.CompassDirection.SOUTH).getNeighbourLocation(Location.CompassDirection.SOUTH));
//            neighbours.add(currentLocation.getNeighbourLocation(Location.CompassDirection.EAST).getNeighbourLocation(Location.CompassDirection.EAST));
//            neighbours.add(currentLocation.getNeighbourLocation(Location.CompassDirection.WEST).getNeighbourLocation(Location.CompassDirection.WEST));
        }

        return neighbours;
    }

    public void getFreeze(int seconds) {
        this.isFurious = false;
        this.isFrozen = true;
        this.stopMoving = true;
        Timer timer = new Timer(); // Instantiate Timer Object
        int SECOND_TO_MILLISECONDS = 1000;
        final Monster monster = this;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                monster.isFrozen = false;
                monster.stopMoving = false;
            }
        }, (long) seconds * SECOND_TO_MILLISECONDS);
    }

    public void getFurious(int seconds) {
        if (isFrozen) return;
        this.isFurious = true;
        Timer timer = new Timer(); // Instantiate Timer Object
        int SECOND_TO_MILLISECONDS = 1000;
        final Monster monster = this;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                monster.isFurious = false;
            }
        }, (long) seconds * SECOND_TO_MILLISECONDS);
    }
}
