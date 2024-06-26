package src.mapeditor.editor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import src.mapeditor.grid.Camera;
import src.mapeditor.grid.Grid;
import src.mapeditor.grid.GridCamera;
import src.mapeditor.grid.GridModel;
import src.mapeditor.grid.GridView;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import src.checker.CompositeLevelChecker;
import src.checker.LevelCheckerStrategy;

/**
 * Controller of the application.
 *
 * @author Daniel "MaTachi" Jonsson
 * @version 1
 * @since v0.0.5
 */
public class Controller implements ActionListener, GUIInformation {

    /**
     * The model of the map editor.
     */
    protected Grid model;

    private Tile selectedTile;
    private Camera camera;

    private List<Tile> tiles;

    protected GridView grid;
    protected View view;

    private int gridWith = Constants.MAP_WIDTH;
    private int gridHeight = Constants.MAP_HEIGHT;
    private String fileName;
    private File file;
    boolean running = true;

    /**
     * Construct the controller.
     */
    public Controller(boolean init_null) {
        if (init_null) {
            init(Constants.MAP_WIDTH, Constants.MAP_HEIGHT);
        }
    }

    public Controller() {
        this.tiles = TileManager.getTilesFromFolder("data/");
        this.model = new GridModel(Constants.MAP_WIDTH, Constants.MAP_WIDTH, tiles.get(0).getCharacter());
        this.camera = new GridCamera(model, Constants.GRID_WIDTH,
                Constants.GRID_HEIGHT);

        grid = new GridView(this, camera, tiles); // Every tile is
    }

    public Controller(File file){
        init(Constants.MAP_WIDTH, Constants.MAP_HEIGHT);
        this.file = file;
        loadFileToGui(file);
    }

    public void init(int width, int height) {
        this.tiles = TileManager.getTilesFromFolder("data/");
        this.model = new GridModel(width, height, tiles.get(0).getCharacter());
        this.camera = new GridCamera(model, Constants.GRID_WIDTH,
                Constants.GRID_HEIGHT);

        grid = new GridView(this, camera, tiles); // Every tile is
        // 30x30 pixels

        this.view = new View(this, camera, grid, tiles);
    }

    /**
     * Different commands that comes from the view.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        for (Tile t : tiles) {
            if (e.getActionCommand().equals(
                    Character.toString(t.getCharacter()))) {
                selectedTile = t;
                break;
            }
        }
        if (e.getActionCommand().equals("flipGrid")) {
            // view.flipGrid();
        } else if (e.getActionCommand().equals("save")) {
            saveFile();
        } else if (e.getActionCommand().equals("load")) {
            loadFile();
        } else if (e.getActionCommand().equals("update")) {
            updateGrid(gridWith, gridHeight);
        } else if (e.getActionCommand().equals("start_game")){
            startGame();
        }
    }

    public void updateGrid(int width, int height) {
        view.close();
        init(width, height);
        view.setSize(width, height);
    }

    public void startGame(){
        this.running = false;
    }

    DocumentListener updateSizeFields = new DocumentListener() {

        public void changedUpdate(DocumentEvent e) {
            gridWith = view.getWidth();
            gridHeight = view.getHeight();
        }

        public void removeUpdate(DocumentEvent e) {
            gridWith = view.getWidth();
            gridHeight = view.getHeight();
        }

        public void insertUpdate(DocumentEvent e) {
            gridWith = view.getWidth();
            gridHeight = view.getHeight();
        }
    };

    private void saveFile() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "xml files", "xml");
        chooser.setFileFilter(filter);
        File workingDirectory = new File(System.getProperty("user.dir"));
        chooser.setCurrentDirectory(workingDirectory);

        int returnVal = chooser.showSaveDialog(null);
        try {
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                this.fileName = chooser.getSelectedFile().getName();
                // level check
                File file = chooser.getSelectedFile();
                this.file = file;
                LevelCheckerStrategy levelCheckerStrategy = new CompositeLevelChecker();
                levelCheckerStrategy.checkLevel(file);

                Element level = new Element("level");
                Document doc = new Document(level);
                doc.setRootElement(level);

                Element size = new Element("size");
                int height = model.getHeight();
                int width = model.getWidth();
                size.addContent(new Element("width").setText(width + ""));
                size.addContent(new Element("height").setText(height + ""));
                doc.getRootElement().addContent(size);

                for (int y = 0; y < height; y++) {
                    Element row = new Element("row");
                    for (int x = 0; x < width; x++) {
                        char tileChar = model.getTile(x, y);
                        String type = "PathTile";

                        if (tileChar == 'b')
                            type = "WallTile";
                        else if (tileChar == 'c')
                            type = "PillTile";
                        else if (tileChar == 'd')
                            type = "GoldTile";
                        else if (tileChar == 'e')
                            type = "IceTile";
                        else if (tileChar == 'f')
                            type = "PacTile";
                        else if (tileChar == 'g')
                            type = "TrollTile";
                        else if (tileChar == 'h')
                            type = "TX5Tile";
                        else if (tileChar == 'i')
                            type = "PortalWhiteTile";
                        else if (tileChar == 'j')
                            type = "PortalYellowTile";
                        else if (tileChar == 'k')
                            type = "PortalDarkGoldTile";
                        else if (tileChar == 'l')
                            type = "PortalDarkGrayTile";

                        Element e = new Element("cell");
                        row.addContent(e.setText(type));
                    }
                    doc.getRootElement().addContent(row);
                }
                XMLOutputter xmlOutput = new XMLOutputter();
                xmlOutput.setFormat(Format.getPrettyFormat());
                xmlOutput
                        .output(doc, new FileWriter(chooser.getSelectedFile()));
            }
        } catch (FileNotFoundException e1) {
            JOptionPane.showMessageDialog(null, "Invalid file!", "error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
        }
    }

    public void loadFile() {
        SAXBuilder builder = new SAXBuilder();
        try {
            JFileChooser chooser = new JFileChooser();
            File selectedFile;
            BufferedReader in;
            FileReader reader = null;
            File workingDirectory = new File(System.getProperty("user.dir"));
            chooser.setCurrentDirectory(workingDirectory);

            int returnVal = chooser.showOpenDialog(null);
            Document document;
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                selectedFile = chooser.getSelectedFile();
                this.file = selectedFile;
                if (selectedFile.canRead() && selectedFile.exists()) {
                    // level check
                    File file = chooser.getSelectedFile();
                    LevelCheckerStrategy levelCheckerStrategy = new CompositeLevelChecker();
                    levelCheckerStrategy.checkLevel(file);
                    document = (Document) builder.build(selectedFile);

                    Element rootNode = document.getRootElement();

                    List sizeList = rootNode.getChildren("size");
                    Element sizeElem = (Element) sizeList.get(0);
                    int height = Integer.parseInt(sizeElem
                            .getChildText("height"));
                    int width = Integer
                            .parseInt(sizeElem.getChildText("width"));
                    updateGrid(width, height);

                    List rows = rootNode.getChildren("row");
                    for (int y = 0; y < rows.size(); y++) {
                        Element cellsElem = (Element) rows.get(y);
                        List cells = cellsElem.getChildren("cell");

                        for (int x = 0; x < cells.size(); x++) {
                            Element cell = (Element) cells.get(x);
                            String cellValue = cell.getText();

                            char tileNr = 'z';
                            if (cellValue.equals("PathTile"))
                                tileNr = 'a';
                            else if (cellValue.equals("WallTile"))
                                tileNr = 'b';
                            else if (cellValue.equals("PillTile"))
                                tileNr = 'c';
                            else if (cellValue.equals("GoldTile"))
                                tileNr = 'd';
                            else if (cellValue.equals("IceTile"))
                                tileNr = 'e';
                            else if (cellValue.equals("PacTile"))
                                tileNr = 'f';
                            else if (cellValue.equals("TrollTile"))
                                tileNr = 'g';
                            else if (cellValue.equals("TX5Tile"))
                                tileNr = 'h';
                            else if (cellValue.equals("PortalWhiteTile"))
                                tileNr = 'i';
                            else if (cellValue.equals("PortalYellowTile"))
                                tileNr = 'j';
                            else if (cellValue.equals("PortalDarkGoldTile"))
                                tileNr = 'k';
                            else if (cellValue.equals("PortalDarkGrayTile"))
                                tileNr = 'l';
                            else
                                tileNr = '0';

                            model.setTile(x, y, tileNr);
                        }
                    }

                    String mapString = model.getMapAsString();
                    grid.redrawGrid();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String loadFile(File file) {
        SAXBuilder builder = new SAXBuilder();
        this.file = file;
        try {
            Document document = (Document) builder.build(file);

            Element rootNode = document.getRootElement();

            List sizeList = rootNode.getChildren("size");
            Element sizeElem = (Element) sizeList.get(0);
            int height = Integer.parseInt(sizeElem.getChildText("height"));
            int width = Integer.parseInt(sizeElem.getChildText("width"));
//            updateGrid(width, height);

            List rows = rootNode.getChildren("row");
            for (int y = 0; y < rows.size(); y++) {
                Element cellsElem = (Element) rows.get(y);
                List cells = cellsElem.getChildren("cell");

                for (int x = 0; x < cells.size(); x++) {
                    Element cell = (Element) cells.get(x);
                    String cellValue = cell.getText();

                    char tileNr = 'z';
                    if (cellValue.equals("PathTile"))
                        tileNr = 'a';
                    else if (cellValue.equals("WallTile"))
                        tileNr = 'b';
                    else if (cellValue.equals("PillTile"))
                        tileNr = 'c';
                    else if (cellValue.equals("GoldTile"))
                        tileNr = 'd';
                    else if (cellValue.equals("IceTile"))
                        tileNr = 'e';
                    else if (cellValue.equals("PacTile"))
                        tileNr = 'f';
                    else if (cellValue.equals("TrollTile"))
                        tileNr = 'g';
                    else if (cellValue.equals("TX5Tile"))
                        tileNr = 'h';
                    else if (cellValue.equals("PortalWhiteTile"))
                        tileNr = 'i';
                    else if (cellValue.equals("PortalYellowTile"))
                        tileNr = 'j';
                    else if (cellValue.equals("PortalDarkGoldTile"))
                        tileNr = 'k';
                    else if (cellValue.equals("PortalDarkGrayTile"))
                        tileNr = 'l';
                    else
                        tileNr = '0';

                    model.setTile(x, y, tileNr);
                }
            }
        } catch (JDOMException | IOException e) {
            e.printStackTrace();
        }
//        System.out.println(model.getMapAsString());
        String res = model.getMapAsString();
        res = res.replaceAll("\n", "");
        res = res.substring(0, Constants.MAP_WIDTH * Constants.MAP_HEIGHT);
        return res;
    }

    public void loadFileToGui(File file) {
        SAXBuilder builder = new SAXBuilder();
        try {
//            JFileChooser chooser = new JFileChooser();
            File selectedFile;
            BufferedReader in;
            FileReader reader = null;
            File workingDirectory = new File(System.getProperty("user.dir"));
//            chooser.setCurrentDirectory(workingDirectory);

            int returnVal = JFileChooser.APPROVE_OPTION;
            Document document;
            selectedFile = file;
            if (selectedFile.canRead() && selectedFile.exists()) {
                document = (Document) builder.build(selectedFile);
                // level check
                LevelCheckerStrategy levelCheckerStrategy = new CompositeLevelChecker();
                boolean result = levelCheckerStrategy.checkLevel(selectedFile);

                Element rootNode = document.getRootElement();

                List sizeList = rootNode.getChildren("size");
                Element sizeElem = (Element) sizeList.get(0);
                int height = Integer.parseInt(sizeElem
                        .getChildText("height"));
                int width = Integer
                        .parseInt(sizeElem.getChildText("width"));
                updateGrid(width, height);

                List rows = rootNode.getChildren("row");
                for (int y = 0; y < rows.size(); y++) {
                    Element cellsElem = (Element) rows.get(y);
                    List cells = cellsElem.getChildren("cell");

                    for (int x = 0; x < cells.size(); x++) {
                        Element cell = (Element) cells.get(x);
                        String cellValue = cell.getText();

                        char tileNr = 'z';
                        if (cellValue.equals("PathTile"))
                            tileNr = 'a';
                        else if (cellValue.equals("WallTile"))
                            tileNr = 'b';
                        else if (cellValue.equals("PillTile"))
                            tileNr = 'c';
                        else if (cellValue.equals("GoldTile"))
                            tileNr = 'd';
                        else if (cellValue.equals("IceTile"))
                            tileNr = 'e';
                        else if (cellValue.equals("PacTile"))
                            tileNr = 'f';
                        else if (cellValue.equals("TrollTile"))
                            tileNr = 'g';
                        else if (cellValue.equals("TX5Tile"))
                            tileNr = 'h';
                        else if (cellValue.equals("PortalWhiteTile"))
                            tileNr = 'i';
                        else if (cellValue.equals("PortalYellowTile"))
                            tileNr = 'j';
                        else if (cellValue.equals("PortalDarkGoldTile"))
                            tileNr = 'k';
                        else if (cellValue.equals("PortalDarkGrayTile"))
                            tileNr = 'l';
                        else
                            tileNr = '0';

                        model.setTile(x, y, tileNr);
                    }
                }

                String mapString = model.getMapAsString();
                grid.redrawGrid();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tile getSelectedTile() {
        return selectedTile;
    }

    public String getFileName() {
        return fileName;
    }
    public File getFile() {
        return file;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
