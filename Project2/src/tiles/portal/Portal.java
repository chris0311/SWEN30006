package src.tiles.portal;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;

public class Portal extends Actor {
    private PortalColor color;

    public Portal(PortalColor color, Location location) {
        super("sprites/" + color.getImageName());
        this.color = color;
        this.setLocation(location);
    }

    public PortalColor getColor() {
        return this.color;
    }
}
