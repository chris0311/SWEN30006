package src.file;

import src.mapeditor.editor.Controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Loader {
    private final static int nbHorzCells = 20;
    private final static int nbVertCells = 11;
    private ArrayList<String> mapList;
    private ArrayList<File> files;
    private Controller controller;
    private String fileName;
    private File file;

    public Loader(File file) {
        this.file = file;
        fileName = file.getName();
        controller = new Controller();
        mapList = new ArrayList<>();
        files = new ArrayList<>();
        loadMapsStringList();
        loadMapFiles();
    }

    public Loader() {
        controller = new Controller();
        mapList = new ArrayList<>();
        files = new ArrayList<>();
    }

    private void loadMapFiles() {
        if (!file.isDirectory()){
            files.add(file);
        }
        else {
            File[] files = file.listFiles();
            assert files != null;
            Map<Integer, File> fileMap = new HashMap<>();
            for (File file : files) {
                if (file.getName().matches("^\\d.*")){
                    String fileName = file.getName();
                    String fileNum = fileName.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[0];
                    fileMap.put(Integer.parseInt(fileNum), file);
                }
            }
            // sort the fileMap with ascending order
            Map<Integer, File> sortedFileMap = new TreeMap<>(fileMap);
            // store the sorted files into an ArrayList
            this.files.addAll(sortedFileMap.values());
        }
    }

    private void loadMapsStringList() {
        if (!file.isDirectory()){
            mapList.add(controller.loadFile(file));
        }
        else {
            File[] files = file.listFiles();
            assert files != null;
            Map<Integer, String> fileMap = new HashMap<>();
            for (File file : files) {
                if (file.getName().matches("^\\d.*")){
                    String fileName = file.getName();
                    String fileNum = fileName.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)")[0];
                    fileMap.put(Integer.parseInt(fileNum), controller.loadFile(file));
                }
            }
            // sort the fileMap with ascending order
            Map<Integer, String> sortedFileMap = new TreeMap<>(fileMap);
            // store the sorted files into an ArrayList
            this.mapList.addAll(sortedFileMap.values());
        }
    }

    public ArrayList<String> getMapList() {
        return mapList;
    }

    public ArrayList<File> getFiles() {
        return files;
    }

    public String getFileName(){
        return fileName;
    }

    public String getMapString(int index){
        return mapList.get(index);
    }

    public String getMapString(){
        return getMapString(0);
    }

    public char[][] loadFileToArray(int index){
        String map = this.mapList.get(index);
        char[][] mazeArray = new char[nbVertCells][nbHorzCells];
        for (int i = 0; i < nbVertCells; i++) {
            for (int k = 0; k < nbHorzCells; k++) {
                char value = map.charAt(nbHorzCells * i + k);
                mazeArray[i][k] = value;
            }
        }
        return mazeArray;
    }

    public char[][] loadFileToArray(){
        return loadFileToArray(0);
    }

    public String getMapString (File file) {
        return controller.loadFile(file);
    }
}
