package src;

import ch.aplu.jgamegrid.Location;

public class TX5 extends Monster {
    public TX5(Game game) {
        super(game, MonsterType.TX5.getImageName());
        stopMoving(5);
    }
    public void walkApproach() {
        // walk towards pacman if possible
        Location pacLocation = game.pacActor.getLocation();
        Location.CompassDirection compassDir =
                getLocation().get4CompassDirectionTo(pacLocation);
        Location next = getLocation().getNeighbourLocation(compassDir);
        if (!isVisited(next) && canMove(next)) {
            setDirection(compassDir);
            setLocation(next);
            super.notifyMonsterLocationChanged(next);
        } else {
            // Random walk
            super.randomWalk();
        }
    }

    @Override
    public MonsterType getType() {
        return MonsterType.TX5;
    }
}
