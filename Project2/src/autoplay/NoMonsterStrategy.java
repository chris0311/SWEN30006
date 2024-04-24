package src.autoplay;

import ch.aplu.jgamegrid.Location;
import src.Game;
import src.PacActor;
import src.path.PathNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class NoMonsterStrategy implements PacAutoPlayStrategy {
    @Override
    public LinkedList<Location> getWalkPath(PacActor pacActor, Game game) {
        Queue<PathNode> queue = new LinkedList<>();
        PathNode start = new PathNode(pacActor.getLocation());
        queue.add(start);
        PathNode current = null;
        while (!queue.isEmpty()) {
            current = queue.poll();
            if (current.getLocation().equals(pacActor.closestPillLocation())) {
                return current.getPath();
            }
            // expand current node
            ArrayList<Location> neighbours = current.getNeighbours(game);
            for (Location neighbor : neighbours) {
                if (pacActor.canMove(neighbor)) {
                    LinkedList<Location> newPath = new LinkedList<>(current.getPath());
                    queue.add(new PathNode(neighbor, newPath, current.getCost() + 1));
                }
            }
        }
        return null;
    }
}
