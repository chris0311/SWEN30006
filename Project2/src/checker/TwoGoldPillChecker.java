package src.checker;

import src.logger.EditLogger;

public class TwoGoldPillChecker implements LevelCheckerStrategy {
    @Override
    public boolean checkLevel(char[][] mazeArray, String levelName) {
        int goldCount = 0, pillCount = 0;
        for (int i = 0; i < NB_VERT_CELLS; i++) {
            for (int k = 0; k < NB_HORZ_CELLS; k++) {
                if (mazeArray[i][k] == 'd') {
                    goldCount++;
                } else if (mazeArray[i][k] == 'c') {
                    pillCount++;
                }
            }
        }
        if (goldCount + pillCount < 2){
            String message = String.format("[%s - less than 2 Gold and Pill]", levelName);
            EditLogger.getInstance().writeString(message, levelName);
            return false;
        }
        return true;
    }
}
