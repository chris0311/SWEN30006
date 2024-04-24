package src.checker;

import java.util.ArrayList;

public class CompositeLevelChecker implements LevelCheckerStrategy {
    private ArrayList<LevelCheckerStrategy> strategies;

    public CompositeLevelChecker(){
        this.strategies = new ArrayList<>();
        this.strategies.add(new PairPortalChecker());
        this.strategies.add(new TwoGoldPillChecker());
        this.strategies.add(new AccessibleGoldPillChecker());
    }

    public boolean checkLevel(char[][] mazeArray, String levelName){
        // check only one pacman first
        SinglePacmanChecker singlePacmanChecker = new SinglePacmanChecker();
        if(!singlePacmanChecker.checkLevel(mazeArray, levelName)){
            return false;
        }

        boolean result = true;
        for(LevelCheckerStrategy strategy : strategies){
            result = result && strategy.checkLevel(mazeArray, levelName);
        }
        return result;
    }
}
