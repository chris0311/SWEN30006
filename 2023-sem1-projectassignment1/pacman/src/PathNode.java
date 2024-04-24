package src;

import ch.aplu.jgamegrid.Location;

import java.util.ArrayList;
import java.util.LinkedList;

/*
Used by path search algorithms
 */
public class PathNode {
    private LinkedList<Location> path;
    private Location location;
    private int cost;

    public PathNode(Location location) {
        this.location = location;
        this.path = new LinkedList<>();
        this.cost = 0;
    }

    public PathNode(Location location, LinkedList<Location> path, int cost) {
        this.location = location;
        this.path = path;
        path.add(location);
        this.cost = cost;
    }

    public LinkedList<Location> getPath() {
        return path;
    }

    public Location getLocation() {
        return location;
    }

    public int getCost() {
        return cost;
    }

    public ArrayList<Location> getNeighbours() {
        ArrayList<Location> neighbours = new ArrayList<>();
        Location currentLocation = getLocation();
        neighbours.add(currentLocation.getNeighbourLocation(Location.CompassDirection.NORTH));
        neighbours.add(currentLocation.getNeighbourLocation(Location.CompassDirection.SOUTH));
        neighbours.add(currentLocation.getNeighbourLocation(Location.CompassDirection.EAST));
        neighbours.add(currentLocation.getNeighbourLocation(Location.CompassDirection.WEST));

        return neighbours;
    }
}
