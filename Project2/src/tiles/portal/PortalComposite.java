package src.tiles.portal;

import ch.aplu.jgamegrid.Location;
import src.Game;

import java.util.ArrayList;

public class PortalComposite {
    private ArrayList<Portal> portals;

    public PortalComposite() {
        this.portals = new ArrayList<>();
    }

    public void addPortal(Portal portal) {
        this.portals.add(portal);
    }

    public ArrayList<Portal> getPortals() {
        return this.portals;
    }

    public Location getExitLocation(Portal portal) {
        PortalColor color = portal.getColor();
        for (Portal p : this.portals) {
            if (p.getColor() == color && !p.getLocation().equals(portal.getLocation())) {
                return p.getLocation();
            }
        }
        return null;
    }

    public Location transit(Location location) {
        for (Portal portal : this.portals) {
            if (portal.getLocation().equals(location)) {
                return this.getExitLocation(portal);
            }
        }
        return location;
    }

    public void removePortal(Portal portal) {
        this.portals.remove(portal);
    }
}
