package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.TreeMap;

public class Wall {
    public static void drawWall(World world, int x, int y) {
        TETile[][] myWorld = world.getWorld();
        TreeMap<String, Point> neighbors = Point.getPoint(world, x, y).getEightNeighbors(world);

        for (Point neighbor : neighbors.values()) {
            if (neighbor != null && neighbor.isType(Tileset.NOTHING)) {
                neighbor.curveTo(myWorld, Tileset.WALL);
            }
        }
    }
}
