package src.checker;

import ch.aplu.jgamegrid.Location;
import src.logger.EditLogger;
import src.tiles.portal.PortalColor;

import java.util.ArrayList;

public class PairPortalChecker implements LevelCheckerStrategy {
    @Override
    public boolean checkLevel(char[][] mazeArray, String levelName) {
        boolean isPair = true;
        for (PortalColor color : PortalColor.values()) {
            ArrayList<Location> portalLocations = countPortal(color, mazeArray);
            if (portalLocations.size() > 0 && portalLocations.size() != 2) {
                isPair = false;
                String portalLocation = "";
                for (Location location : portalLocations) {
                    portalLocation += location.toString() + "; ";
                }
                // cast color to lowercase except for the first letter
                String colorString = color.toString();
                colorString = colorString.charAt(0) + colorString.substring(1).toLowerCase();
                String message = String.format("[%s - portal %s count is not 2: %s]", levelName, colorString, portalLocation);
                EditLogger.getInstance().writeString(message, levelName);
            }
        }
        return isPair;
    }

    private ArrayList<Location> countPortal(PortalColor color, char[][] mazeArray){
        ArrayList<Location> portalLocations = new ArrayList<>();
        for (int i = 0; i < NB_VERT_CELLS; i++) {
            for (int k = 0; k < NB_HORZ_CELLS; k++) {
                if (mazeArray[i][k] == color.getChar()) {
                    portalLocations.add(new Location(k, i));
                }
            }
        }
        return portalLocations;
    }
}
