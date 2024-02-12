package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {
    private int width;
    private int height;

    private TETile[][] world;
    private Point[][] points;
    private long seed;
    private Random rand;
    private static final int MAX_SEARCH_TIMES = 200;
    private String action;
    private String input;

    public World(int width, int height, String input) {
        this.width = width;
        this.height = height;
        this.world = new TETile[width][height];
        this.action = extractAction(input);

        String digits = input.replaceAll("[^0-9]", "");
        this.seed = Long.parseLong(digits);
        this.rand = new Random(seed);
        this.points = new Point[width][height];
        this.input = input;
    }

    private String extractAction(String input) {
        String theInput = input.toLowerCase();
        StringBuilder theAction = new StringBuilder();
        int startIndex = input.indexOf("s") + 1;
        for (int i = startIndex; i < input.length(); i++) {
            theAction.append(theInput.charAt(i));
        }
        return theAction.toString();
    }

    public void createWorld() {
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                Point point = new Point(x, y, Tileset.NOTHING);
                point.curveTo(world, point.getType());
                points[x][y] = point;
            }
        }
    }

    public long getSeed() {
        return seed;
    }

    public TETile[][] getWorld() {
        return world;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Random getRand() {
        return rand;
    }

    /** Generates rooms in the world.
     * 1. Iterate MAX_SEARCH_TIMES times
     * 2. Get random x and y (must be odd)
     * 3. Draw room at x and y
     */
    public void generateRooms() {
        Random myRand = new Random(getSeed());
        for (int i = 0; i < MAX_SEARCH_TIMES; i++) {
            int x = Utils.getRandomX(myRand, this);
            int y = Utils.getRandomY(myRand, this);
            Room.drawRoom(this, x, y);
        }
    }

    /** Generates a maze in the world.
     * 1. Curves any odd points thai are NOTHING to FLOWER.
     * 2. Fill the maze
     */
    public void generateMaze() {
        TETile mazeType = Tileset.FLOOR;
        for (int x = 1; x < width; x += 2) {
            for (int y = 1; y < height; y += 2) {
                Point point = Point.getPoint(this, x, y);
                if (point.getType() == Tileset.NOTHING) {
                    point.curveTo(world, mazeType);
                }
            }
        }

        Maze.fillMaze(this, mazeType);
    }

    public Point[][] getPoints() {
        return points;
    }


    /** Generates gates in the world.
     * 1. Find all possible gates in the world.
     * 2. Save successive possible gates as a whole in a "vector".
     * 3. Iterate the "vector", randomly choose a gate and draw it.
     */
    public void generateGates() {
        List<ArrayList<Gate>> possibleGates = Gate.generatePossibleGates(this);
        for (ArrayList<Gate> adjacentGates : possibleGates) {
            int randomIndex = rand.nextInt(adjacentGates.size());
            Point point = adjacentGates.get(randomIndex);
            Gate.drawGate(this, point.getX(), point.getY());
            Gate.addDrawnGates(point);
        }
    }

    public void generateWall() {
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                Point point = Point.getPoint(this, x, y);
                if (!point.isType(Tileset.NOTHING) && !point.isType(Tileset.WALL)) {
                    Wall.drawWall(this, point.getX(), point.getY());
                }
            }
        }
    }

    public TETile[][] generateCompleteWorld() {
        createWorld();
        generateRooms();
        generateMaze();
        generateGates();
        generateWall();
        generateAvatar();
        save();
        return getWorld();
    }

    public Avatar generateAvatar() {
        Point gate = Gate.getRandomGate(rand);
        Avatar avatar = new Avatar(gate.getX(), gate.getY(), Tileset.LOCKED_DOOR);
        avatar.drawAvatar(this);
        return avatar;
    }

    public String getAction() {
        return action;
    }

    public void save() {
        File file = Utils.join("byow/Core", "save.txt");
        StringBuilder inputToSave = new StringBuilder();
        for (int i = 0; i < this.input.length(); i++) {
            char c = this.input.charAt(i);
            if (c == ':') {
                break;
            }
            inputToSave.append(c);
        }
        Utils.writeContents(file, inputToSave.toString());
    }
}
