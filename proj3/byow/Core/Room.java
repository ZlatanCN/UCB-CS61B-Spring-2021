package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;
import java.util.TreeMap;

public class Room {
    private int width;
    private int height;
    private static final TETile FLOOR_TYPE = Tileset.GRASS;

    public Room(Random rand) {
        width = Utils.getRandomLength(rand, 10);
        height = Utils.getRandomLength(rand, 10);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public static TETile getFloorType() {
        return FLOOR_TYPE;
    }

    /** Draws a room in the world. */
    public static void drawRoom(World world, int x, int y) {
        Room room = new Room(world.getRand());
        int width = room.getWidth();
        int height = room.getHeight();
        TETile[][] myWorld = world.getWorld();

        if (isRoomValid(world, x, y, width, height)) {
            for (int i = x; i < x + width; i += 1) {
                for (int j = y; j < y + height; j += 1) {
                    Point point = Point.getPoint(world, i, j);
                    point.curveTo(myWorld, getFloorType());
                }
            }
        }
    }

    private static boolean isRoomValid(World world, int x, int y, int width, int height) {
        if (!isOutOfBound(world, x, y, width, height) && !isOverlap(world, x, y, width, height)) {
            return true;
        }
        return false;
    }

    private static boolean isOutOfBound(World world, int x, int y, int width, int height) {
        if (x + width > world.getWidth() || y + height > world.getHeight()) {
            return true;
        }
        return false;
    }

    private static boolean isOverlap(World world, int x, int y, int width, int height) {
        TETile[][] myWorld = world.getWorld();
        for (int i = x; i < x + width; i += 1) {
            for (int j = y; j < y + height; j += 1) {
                Point point = Point.getPoint(world, i, j);
                if (point.getType() != Tileset.NOTHING) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isDiagonal(World world, Point point, TETile roomType) {
        TreeMap<String, Point> neighbors = point.getFourNeighbors(world);
        Point east = neighbors.get("east");
        Point south = neighbors.get("south");
        Point west = neighbors.get("west");
        Point north = neighbors.get("north");

        boolean isEND = east.isType(Tileset.NOTHING) && north.isType(Tileset.NOTHING)
                && south.isType(roomType) && west.isType(roomType);
        boolean isESD = east.isType(Tileset.NOTHING) && south.isType(Tileset.NOTHING)
                && north.isType(roomType) && west.isType(roomType);
        boolean isWND = west.isType(Tileset.NOTHING) && north.isType(Tileset.NOTHING)
                && south.isType(roomType) && east.isType(roomType);
        boolean isWSD = west.isType(Tileset.NOTHING) && south.isType(Tileset.NOTHING)
                && north.isType(roomType) && east.isType(roomType);

        return isEND || isESD || isWND || isWSD;
    }
}
