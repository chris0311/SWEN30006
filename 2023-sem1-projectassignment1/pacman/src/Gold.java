package src;

import ch.aplu.jgamegrid.Location;

public class Gold extends StaticEntity {
    public Gold() {
        super("gold.png");
    }
    public Gold(int x, int y) {
        super("gold.png", x, y);
    }
    public Gold(Location location) {
        super("gold.png", location);
    }

    @Override
    public String getClassName() {
        return "gold";
    }
}
