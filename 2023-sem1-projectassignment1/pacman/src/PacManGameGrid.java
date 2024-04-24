// PacGrid.java
package src;

import ch.aplu.jgamegrid.*;

import java.awt.*;
import java.util.ArrayList;

public class PacManGameGrid extends GameGrid {
    private int nbHorzCells;
    private int nbVertCells;
    private int[][] mazeArray;
    private GGBackground bg;

    public PacManGameGrid(int nbHorzCells, int nbVertCells, GGBackground bg) {
        this.nbHorzCells = nbHorzCells;
        this.nbVertCells = nbVertCells;
        mazeArray = new int[nbVertCells][nbHorzCells];
        String maze =
                "xxxxxxxxxxxxxxxxxxxx" + // 0
                        "x....x....g...x....x" + // 1
                        "xgxx.x.xxxxxx.x.xx.x" + // 2
                        "x.x.......i.g....x.x" + // 3
                        "x.x.xx.xx  xx.xx.x.x" + // 4
                        "x......x    x......x" + // 5
                        "x.x.xx.xxxxxx.xx.x.x" + // 6
                        "x.x......gi......x.x" + // 7
                        "xixx.x.xxxxxx.x.xx.x" + // 8
                        "x...gx....g...x....x" + // 9
                        "xxxxxxxxxxxxxxxxxxxx";// 10


        // Copy structure into integer array
        for (int i = 0; i < nbVertCells; i++) {
            for (int k = 0; k < nbHorzCells; k++) {
                int value = toInt(maze.charAt(nbHorzCells * i + k));
                mazeArray[i][k] = value;
            }
        }
        this.bg = bg;
    }

    public int getCell(Location location) {
        return mazeArray[location.y][location.x];
    }

    private int toInt(char c) {
        if (c == 'x')
            return 0;
        if (c == '.')
            return 1;
        if (c == ' ')
            return 2;
        if (c == 'g')
            return 3;
        if (c == 'i')
            return 4;
        return -1;
    }

    public void drawPacmanHit() {
        bg.setPaintColor(Color.red);
    }

    public void drawPacmanWin() {
        bg.setPaintColor(Color.yellow);
    }

    public CellType getCellTypeAt(Location location) {
        int type = getCell(location);
        return switch (type) {
            case 0 -> CellType.Wall;
            case 1 -> CellType.Pill;
            case 2 -> CellType.Empty;
            case 3 -> CellType.Gold;
            case 4 -> CellType.Ice;
            default -> CellType.Unknown;
        };
    }

    /*
    Draw the walls of the maze
     */
    public void drawWalls() {
        bg.clear(Color.gray);
        for (int y = 0; y < nbVertCells; y++) {
            for (int x = 0; x < nbHorzCells; x++) {
                bg.setPaintColor(Color.white);
                Location location = new Location(x, y);
                int a = getCell(location);
                if (a > 0)
                    bg.fillCell(location, Color.lightGray);
            }
        }
    }
}