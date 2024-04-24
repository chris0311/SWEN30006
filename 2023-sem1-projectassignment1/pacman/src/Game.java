// PacMan.java
// Simple PacMan implementation
package src;

import ch.aplu.jgamegrid.*;
import src.utility.GameCallback;

import java.awt.*;
import java.util.ArrayList;
import java.util.Properties;

public class Game extends GameGrid {
    private final static int nbHorzCells = 20;
    private final static int nbVertCells = 11;
    protected PacManGameGrid grid;
    protected PacActor pacActor = new PacActor(this);
    private ArrayList<Monster> monsters = new ArrayList<>();
    private ArrayList<StaticEntity> entities = new ArrayList<>();
    private GameCallback gameCallback;
    private Properties properties;
    private int seed = 30006;
    private int iceCount = 0;  // number of ice cells in game
    private PacVersion gameVersion;
    private ArrayList<Location> pillAndItemLocations = new ArrayList<Location>();

    public Game(GameCallback gameCallback, Properties properties) {
        //Setup game
        super(nbHorzCells, nbVertCells, 20, false);
        this.gameCallback = gameCallback;
        this.properties = properties;
        setSimulationPeriod(100);
        setTitle("[PacMan in the Multiverse]");

        //Setup for auto test
        setUpAutoTest(properties);

        grid = new PacManGameGrid(nbHorzCells, nbVertCells, getBg());

        setupPillAndItemsLocations();
        setupGrid();

        // setup game
        setupGameVersion(properties);
        gameVersion.setupGame(this);

        //Setup Random seeds
        setUpGameProperties(properties);

        //Run the game
        doRun();
        show();
        // Loop to look for collision in the application thread
        // This makes it improbable that we miss a hit
        boolean hasPacmanBeenHit = checkPacmanWin();

        delay(120);

        Location loc = pacActor.getLocation();
        stopAllActors();

        indicateGameEnd(gameCallback, hasPacmanBeenHit, loc);

        doPause();
    }

    /*
    Set up actors in a simple version game
     */
    public void setUpSimpleVerActor() {
        // set up Troll
        monsters.add(new Troll(this));
        // set up TX5
        monsters.add(new TX5(this));
        // set up monster locations
        String[] monsterLocation;
        int x, y;
        for (Monster monster : getMonsters()) {
            switch (monster.getType()) {
                case Troll -> {
                    monsterLocation = getProperties().getProperty("Troll.location").split(",");
                    x = Integer.parseInt(monsterLocation[0]);
                    y = Integer.parseInt(monsterLocation[1]);
                    addActor(monster, new Location(x, y), Location.NORTH);
                }
                case TX5 -> {
                    monsterLocation = getProperties().getProperty("TX5.location").split(",");
                    x = Integer.parseInt(monsterLocation[0]);
                    y = Integer.parseInt(monsterLocation[1]);
                    addActor(monster, new Location(x, y), Location.NORTH);
                }
                default -> {
                }
            }
        }
        // set up PacMan
        String[] pacManLocations = getProperties().getProperty("PacMan.location").split(",");
        int pacManX = Integer.parseInt(pacManLocations[0]);
        int pacManY = Integer.parseInt(pacManLocations[1]);
        addActor(pacActor, new Location(pacManX, pacManY));
    }
    /*
    Set up additional actors in a multiverse version game
     */
    public void setUpMultiverseVerActor(){
        monsters.add(new Orion(this));
        monsters.add(new Alien(this));
        monsters.add(new Wizard(this));
        // set up monster locations
        String[] monsterLocation;
        int x, y;
        for (Monster monster : monsters) {
            switch (monster.getType()) {
                case Orion -> {
                    monsterLocation = getProperties().getProperty("Orion.location").split(",");
                    x = Integer.parseInt(monsterLocation[0]);
                    y = Integer.parseInt(monsterLocation[1]);
                    addActor(monster, new Location(x, y), Location.NORTH);
                }
                case Alien -> {
                    monsterLocation = getProperties().getProperty("Alien.location").split(",");
                    x = Integer.parseInt(monsterLocation[0]);
                    y = Integer.parseInt(monsterLocation[1]);
                    addActor(monster, new Location(x, y), Location.NORTH);
                }
                case Wizard -> {
                    monsterLocation = getProperties().getProperty("Wizard.location").split(",");
                    x = Integer.parseInt(monsterLocation[0]);
                    y = Integer.parseInt(monsterLocation[1]);
                    addActor(monster, new Location(x, y), Location.NORTH);
                }
                default -> {
                }
            }
        }
    }

    public ArrayList<Monster> getMonsters() {
        return monsters;
    }

    public Properties getProperties() {
        return properties;
    }

    /*
    pause all monsters and remove PacMan
     */
    private void stopAllActors() {
        for (Monster monster : monsters) {
            monster.setStopMoving(true);
        }
        pacActor.removeSelf();
    }

    /*
    Check if PacMan has been hit or eaten all pills
     */
    private boolean checkPacmanWin() {
        boolean hasPacmanBeenHit;
        boolean hasPacmanEatAllPills;
        int maxPillsAndItems = countPillsAndItems();

        // loop to check if PacMan has been hit or eaten all pills
        do {
            hasPacmanBeenHit = hasPacmanHit();
            hasPacmanEatAllPills = pacActor.getNbPills() >= maxPillsAndItems;
            delay(10);
        } while (!hasPacmanBeenHit && !hasPacmanEatAllPills);
        return hasPacmanBeenHit;
    }

    /*
    Check if PacMan has been hit by monsters
     */
    private boolean hasPacmanHit() {
        for (Monster monster : monsters) {
            if (monster.getLocation().equals(pacActor.getLocation())) {
                return true;
            }
        }
        return false;
    }

    /*
    Display the end of game message
     */
    private void indicateGameEnd(GameCallback gameCallback, boolean hasPacmanBeenHit, Location loc) {
        String title = "";
        if (hasPacmanBeenHit) {
            grid.drawPacmanHit();
            title = "GAME OVER";
            addActor(new Actor("sprites/explosion3.gif"), loc);
        } else {
            grid.drawPacmanWin();
            title = "YOU WIN";
        }
        setTitle(title);
        gameCallback.endOfGame(title);
    }

    /*
    set up random seeds for monsters and PacMan
    set up pre-defined properties for the game
     */
    private void setUpGameProperties(Properties properties) {
        seed = Integer.parseInt(properties.getProperty("seed"));
        // set up monster
        for (Monster monster : monsters) {
            monster.setSeed(seed);
            monster.setSlowDown(3);
        }
        // set up pacman
        pacActor.setSeed(seed);
        pacActor.setSlowDown(3);

        addKeyRepeatListener(pacActor);
        setKeyRepeatPeriod(150);
    }

    /*
    set up auto test for PacMan, used for testing
     */
    private void setUpAutoTest(Properties properties) {
        pacActor.setPropertyMoves(properties.getProperty("PacMan.move"));
        pacActor.setAuto(Boolean.parseBoolean(properties.getProperty("PacMan.isAuto")));
    }

    /*
    switch version by reading the properties file
     */
    private void setupGameVersion(Properties properties) {
        switch (properties.getProperty("version")) {
            case "simple":
                gameVersion = new SimpleGame();
                break;
            case "multiverse":
                gameVersion = new MultiverseGame();
                break;
            default: gameVersion = new SimpleGame();
        }
    }

    public GameCallback getGameCallback() {
        return gameCallback;
    }

    protected int countPillsAndItems() {
        return entities.size() - iceCount;
    }

    /*
    get the location of visible pills and gold
     */
    public ArrayList<Location> getItemLocation() {
        ArrayList<Location> result = new ArrayList<>();
        for (StaticEntity entity : entities) {
            if (entity.isVisible()) { // pill is not visible
                result.add(entity.getLocation());
            }
        }
        return result;
    }

    public PacManGameGrid getGrid() {
        return grid;
    }

    /*
    Load static entity locations from properties file
     */
    private void setupPillAndItemsLocations() {
        ArrayList<Location> pillLocations = getPropertyLocation("Pills.location");
        ArrayList<Location> goldLocations = getPropertyLocation("Gold.location");
        // set up pills and gold according to the default map if no locations are provided
        for (int y = 0; y < nbVertCells; y++) {
            for (int x = 0; x < nbHorzCells; x++) {
                Location location = new Location(x, y);
                int cellType = grid.getCell(location);
                Pill pill;
                Gold gold;
                Ice ice;
                if (cellType == 1 && pillLocations == null){
                    pill = new Pill(location);
                    addActor(pill, location);
                    this.entities.add(pill);
                } else if (cellType == 3 && goldLocations == null) {
                    gold = new Gold(location);
                    addActor(gold, location);
                    this.entities.add(gold);
                } else if (cellType == 4) {
                    ice = new Ice(location);
                    addActor(ice, location);
                    this.entities.add(ice);
                    this.iceCount++;
                }
            }
        }
        // set up pills and gold according to the locations provided
        if (pillLocations != null) {
            for (Location location : pillLocations) {
                Pill pill = new Pill(location);
                addActor(pill, location);
                this.entities.add(pill);
            }
        }
        if (goldLocations != null) {
            for (Location location : goldLocations) {
                Gold gold = new Gold(location);
                addActor(gold, location);
                this.entities.add(gold);
            }
        }
        // set up pill and item locations
        for (StaticEntity entity : this.entities) {
            pillAndItemLocations.add(entity.getLocation());
        }
    }

    /*
    Remove item from the grid
     */
    public void removeItem(Location location) {
        for (StaticEntity entity : this.entities) {
            if (entity.getLocation().equals(location)) {
                entity.hide();
            }
        }
    }

    public int getNumHorzCells() {
        return nbHorzCells;
    }

    public int getNumVertCells() {
        return nbVertCells;
    }

    public PacVersion getGameVersion() { return gameVersion; }

    /*
    Notify monsters that PacMan has eaten a pill
     */
    public void notifyMonsterEatPill() {
        return;
    }

    /*
    Notify monsters that PacMan has eaten a gold
     */
    public void notifyMonsterEatGold() {
        for (Monster monster : monsters) {
            monster.getFurious(3);
        }
    }

    /*
    Notify monsters that PacMan has eaten an ice
     */
    public void notifyMonsterEatIce() {
        for (Monster monster : monsters) {
            monster.getFreeze(3);
        }
    }

    /*
    Return locations of a property from properties file
     */
    private ArrayList<Location> getPropertyLocation(String property) {
        String locationString = getProperties().getProperty(property);
        if (locationString == null) {
            return null;
        }
        ArrayList<Location> result = new ArrayList<>();
        String[] locations = locationString.split(";");
        for (String singleLocation: locations){
            String[] location = singleLocation.split(",");
            int x = Integer.parseInt(location[0]);
            int y = Integer.parseInt(location[1]);
            result.add(new Location(x, y));
        }
        return result;
    }

    /*
    Draw walls and entities on the grid
     */
    private void setupGrid() {
        grid.drawWalls();
        // draw entities
        GGBackground bg = getBg();
        for (StaticEntity entity: entities){
            if (entity instanceof Pill){
                bg.setPaintColor(Color.white);
                bg.fillCircle(toPoint(entity.getLocation()), entity.getRadius());
            }
            else if (entity instanceof Gold){
                bg.setPaintColor(Color.yellow);
                bg.fillCircle(toPoint(entity.getLocation()), entity.getRadius());
            }
            else if (entity instanceof Ice){
                bg.setPaintColor(Color.blue);
                bg.fillCircle(toPoint(entity.getLocation()), entity.getRadius());
            }
        }
    }

    public ArrayList<Location> getPillAndItemLocations() {
        return pillAndItemLocations;
    }
}
