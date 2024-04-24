package src;

import ch.aplu.jgamegrid.Location;

import java.util.ArrayList;
import java.util.HashSet;

public class Wizard extends Monster {
    private Location destination;
    public Wizard(Game game) {
        super(game, MonsterType.Wizard.getImageName());
    }

    @Override
    public MonsterType getType() {
        return MonsterType.Wizard;
    }

    @Override
    public void walkApproach() {
        // move to a random location
        if (!game.getGrid().getCellTypeAt(getLocation()).equals((CellType.Wall)))
            this.destination = getDestination();
        Location.CompassDirection compassDir = getLocation().getCompassDirectionTo(this.destination);
        setDirection(compassDir);
        setLocation(this.destination);
    }

    private Location getDestination() {
        ArrayList<Location> reachableLocations = getNeighbours();
        // randomly choose a location
        int randomIndex = randomiser.nextInt(reachableLocations.size());
        return reachableLocations.get(randomIndex);
    }

    /*
    get all neighbour locations, check one cell beyond if it is a wall
     */
    @Override
    protected ArrayList<Location> getNeighbours(){
        ArrayList<Location> neighbours = new ArrayList<>();
        Location location = getLocation(), next;
        if (!isFurious) {
            if (canMove(next = location.getNeighbourLocation(Location.CompassDirection.NORTH))) {
                neighbours.add(next);
            } else if (canMove(next = next.getNeighbourLocation(Location.CompassDirection.NORTH))) {
                neighbours.add(next);
            }
            if (canMove(next = location.getNeighbourLocation(Location.CompassDirection.SOUTH))) {
                neighbours.add(next);
            } else if (canMove(next = next.getNeighbourLocation(Location.CompassDirection.SOUTH))) {
                neighbours.add(next);
            }
            if (canMove(next = location.getNeighbourLocation(Location.CompassDirection.EAST))) {
                neighbours.add(next);
            } else if (canMove(next = next.getNeighbourLocation(Location.CompassDirection.EAST))) {
                neighbours.add(next);
            }
            if (canMove(next = location.getNeighbourLocation(Location.CompassDirection.WEST))) {
                neighbours.add(next);
            } else if (canMove(next = next.getNeighbourLocation(Location.CompassDirection.WEST))) {
                neighbours.add(next);
            }
            if (canMove(next = location.getNeighbourLocation(Location.CompassDirection.NORTHEAST))) {
                neighbours.add(next);
            } else if (canMove(next = next.getNeighbourLocation(Location.CompassDirection.NORTHEAST))) {
                neighbours.add(next);
            }
            if (canMove(next = location.getNeighbourLocation(Location.CompassDirection.NORTHWEST))) {
                neighbours.add(next);
            } else if (canMove(next = next.getNeighbourLocation(Location.CompassDirection.NORTHWEST))) {
                neighbours.add(next);
            }
            if (canMove(next = location.getNeighbourLocation(Location.CompassDirection.SOUTHEAST))) {
                neighbours.add(next);
            } else if (canMove(next = next.getNeighbourLocation(Location.CompassDirection.SOUTHEAST))) {
                neighbours.add(next);
            }
            if (canMove(next = location.getNeighbourLocation(Location.CompassDirection.SOUTHWEST))) {
                neighbours.add(next);
            } else if (canMove(next = next.getNeighbourLocation(Location.CompassDirection.SOUTHWEST))) {
                neighbours.add(next);
            }
        }
        else {
            if (canMove(next = location.getNeighbourLocation(Location.CompassDirection.NORTH).getNeighbourLocation(Location.CompassDirection.NORTH))) {
                neighbours.add(next);
            } else if (canMove(next = next.getNeighbourLocation(Location.CompassDirection.NORTH))) {
                neighbours.add(next);
            }
            if (canMove(next = location.getNeighbourLocation(Location.CompassDirection.SOUTH).getNeighbourLocation(Location.CompassDirection.SOUTH))) {
                neighbours.add(next);
            } else if (canMove(next = next.getNeighbourLocation(Location.CompassDirection.SOUTH))) {
                neighbours.add(next);
            }
            if (canMove(next = location.getNeighbourLocation(Location.CompassDirection.EAST).getNeighbourLocation(Location.CompassDirection.EAST))) {
                neighbours.add(next);
            } else if (canMove(next = next.getNeighbourLocation(Location.CompassDirection.EAST))) {
                neighbours.add(next);
            }
            if (canMove(next = location.getNeighbourLocation(Location.CompassDirection.WEST).getNeighbourLocation(Location.CompassDirection.WEST))) {
                neighbours.add(next);
            } else if (canMove(next = next.getNeighbourLocation(Location.CompassDirection.WEST))) {
                neighbours.add(next);
            }
            if (canMove(next = location.getNeighbourLocation(Location.CompassDirection.NORTHEAST).getNeighbourLocation(Location.CompassDirection.NORTHEAST))) {
                neighbours.add(next);
            } else if (canMove(next = next.getNeighbourLocation(Location.CompassDirection.NORTHEAST))) {
                neighbours.add(next);
            }
            if (canMove(next = location.getNeighbourLocation(Location.CompassDirection.NORTHWEST).getNeighbourLocation(Location.CompassDirection.NORTHWEST))) {
                neighbours.add(next);
            } else if (canMove(next = next.getNeighbourLocation(Location.CompassDirection.NORTHWEST))) {
                neighbours.add(next);
            }
            if (canMove(next = location.getNeighbourLocation(Location.CompassDirection.SOUTHEAST).getNeighbourLocation(Location.CompassDirection.SOUTHEAST))) {
                neighbours.add(next);
            } else if (canMove(next = next.getNeighbourLocation(Location.CompassDirection.SOUTHEAST))) {
                neighbours.add(next);
            }
            if (canMove(next = location.getNeighbourLocation(Location.CompassDirection.SOUTHWEST).getNeighbourLocation(Location.CompassDirection.SOUTHWEST))) {
                neighbours.add(next);
            } else if (canMove(next = next.getNeighbourLocation(Location.CompassDirection.SOUTHWEST))) {
                neighbours.add(next);
            }
        }
        return neighbours;
    }

}
