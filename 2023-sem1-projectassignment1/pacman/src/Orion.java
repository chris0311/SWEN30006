package src;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import ch.aplu.jgamegrid.*;

public class Orion extends Monster {
    private ArrayList<Location> goldLocations = new ArrayList<>();
    private HashSet<Location> visitedLocations = new HashSet<>();
    private ArrayList<Location> remainingLocations = new ArrayList<>(); // location that has gold
    private Location destination;
    private boolean changeLocation;
    private LinkedList<Location> walkPath = new LinkedList<>();

    public Orion(Game game) {
        super(game, MonsterType.Orion.getImageName());
        getGoldLocation();

        // copy all gold locations to remaining locations
        this.remainingLocations.addAll(goldLocations);
    }

    @Override
    public MonsterType getType() {
        return MonsterType.Orion;
    }

    @Override
    public void walkApproach() {
        if (walkPath.size() == 0) {
            // reached destination, check if there is gold remaining
            changeLocation = true;
            remainingLocations.removeIf(location -> !checkGoldExist(location));
        }
        if (changeLocation) {
            // find path to next destination if destination changed
            destination = pickNextDestination();
            changeLocation = false;
            findPath();
        }
        // get next location from path
        Location next = walkPath.poll();
        // move two steps if furious
        if (isFurious && walkPath.size() != 0){
            Location temp = next;
            next = walkPath.poll();
            if (getLocation().getDirectionTo(next) != getLocation().getDirectionTo(temp)) {
                super.randomWalk();
                // need to find path again after random move
                findPath();
                return;
            }
        }
        Location.CompassDirection compassDir = getLocation().get4CompassDirectionTo(next);
        setDirection(compassDir);
        setLocation(next);

        notifyMonsterLocationChanged(next);
    }

    /*
    Use BFS to find path to destination
     */
    private void findPath() {
        Queue<PathNode> queue = new LinkedList<>();
        PathNode start = new PathNode(getLocation());
        queue.add(start);
        PathNode current = null;
        while (!queue.isEmpty()) {
            current = queue.poll();
            if (current.getLocation().equals(destination)) {
                // path found
                walkPath = current.getPath();
                break;
            }
            // expand current node
            ArrayList<Location> neighbours = current.getNeighbours();
            for (Location neighbour : neighbours) {
                if (canMove(neighbour)) {
                    LinkedList<Location> newPath = new LinkedList<>(current.getPath());
                    queue.add(new PathNode(neighbour, newPath, current.getCost() + 1));
                }
            }
        }
    }

    /*
    Randomly pick a unvisited gold to visit, if there are any
    Randomly select a gold location otherwise
     */
    private Location pickNextDestination() {
        Location dest;
        if (visitedLocations.size() == goldLocations.size()) {
            // visited all gold locations
            visitedLocations.clear();
        }

        if (remainingLocations.size() != 0 && !visitedLocations.containsAll(remainingLocations)) {
            // still have locations that has gold unvisited, randomly pick one location from remaining locations
            dest = pickRandomLocation(remainingLocations);
            this.visitedLocations.add(dest);
        }
        else {
            // randomly pick one location from all gold locations
            dest = pickRandomLocation(goldLocations);
            this.visitedLocations.add(dest);
        }
        return dest;
    }

    /*
    Randomly pick a unvisited location from the list
     */
    private Location pickRandomLocation(ArrayList<Location>locations) {
        Location dest;
        int index;
        do {
            index = randomiser.nextInt(locations.size());
            dest = locations.get(index);
        } while (visitedLocations.contains(dest)); // if the location is visited, pick another one
        return dest;
    }

    /*
    Find all gold locations in game
     */
    private void getGoldLocation() {
        ArrayList<Location> allItemLocation = game.getItemLocation();
        for (Location location : allItemLocation) {
            if (game.getGrid().getCellTypeAt(location).equals(CellType.Gold)) {
                this.goldLocations.add(location);
            }
        }
    }

    /*
    Check if there is gold at the location
     */
    private boolean checkGoldExist(Location location) {
        return super.game.getItemLocation().contains(location);
    }
}
