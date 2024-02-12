package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.*;

public class Maze {
    /** Fills the world with a maze. */
    public static void fillMaze(World world, TETile mazeType) {
        TETile[][] myWorld = world.getWorld();
        for (int i = 1; i < myWorld.length - 1; i += 2) {
            for (int j = 1; j < myWorld[0].length - 1; j += 2) {
                Point startPoint = Point.getPoint(world, i, j);
                if (startPoint.isType(mazeType) && !startPoint.isVisited()) {
                    growMaze(world, startPoint, mazeType, world.getRand());
                }
            }
        }

        pruneMaze(world, mazeType);
    }

    /** Grows the maze randomly.
     * 1. Mark the current point as visited.
     * 2. Get the four neighbors of the current point.
     * 3. Randomly shuffle the visiting order of the neighbors.
     * 4. Visit the neighbors in the shuffled order iteratively.
     * 5. If
     */
    private static void growMaze(World world, Point currentPoint, TETile curveType, Random rand) {
        currentPoint.visit();

        TreeMap<String, Point> neighbors = currentPoint.getFourNeighbors(world);
        List<String> directions = new ArrayList<>(neighbors.keySet());
        Collections.shuffle(directions, rand);

        for (String direction : directions) {
            Point neighbor = neighbors.get(direction);
            Point nextNeighbor = neighbor.getFourNeighbors(world).get(direction);
            // Null means the neighbor is out of bound.
            boolean neighborCanBeVisited = neighbor != null
                    && !neighbor.isVisited()
                    && nextNeighbor != null
                    && nextNeighbor.isType(curveType)
                    && !nextNeighbor.isVisited();
            if (neighborCanBeVisited) {
                neighbor.visit();
                neighbor.curveTo(world.getWorld(), curveType);
                growMaze(world, nextNeighbor, curveType, rand);
            }
        }
    }

    public static void pruneMaze(World world, TETile mazeType) {
        TETile[][] myWorld = world.getWorld();
        for (int i = 1; i < myWorld.length - 1; i += 1) {
            for (int j = 1; j < myWorld[0].length - 1; j += 1) {
                Point point = Point.getPoint(world, i, j);
                if (point.isType(mazeType)) {
                    TreeMap<String, Point> neighbors = point.getFourNeighbors(world);
                    int count = 0;
                    for (Point neighbor : neighbors.values()) {
                        if (neighbor.isType(Tileset.NOTHING)) {
                            count += 1;
                        }
                    }
                    if (count >= 3) {
                        point.curveTo(myWorld, Tileset.NOTHING);
                    }
                }
            }
        }
    }
}
