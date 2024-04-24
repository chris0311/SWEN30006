package src.checker;

import src.logger.EditLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class AccessibleGoldPillChecker implements LevelCheckerStrategy {
    /*
    Path finding algorithm adapted and modified from: ChatGPT, 21 May, OpenAI, 8 Mar. 2023, chat.openai.com/chat
     */
    private List<Location> unreachableGoldLocations;
    private List<Location> unreachablePillLocations;

    @Override
    public boolean checkLevel(char[][] mazeArray, String levelName) {
        // Find the location of Pac-Man
        Location pacManLocation = findPacManLocation(mazeArray);
        if (pacManLocation == null) {
            // Pac-Man not found, level is invalid
            return false;
        }

        // Find the locations of all gold and pill tiles
        List<Location> goldLocations = new ArrayList<>();
        List<Location> pillLocations = new ArrayList<>();
        for (int y = 0; y < mazeArray.length; y++) {
            for (int x = 0; x < mazeArray[y].length; x++) {
                if (mazeArray[y][x] == 'c') {
                    pillLocations.add(new Location(x, y));
                } else if (mazeArray[y][x] == 'd') {
                    goldLocations.add(new Location(x, y));
                }
            }
        }

        // Check if there is a path from Pac-Man to each gold and pill tile
        List<Location> unreachableGoldLocations = new ArrayList<>();
        List<Location> unreachablePillLocations = new ArrayList<>();
        for (Location location : goldLocations) {
            if (!hasPathToLocation(mazeArray, pacManLocation, location)) {
                unreachableGoldLocations.add(location);
            }
        }
        for (Location location : pillLocations) {
            if (!hasPathToLocation(mazeArray, pacManLocation, location)) {
                unreachablePillLocations.add(location);
            }
        }

        if (unreachableGoldLocations.isEmpty() && unreachablePillLocations.isEmpty()) {
            return true;
        } else {
            this.unreachableGoldLocations = unreachableGoldLocations;
            this.unreachablePillLocations = unreachablePillLocations;
            if (!unreachableGoldLocations.isEmpty()) {
                String unreachableStr = "";
                for (Location location : unreachableGoldLocations) {
                    unreachableStr += location.toString() + "; ";
                }
                String message = String.format("[%s - Gold not accessible: %s]", levelName, unreachableStr);
                EditLogger.getInstance().writeString(message, levelName);
            }
            if (!unreachablePillLocations.isEmpty()) {
                String unreachableStr = "";
                for (Location location : unreachablePillLocations) {
                    unreachableStr += location.toString() + "; ";
                }
                String message = String.format("[%s - Pill not accessible: %s]", levelName, unreachableStr);
                EditLogger.getInstance().writeString(message, levelName);
            }

            return false;
        }
    }

    private Location findPacManLocation(char[][] mazeArray) {
        for (int y = 0; y < mazeArray.length; y++) {
            for (int x = 0; x < mazeArray[y].length; x++) {
                if (mazeArray[y][x] == 'f') {
                    return new Location(x, y);
                }
            }
        }
        return null;
    }

    private boolean hasPathToLocation(char[][] mazeArray, Location startLocation, Location endLocation) {
        // Create a priority queue to store the locations to visit
        PriorityQueue<Location> queue = new PriorityQueue<>();
        // Create a map to store the distance to each location
        Map<Location, Integer> distanceMap = new HashMap<>();
        // Add the start location to the queue with a distance of 0
        queue.add(startLocation);
        distanceMap.put(startLocation, 0);

        while (!queue.isEmpty()) {
            // Get the location with the smallest distance from the queue
            Location currentLocation = queue.poll();
            int currentDistance = distanceMap.get(currentLocation);

            // Check if we have reached the end location
            if (currentLocation.equals(endLocation)) {
                return true;
            }

            // Check the neighbors of the current location
            for (Location neighbor : getNeighbors(mazeArray, currentLocation)) {
                int neighborDistance = currentDistance + 1;
                if (!distanceMap.containsKey(neighbor) || neighborDistance < distanceMap.get(neighbor)) {
                    // Update the distance to the neighbor and add it to the queue
                    distanceMap.put(neighbor, neighborDistance);
                    queue.add(neighbor);
                }
            }
        }

        // No path found
        return false;
    }


    private List<Location> getNeighbors(char[][] mazeArray, Location location) {
        List<Location> neighbors = new ArrayList<>();
        int x = location.x;
        int y = location.y;
        if (x > 0 && mazeArray[y][x - 1] != 'b') {
            if (mazeArray[y][x - 1] == 'i' || mazeArray[y][x - 1] == 'j' || mazeArray[y][x - 1] == 'k' || mazeArray[y][x - 1] == 'l') {
                // Portal tile, find corresponding portal tile
                Location portalLocation = findPortalLocation(mazeArray, new Location(x - 1, y));
                if (portalLocation != null) {
                    neighbors.add(portalLocation);
                }
            } else {
                neighbors.add(new Location(x - 1, y));
            }
        }
        if (x < mazeArray[y].length - 1 && mazeArray[y][x + 1] != 'b') {
            if (mazeArray[y][x + 1] == 'j' || mazeArray[y][x + 1] == 'i' || mazeArray[y][x + 1] == 'l' || mazeArray[y][x + 1] == 'k') {
                // Portal tile, find corresponding portal tile
                Location portalLocation = findPortalLocation(mazeArray, new Location(x + 1, y));
                if (portalLocation != null) {
                    neighbors.add(portalLocation);
                }
            } else {
                neighbors.add(new Location(x + 1, y));
            }
        }
        if (y > 0 && mazeArray[y - 1][x] != 'b') {
            if (mazeArray[y - 1][x] == 'k' || mazeArray[y - 1][x] == 'l' || mazeArray[y - 1][x] == 'i' || mazeArray[y - 1][x] == 'j') {
                // Portal tile, find corresponding portal tile
                Location portalLocation = findPortalLocation(mazeArray, new Location(x, y - 1));
                if (portalLocation != null) {
                    neighbors.add(portalLocation);
                }
            } else {
                neighbors.add(new Location(x, y - 1));
            }
        }
        if (y < mazeArray.length - 1 && mazeArray[y + 1][x] != 'b') {
            if (mazeArray[y + 1][x] == 'l' || mazeArray[y + 1][x] == 'k' || mazeArray[y + 1][x] == 'j' || mazeArray[y + 1][x] == 'i') {
                // Portal tile, find corresponding portal tile
                Location portalLocation = findPortalLocation(mazeArray, new Location(x, y + 1));
                if (portalLocation != null) {
                    neighbors.add(portalLocation);
                }
            } else {
                neighbors.add(new Location(x, y + 1));
            }
        }
        return neighbors;
    }

    private Location findPortalLocation(char[][] mazeArray, Location portalLocation) {
        char portalType = mazeArray[portalLocation.y][portalLocation.x];
        for (int y = 0; y < mazeArray.length; y++) {
            for (int x = 0; x < mazeArray[y].length; x++) {
                if (mazeArray[y][x] == portalType && !(x == portalLocation.x && y == portalLocation.y)) {
//                    System.out.println("Found portal: " + portalType + " location: " + x + ", " + y);
                    return new Location(x, y);
                }
            }
        }
        return null;
    }

    private static class Location implements Comparable<Location> {
        private final int x;
        private final int y;

        public Location(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Location) {
                Location other = (Location) obj;
                return x == other.x && y == other.y;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return x * 31 + y;
        }

        @Override
        public int compareTo(Location other) {
            return Integer.compare(getDistance(), other.getDistance());
        }

        public int getDistance() {
            return x + y;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }
}