package src.checker;

import src.logger.EditLogger;

import java.io.File;

/*
Check if at least one file is legal
 */
public class SingleNameChecker implements GameCheckerStrategy{

    @Override
    public boolean checkGame(String dirPath) {
        // check if at least one xml file starts with a digit
        File dir = new File(dirPath);
        File[] files = dir.listFiles();

        boolean hasLegalFile = false;
        assert files != null;
        for(File file : files){
            if(file.getName().matches("^\\d.*")){
                hasLegalFile = true;
                break;
            }
        }
        if (!hasLegalFile){
            String message = String.format("[%s - no maps found]", dirPath);
            EditLogger.getInstance().writeString(message, dirPath);
            return false;
        }
        return true;
    }
}
