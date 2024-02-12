package byow.Core;

import byow.TileEngine.TETile;
import java.util.TreeMap;


public class Point {
    private int x;
    private int y;
    private TETile type;
    private boolean isVisited;

    public Point(int x, int y, TETile type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.isVisited = false;
    }

    /** Use TreeMap to store the neighbors of the current point, then return the TreeMap.
     * 1. The key is the direction of the neighbor.
     * 2. The value is the neighbor point.
     */
    public TreeMap<String, Point> getFourNeighbors(World world) {
        TreeMap<String, Point> neighbors = new TreeMap<>();
        neighbors.put("east", Point.getPoint(world, x + 1, y));
        neighbors.put("south", Point.getPoint(world, x, y - 1));
        neighbors.put("west", Point.getPoint(world, x - 1, y));
        neighbors.put("north", Point.getPoint(world, x, y + 1));
        return neighbors;
    }

    public void curveTo(TETile[][] world, TETile curveType) {
        world[x][y] = curveType;
        this.type = curveType;
    }

    /** Get the point at (x, y) in the world. Return null if the point is out of bound. */
    public static Point getPoint(World world, int x, int y) {
        TETile[][] myWorld = world.getWorld();
        if (x < 0 || x >= myWorld.length || y < 0 || y >= myWorld[0].length) {
            return null;
        }
        Point[][] points = world.getPoints();
        return points[x][y];
    }

    public TETile getType() {
        return this.type;
    }

    public void visit() {
        this.isVisited = true;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public boolean isType(TETile typeToCompare) {
        return this.type.equals(typeToCompare);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public TreeMap<String, Point> getEightNeighbors(World world) {
        TreeMap<String, Point> neighbors = new TreeMap<>();
        neighbors.put("east", Point.getPoint(world, x + 1, y));
        neighbors.put("south", Point.getPoint(world, x, y - 1));
        neighbors.put("west", Point.getPoint(world, x - 1, y));
        neighbors.put("north", Point.getPoint(world, x, y + 1));
        neighbors.put("northeast", Point.getPoint(world, x + 1, y + 1));
        neighbors.put("southeast", Point.getPoint(world, x + 1, y - 1));
        neighbors.put("southwest", Point.getPoint(world, x - 1, y - 1));
        neighbors.put("northwest", Point.getPoint(world, x - 1, y + 1));
        return neighbors;
    }
}
