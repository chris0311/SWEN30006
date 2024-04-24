// PacMan.java
// Simple PacMan implementation
package src;

import ch.aplu.jgamegrid.*;
import src.tiles.portal.Portal;
import src.tiles.portal.PortalColor;
import src.tiles.portal.PortalComposite;
import src.utility.GameCallback;

import java.awt.*;
import java.util.ArrayList;
import java.util.Properties;

public class Game extends GameGrid {
    private final static int nbHorzCells = 20;
    private final static int nbVertCells = 11;
    protected PacManGameGrid grid;

    protected PacActor pacActor = new PacActor(this);
    //    private Monster troll = new Monster(this, MonsterType.Troll);
//    private Monster tx5 = new Monster(this, MonsterType.TX5);
    private ArrayList<Location> pillAndItemLocations = new ArrayList<Location>();
    private ArrayList<Actor> iceCubes = new ArrayList<Actor>();
    private ArrayList<Actor> goldPieces = new ArrayList<Actor>();
    private GameCallback gameCallback;
    private Properties properties;
    private int seed = 30006;
    private ArrayList<Location> propertyPillLocations = new ArrayList<>();
    private ArrayList<Location> propertyGoldLocations = new ArrayList<>();
    private PortalComposite portals = new PortalComposite();
    private Location pacStartLocation = null;
    private Location pacDeadLocation = null;
    private MonsterComposite monsters = new MonsterComposite();
    private boolean hasPacmanBeenHit = false, hasPacmanEatAllPills = false;
    private GGBackground bg = getBg();
    private boolean win = false;

    public Game(GameCallback gameCallback, Properties properties, String map) {
        super(nbHorzCells, nbVertCells, 20, false);
        startGame(gameCallback, properties, map);
        stopGame(gameCallback);
        doPause();
    }

    private void stopGame(GameCallback gameCallback) {
        String title = "";
        if (hasPacmanBeenHit) {
            bg.setPaintColor(Color.red);
            title = "GAME OVER";
            addActor(new Actor("sprites/explosion3.gif"), pacDeadLocation);
            win = false;
        } else if (hasPacmanEatAllPills) {
            bg.setPaintColor(Color.yellow);
            title = "YOU WIN";
            win = true;
        }
        setTitle(title);
        gameCallback.endOfGame(title);
    }

    public GameCallback getGameCallback() {
        return gameCallback;
    }

    private void setupActorLocations() {
        int pacManX = this.pacStartLocation.getX();
        int pacManY = this.pacStartLocation.getY();
        addActor(pacActor, new Location(pacManX, pacManY));
    }

    private int countPillsAndItems() {
        int pillsAndItemsCount = 0;
        for (int y = 0; y < nbVertCells; y++) {
            for (int x = 0; x < nbHorzCells; x++) {
                Location location = new Location(x, y);
                int a = grid.getCell(location);
                if (a == 1 && propertyPillLocations.size() == 0) { // Pill
                    pillsAndItemsCount++;
                } else if (a == 3 && propertyGoldLocations.size() == 0) { // Gold
                    pillsAndItemsCount++;
                }
            }
        }
        if (propertyPillLocations.size() != 0) {
            pillsAndItemsCount += propertyPillLocations.size();
        }

        if (propertyGoldLocations.size() != 0) {
            pillsAndItemsCount += propertyGoldLocations.size();
        }

        return pillsAndItemsCount;
    }

    public ArrayList<Location> getPillAndItemLocations() {
        return pillAndItemLocations;
    }


    private void loadPillAndItemsLocations() {
        String pillsLocationString = properties.getProperty("Pills.location");
        if (pillsLocationString != null) {
            String[] singlePillLocationStrings = pillsLocationString.split(";");
            for (String singlePillLocationString : singlePillLocationStrings) {
                String[] locationStrings = singlePillLocationString.split(",");
                propertyPillLocations.add(new Location(Integer.parseInt(locationStrings[0]), Integer.parseInt(locationStrings[1])));
            }
        }

        String goldLocationString = properties.getProperty("Gold.location");
        if (goldLocationString != null) {
            String[] singleGoldLocationStrings = goldLocationString.split(";");
            for (String singleGoldLocationString : singleGoldLocationStrings) {
                String[] locationStrings = singleGoldLocationString.split(",");
                propertyGoldLocations.add(new Location(Integer.parseInt(locationStrings[0]), Integer.parseInt(locationStrings[1])));
            }
        }
    }

    private void setupPillAndItemsLocations() {
        for (int y = 0; y < nbVertCells; y++) {
            for (int x = 0; x < nbHorzCells; x++) {
                Location location = new Location(x, y);
                int a = grid.getCell(location);
                if (a == 1 && propertyPillLocations.size() == 0) {
                    pillAndItemLocations.add(location);
                }
                if (a == 3 && propertyGoldLocations.size() == 0) {
                    pillAndItemLocations.add(location);
                }
                if (a == 4) {
                    pillAndItemLocations.add(location);
                }
            }
        }


        if (propertyPillLocations.size() > 0) {
            for (Location location : propertyPillLocations) {
                pillAndItemLocations.add(location);
            }
        }
        if (propertyGoldLocations.size() > 0) {
            for (Location location : propertyGoldLocations) {
                pillAndItemLocations.add(location);
            }
        }
    }

    private void initGrid(GGBackground bg) {
        bg.clear(Color.gray);
//        bg.setPaintColor(Color.white);
        for (int y = 0; y < nbVertCells; y++) {
            for (int x = 0; x < nbHorzCells; x++) {
                bg.setPaintColor(Color.white);
                Location location = new Location(x, y);
                int a = grid.getCell(location);
//                System.out.println("a: " + a);
                if (a > 0) {
                    bg.fillCell(location, Color.lightGray);
                }
                if (a == 1) {
                    putPill(bg, location);
                }
                if (a == 3) {
                    putGold(bg, location);
                }
                if (a == 4) {
                    putIce(bg, location);
                }
                if (a == 5) {
                    this.pacStartLocation = location;
                }
                if (a == 6) {
                    Monster troll = new Monster(this, MonsterType.Troll);
                    addActor(troll, location);
                    monsters.addMonster(troll);
                }
                if (a == 7) {
                    Monster tx5 = new Monster(this, MonsterType.TX5);
                    addActor(tx5, location);
                    tx5.stopMoving(5);
                    monsters.addMonster(tx5);
                }
                if (a == 8) {
                    Portal portal = new Portal(PortalColor.WHITE, location);
                    portals.addPortal(portal);
                    addActor(portal, location);
                }
                if (a == 9) {
                    Portal portal = new Portal(PortalColor.YELLOW, location);
                    portals.addPortal(portal);
                    addActor(portal, location);
                }
                if (a == 10) {
                    Portal portal = new Portal(PortalColor.DARKGOLD, location);
                    portals.addPortal(portal);
                    addActor(portal, location);
                }
                if (a == 11) {
                    Portal portal = new Portal(PortalColor.DARKGRAY, location);
                    portals.addPortal(portal);
                    addActor(portal, location);
                }
            }
        }
    }

    private void putPill(GGBackground bg, Location location) {
        bg.fillCircle(toPoint(location), 5);
    }

    private void putGold(GGBackground bg, Location location) {
        bg.setPaintColor(Color.yellow);
        bg.fillCircle(toPoint(location), 5);
        Actor gold = new Actor("sprites/gold.png");
        this.goldPieces.add(gold);
        addActor(gold, location);
    }

    private void putIce(GGBackground bg, Location location) {
        bg.setPaintColor(Color.blue);
        bg.fillCircle(toPoint(location), 5);
        Actor ice = new Actor("sprites/ice.png");
        this.iceCubes.add(ice);
        addActor(ice, location);
    }

    public void removeItem(String type, Location location) {
        switch (type) {
            case "gold" -> {
                for (Actor item : this.goldPieces) {
                    if (location.getX() == item.getLocation().getX() && location.getY() == item.getLocation().getY()) {
                        item.hide();
                    }
                }
                // remove from pillAndItemLocations
                for (Location item : this.pillAndItemLocations) {
                    if (location.getX() == item.getX() && location.getY() == item.getY()) {
                        this.pillAndItemLocations.remove(item);
                        break;
                    }
                }
            }
            case "ice" -> {
                for (Actor item : this.iceCubes) {
                    if (location.getX() == item.getLocation().getX() && location.getY() == item.getLocation().getY()) {
                        item.hide();
                    }
                }
                // remove from pillAndItemLocations
                for (Location item : this.pillAndItemLocations) {
                    if (location.getX() == item.getX() && location.getY() == item.getY()) {
                        this.pillAndItemLocations.remove(item);
                        break;
                    }
                }
            }
            case "pill" -> {
                // remove from pillAndItemLocations
                for (Location item : this.pillAndItemLocations) {
                    if (location.getX() == item.getX() && location.getY() == item.getY()) {
                        this.pillAndItemLocations.remove(item);
//                        System.out.println("removed location: " + item.getX() + ", " + item.getY());
                        break;
                    }
                }
            }
        }

    }

    public int getNumHorzCells() {
        return this.nbHorzCells;
    }

    public int getNumVertCells() {
        return this.nbVertCells;
    }

    public PortalComposite getPortals() {
        return portals;
    }

    public void startGame(GameCallback gameCallback, Properties properties, String map) {
        //Setup game
        this.gameCallback = gameCallback;
        this.properties = properties;
        this.grid = new PacManGameGrid(nbHorzCells, nbVertCells, map);
        setSimulationPeriod(100);
        setTitle("[PacMan in the Multiverse]");

        //Setup for auto test
//        pacActor.setPropertyMoves(properties.getProperty("PacMan.move"));
        pacActor.setAuto(Boolean.parseBoolean(properties.getProperty("PacMan.isAuto")));
//        loadPillAndItemsLocations();

//        GGBackground bg = getBg();
        initGrid(bg);

        //Setup Random seeds
        seed = Integer.parseInt(properties.getProperty("seed"));
        setupActorLocations();
        pacActor.setSeed(seed);
        monsters.setSeed(seed);
        addKeyRepeatListener(pacActor);
        setKeyRepeatPeriod(150);
        monsters.setSlowDown(3);
        pacActor.setSlowDown(3);
//        tx5.stopMoving(5);


        //Run the game
        doRun();
        show();
        // Loop to look for collision in the application thread
        // This makes it improbable that we miss a hit
//        boolean hasPacmanBeenHit = false;
//        boolean hasPacmanEatAllPills = false;
        setupPillAndItemsLocations();
        int maxPillsAndItems = countPillsAndItems();

        do {
            hasPacmanBeenHit = monsters.checkCollision(pacActor);
            hasPacmanEatAllPills = pacActor.getNbPills() >= maxPillsAndItems;
            delay(10);
        } while (!hasPacmanBeenHit && !hasPacmanEatAllPills);
        delay(120);

        pacDeadLocation = pacActor.getLocation();
        monsters.setStopMoving(true);
        pacActor.removeSelf();
        monsters.removeMonsters();
    }

    public boolean getWin() {
        return win;
    }
}
