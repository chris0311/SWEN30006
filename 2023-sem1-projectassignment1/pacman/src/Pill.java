package src;

import ch.aplu.jgamegrid.Location;

public class Pill extends StaticEntity{
    public Pill(Location location) {
        super(location);
    }

    @Override
    public String getClassName() {
        return "pills";
    }
}
