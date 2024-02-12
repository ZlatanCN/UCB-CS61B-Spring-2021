package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

public class Gate extends Point {
    private static final TETile TYPE = Tileset.LOCKED_DOOR;
    private static final ArrayList<Gate> CONNECTED_GATES = new ArrayList<>();
    private static final ArrayList<Point> DRAWN_GATES = new ArrayList<>();

    public Gate(int x, int y) {
        super(x, y, TYPE);
    }

    public static List<ArrayList<Gate>> generatePossibleGates(World world) {
        List<ArrayList<Gate>> possibleGates = new ArrayList<>();
        for (int i = 2; i < world.getWidth() - 2; i += 1) {
            for (int j = 2; j < world.getHeight() - 2; j += 1) {
                Point point = Point.getPoint(world, i, j);
                if (isPossibleGate(world, point)) {
                    ArrayList<Gate> adjacentGates = getAdjacentGates(world, point);
                    possibleGates.add(adjacentGates);
                }
            }
        }
        return possibleGates;
    }

    private static boolean isPossibleGate(World world, Point point) {
        if (point.getX() < 2 || point.getX() >= world.getWidth() - 2
                || point.getY() < 2 || point.getY() >= world.getHeight() - 2) {
            return false;
        }

        if (!point.isType(Tileset.NOTHING)) {
            return false;
        }

        if (isConnected(point)) {
            return false;
        }

        TreeMap<String, Point> neighbors = point.getFourNeighbors(world);
        Point east = neighbors.get("east");
        Point south = neighbors.get("south");
        Point west = neighbors.get("west");
        Point north = neighbors.get("north");

        boolean isLinkNS = !north.isType(Tileset.NOTHING)
                && !south.isType(Tileset.NOTHING)
                && !north.isType(south.getType())
                && !Room.isDiagonal(world, north, Room.getFloorType())
                && !Room.isDiagonal(world, south, Room.getFloorType());
        boolean isLinkEW = !east.isType(Tileset.NOTHING)
                && !west.isType(Tileset.NOTHING)
                && !east.isType(west.getType())
                && !Room.isDiagonal(world, east, Room.getFloorType())
                && !Room.isDiagonal(world, west, Room.getFloorType());

        if (isLinkEW || isLinkNS) {
            return true;
        }
        return false;
    }

    private static ArrayList<Gate> getAdjacentGates(World world, Point point) {
        ArrayList<Gate> adjacentGates = new ArrayList<>();
        Gate gate = new Gate(point.getX(), point.getY());
        gate.connect();
        adjacentGates.add(gate);

        Point east = point.getFourNeighbors(world).get("east");
        Point north = point.getFourNeighbors(world).get("north");

        while (isPossibleGate(world, east)
                && !isConnected(east)
                && east.isType(Tileset.NOTHING)) {
            adjacentGates.add(new Gate(east.getX(), east.getY()));
            connect(east);
            east = east.getFourNeighbors(world).get("east");
        }

        while (isPossibleGate(world, north)
                && !isConnected(north)
                && north.isType(Tileset.NOTHING)) {
            adjacentGates.add(new Gate(north.getX(), north.getY()));
            connect(north);
            north = north.getFourNeighbors(world).get("north");
        }

        return adjacentGates;
    }

    private void connect() {
        CONNECTED_GATES.add(this);
    }

    private static boolean isConnected(Point point) {
        Gate gate = new Gate(point.getX(), point.getY());
        return CONNECTED_GATES.contains(gate);
    }

    private static void connect(Point point) {
        Gate gate = new Gate(point.getX(), point.getY());
        gate.connect();
    }

    public static void drawGate(World world, int x, int y) {
        Point point = Point.getPoint(world, x, y);
        point.curveTo(world.getWorld(), TYPE);
    }

    public static Point getRandomGate(Random rand) {
        int randomIndex = rand.nextInt(DRAWN_GATES.size());
        return DRAWN_GATES.get(randomIndex);
    }

    public static void addDrawnGates(Point gate) {
        DRAWN_GATES.add(gate);
    }

    public static void clear() {
        CONNECTED_GATES.clear();
        DRAWN_GATES.clear();
    }
}
