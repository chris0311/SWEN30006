package src.checker;

import java.util.ArrayList;

public class CompositeGameChecker implements GameCheckerStrategy{
    private ArrayList<GameCheckerStrategy> strategies;
    public CompositeGameChecker(){
        this.strategies = new ArrayList<>();
        this.strategies.add(new SingleNameChecker());
        this.strategies.add(new DuplicateChecker());
    }

    @Override
    public boolean checkGame(String dirPath) {
        boolean result = true;
        for(GameCheckerStrategy strategy : strategies){
            result = result && strategy.checkGame(dirPath);
        }
        return result;
    }
}
