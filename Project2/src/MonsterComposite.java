package src;

import java.util.ArrayList;

public class MonsterComposite {
    ArrayList<Monster> monsters;
    public MonsterComposite(){
        this.monsters = new ArrayList<>();
    }

    public void addMonster(Monster monster){
        this.monsters.add(monster);
    }

    public void removeMonster(Monster monster){
        this.monsters.remove(monster);
    }

    public void setSlowDown(int factor){
        for(Monster monster : monsters){
            monster.setSlowDown(factor);
        }
    }

    public void setSeed(int seed){
        for(Monster monster : monsters){
            monster.setSeed(seed);
        }
    }

    public void setStopMoving(boolean stopMoving){
        for(Monster monster : monsters){
            monster.setStopMoving(stopMoving);
        }
    }

    public boolean checkCollision(PacActor pacman){
        for(Monster monster : monsters){
            if (monster.getLocation().equals(pacman.getLocation())){
                return true;
            }
        }
        return false;
    }
    public void removeMonsters(){
        for(Monster monster : monsters){
            monster.removeSelf();
        }
        monsters.clear();
    }
}
