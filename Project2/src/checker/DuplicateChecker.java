package src.checker;

import src.logger.EditLogger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DuplicateChecker implements GameCheckerStrategy {
    @Override
    public boolean checkGame(String dirPath) {
        // check no two files start with the same number
        File dir = new File(dirPath);
        File[] files = dir.listFiles();

        Map<String, String> fileMap = new HashMap<>();
        List<String> duplicateFiles = new ArrayList<>();
        List<String> duplicateNumbers = new ArrayList<>();
        assert files != null;
        for (File file : files) {
            if (file.getName().matches("^\\d.*")) {
                String fileName = file.getName();
                String fileNum = fileName.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[0];
                if (fileMap.containsKey(fileNum)) {
                    duplicateFiles.add(fileName);
                    duplicateNumbers.add(fileNum);
                } else {
                    fileMap.put(fileNum, fileName);
                }
            }
        }
        if (duplicateFiles.size() > 0) {
            for (String fileNum : duplicateNumbers) {
                duplicateFiles.add(fileMap.get(fileNum));
            }
            String message = String.format("[%s - multiple maps at same level: %s]", dirPath, String.join("; ", duplicateFiles));
            EditLogger.getInstance().writeString(message, dirPath);
            return false;
        }
        return true;
    }
}