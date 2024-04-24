package src.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
public class EditLogger {
    private String logFilePath;
    private FileWriter fileWriter = null;
    private static final EditLogger INSTANCE = new EditLogger();

    private EditLogger(){

    }

    public void writeString(String str, String filePath) {
        this.logFilePath = filePath + "Log.txt";
        createFileWriter();
        try {
            fileWriter.write(str);
            fileWriter.write("\n");
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createFileWriter(){
        try {
            File file = new File(logFilePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            fileWriter = new FileWriter(file, false);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static EditLogger getInstance(){
        return INSTANCE;
    }
}
