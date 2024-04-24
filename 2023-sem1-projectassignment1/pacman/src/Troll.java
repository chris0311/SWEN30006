package src;

import ch.aplu.jgamegrid.Location;

public class Troll extends Monster {

    public Troll(Game game) {
        super(game, MonsterType.Troll.getImageName());
    }

    @Override
    public MonsterType getType() {
        return MonsterType.Troll;
    }

    @Override
    public void walkApproach() {
        super.randomWalk();
    }
}
