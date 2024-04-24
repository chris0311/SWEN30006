package src.checker;

import ch.aplu.jgamegrid.Location;
import src.logger.EditLogger;

import java.util.ArrayList;

public class SinglePacmanChecker implements LevelCheckerStrategy {
    @Override
    public boolean checkLevel(char[][] mazeArray, String levelName) {
        ArrayList<Location> pacmanLocations = new ArrayList<>();
        for (int i = 0; i < NB_VERT_CELLS; i++) {
            for (int k = 0; k < NB_HORZ_CELLS; k++) {
                if (mazeArray[i][k] == 'f') {
                    pacmanLocations.add(new Location(k, i));
                }
            }
        }

        if (pacmanLocations.size() > 1) {
            String pacLocation = "";
            for (Location location : pacmanLocations) {
                pacLocation += location.toString() + "; ";
            }
            String message = String.format("[%s - more than one start for PacMan: %s]", levelName, pacLocation);
            EditLogger.getInstance().writeString(message, levelName);
            return false;
        } else if (pacmanLocations.size() == 0) {
            String message = String.format("[%s - no start for PacMan]", levelName);
            EditLogger.getInstance().writeString(message, levelName);
            return false;
        }
        return true;
    }
}
