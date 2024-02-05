package byow.lab12;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 100;
    private static final int HEIGHT = 100;
    private static final TETile[][] WORLD = new TETile[WIDTH][HEIGHT];
    private static int sizeOfHex;
    private static int widthOfHex;

    public HexWorld(int size) {
        createHexWorld();
        sizeOfHex = size;
        widthOfHex = 3 * size - 2;
    }

    private static class Hexagon {
        private int size;
        private TETile type;
        private int height;
        private int width;

        private Hexagon(int size, TETile type) {
            this.size = size;
            this.type = type;
            this.height = 2 * size;
            this.width = 3 * size - 2;
        }

        /** Draws the hexagon at the given position.
         * 1. Check if the position is legal
         * 2. Draw the hexagon through iteration (from bottom to top)
         */
        private void draw(int x, int y) {
            checkPosition(x, y, height);
            drawRows(x, y, height);
        }

        private void checkPosition(int x, int y, int height) {
            boolean isXLegal = x - size >= 0 && WIDTH - x >= size - 1;
            boolean isYLegal = y >= 1 && HEIGHT - y >= height - 1;

            if (!isXLegal || !isYLegal) {
                throw new IllegalArgumentException("The position is illegal.");
            }
        }

        private void drawRows(int x, int y, int height) {
            int endY = y + height - 1;
            int tempWidth = size;
            while (y <= endY) {
                drawOneRow(x, y, tempWidth);
                if (y < endY - size) {
                    x -= 1;
                    tempWidth += 2;
                } else if (y > endY - size) {
                    x += 1;
                    tempWidth -= 2;
                }
                y += 1;
            }
        }

        private void drawOneRow(int x, int y, int width) {
            int endX = x + width - 1;
            while (x <= endX) {
                WORLD[x - 1][y - 1] = type;
                x += 1;
            }
        }
    }

    public static void createHexWorld() {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                WORLD[x][y] = Tileset.NOTHING;
            }
        }
    }

    /** Draw hexagons in the world. From left to right, bottom to top */
    public static void drawHexagons(int x, int y, int centerSize, int edgeSize) {
        boolean visitedCenter = false;
        int minSize = edgeSize;
        while (edgeSize >= minSize) {
            drawOneColumn(x, y, edgeSize);
            x += 2 * sizeOfHex - 1;
            if (edgeSize < centerSize && !visitedCenter) {
                y -= sizeOfHex;
                edgeSize += 1;
            } else if (edgeSize == centerSize) {
                visitedCenter = true;
                y += sizeOfHex;
                edgeSize -= 1;
            } else {
                y += sizeOfHex;
                edgeSize -= 1;
            }
        }
    }

    private static void drawOneColumn(int x, int y, int num) {
        while (num > 0) {
            Hexagon hexagon = new Hexagon(sizeOfHex, randomTile());
            hexagon.draw(x, y);
            num -= 1;
            y += 2 * sizeOfHex;
        }
    }

    private static TETile randomTile() {
        Random random = new Random();
        int tileNum = random.nextInt(11);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.UNLOCKED_DOOR;
            case 3: return Tileset.AVATAR;
            case 4: return Tileset.FLOOR;
            case 5: return Tileset.GRASS;
            case 6: return Tileset.LOCKED_DOOR;
            case 7: return Tileset.MOUNTAIN;
            case 8: return Tileset.SAND;
            case 9: return Tileset.TREE;
            case 10: return Tileset.WATER;
            default: return Tileset.NOTHING;
        }
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        new HexWorld(2);
        drawHexagons(35, 35, 7, 2);

        ter.renderFrame(WORLD);
    }
}
