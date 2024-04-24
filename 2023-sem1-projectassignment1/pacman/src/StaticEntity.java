package src;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;

public abstract class StaticEntity extends Actor {
    protected Location location;
    private final int RADIUS = 5;
    public StaticEntity(Location location) {
        super();
        this.location = location;
    }
    public StaticEntity(String fileName) {
        super("sprites/" + fileName);
    }
    public StaticEntity(String fileName, int x, int y) {
        super("sprites/" + fileName);
        this.location = new Location(x, y);
    }
    public StaticEntity(String fileName, Location location) {
        super("sprites/" + fileName);
        this.location = location;
    }

    public abstract String getClassName();
    public void setLocation(Location location) {
        this.location = location;
    }
    public void setLocation(int x, int y) {
        this.location = new Location(x, y);
    }
    public int getRadius() {
        return RADIUS;
    }
}
