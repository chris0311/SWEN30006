package src;

import ch.aplu.jgamegrid.Location;

public class Ice extends StaticEntity {
    public Ice() {
        super("ice.png");
    }
    public Ice(int x, int y) {
        super("ice.png", x, y);
    }
    public Ice(Location location) {
        super("ice.png", location);
    }

    public String getClassName() {
        return "ice";
    }
}
