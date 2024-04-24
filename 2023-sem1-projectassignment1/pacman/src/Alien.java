package src;

import ch.aplu.jgamegrid.Location;

import java.util.ArrayList;

public class Alien extends Monster {
    public Alien(Game game) {
        super(game, MonsterType.Alien.getImageName());
    }

    @Override
    public MonsterType getType() {
        return MonsterType.Alien;
    }

    /*
    perform walk, Alien will move to the nearest location to Pacman
    If furious, Alien will walk two steps
    */
    @Override
    public void walkApproach() {

        ArrayList<Location> destinations = getDestination();

        // randomly choose a location from nearestLocations
        int randomIndex = randomiser.nextInt(destinations.size());
        Location destination = destinations.get(randomIndex);

        // walk to the destination
        Location.CompassDirection compassDir = getLocation().getCompassDirectionTo(destination);
        setDirection(compassDir);
        if (isFurious){
            Location next = getLocation().getNeighbourLocation(compassDir);
            Location nextNext = next.getNeighbourLocation(compassDir);
            if (canMove(next) && canMove(nextNext)) {
                destination = nextNext;
            }
        }
        setLocation(destination);

    }

    /*
    Get a list of reachable locations
     */
    private ArrayList<Location> getDestination() {
        ArrayList<Location> reachableLocations = computeReachableLocations();
        ArrayList<Location> nearestLocations = new ArrayList<>();
        Location pacLocation = game.pacActor.getLocation();

        // find the shortest distance
        double minDistance = Integer.MAX_VALUE;
        for (Location location : reachableLocations) {
            double distance = location.getDistanceTo(pacLocation);
            if (distance < minDistance) {
                minDistance = distance;
            }
        }
        // find all locations that have the shortest distance
        for (Location location : reachableLocations) {
            if (location.getDistanceTo(pacLocation) == minDistance) {
                nearestLocations.add(location);
            }
        }
        return nearestLocations;
    }

    /*
    Filter out unreachable locations
     */
    private ArrayList<Location> computeReachableLocations() {
        // compute reachable locations
        ArrayList<Location> reachableLocations = this.getNeighbours();
        reachableLocations.removeIf(location -> !super.canMove(location));
        return reachableLocations;
    }
    /*
    Get all neighbours of the current location
     */
    @Override
    protected ArrayList<Location> getNeighbours(){
        ArrayList<Location> neighbours;
        Location currentLocation = getLocation();
        neighbours = super.getNeighbours();
        if (!isFurious) {
            neighbours.add(currentLocation.getNeighbourLocation(Location.CompassDirection.NORTHEAST));
            neighbours.add(currentLocation.getNeighbourLocation(Location.CompassDirection.NORTHWEST));
            neighbours.add(currentLocation.getNeighbourLocation(Location.CompassDirection.SOUTHEAST));
            neighbours.add(currentLocation.getNeighbourLocation(Location.CompassDirection.SOUTHWEST));
        }
        else {
            // make move stop to 2 if furious
            if (canMove(currentLocation.getNeighbourLocation(Location.CompassDirection.NORTHEAST)))
                neighbours.add(currentLocation.getNeighbourLocation(Location.CompassDirection.NORTHEAST).getNeighbourLocation(Location.CompassDirection.NORTHEAST));
            if (canMove(currentLocation.getNeighbourLocation(Location.CompassDirection.NORTHWEST)))
                neighbours.add(currentLocation.getNeighbourLocation(Location.CompassDirection.NORTHWEST).getNeighbourLocation(Location.CompassDirection.NORTHWEST));
            if (canMove(currentLocation.getNeighbourLocation(Location.CompassDirection.SOUTHEAST)))
                neighbours.add(currentLocation.getNeighbourLocation(Location.CompassDirection.SOUTHEAST).getNeighbourLocation(Location.CompassDirection.SOUTHEAST));
            if (canMove(currentLocation.getNeighbourLocation(Location.CompassDirection.SOUTHWEST)))
                neighbours.add(currentLocation.getNeighbourLocation(Location.CompassDirection.SOUTHWEST).getNeighbourLocation(Location.CompassDirection.SOUTHWEST));
        }
        return neighbours;
    }
}
