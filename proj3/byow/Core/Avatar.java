package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

public class Avatar {
    private int x;
    private int y;
    private TETile originalType;

    public Avatar(int x, int y, TETile originalType) {
        this.x = x;
        this.y = y;
        this.originalType = originalType;
    }

    public void drawAvatar(World world) {
        String action = world.getAction();
        TETile[][] myWorld = world.getWorld();
        myWorld[x][y] = Tileset.AVATAR;
        if (action.equals("")) {
            return;
        }
        for (int i = 0; i < action.length(); i++) {
            char c = action.charAt(i);
            if (c == ':') {
                if (action.charAt(i + 1) == 'q') {
                    world.save();
                    break;
                }
            } else {
                moveTo(c, myWorld);
            }
        }
    }

    public void moveTo(char c, TETile[][] myWorld) {
        if (originalType == Tileset.AVATAR) {
            originalType = Tileset.LOCKED_DOOR;
        }
        if (c == 'w') { // north
            moveToNorth(myWorld);
        } else if (c == 'a') { // west
            moveToWest(myWorld);
        } else if (c == 's') { // south
            moveToSouth(myWorld);
        } else if (c == 'd') { // east
            moveToEast(myWorld);
        }
    }

    private void moveToNorth(TETile[][] myWorld) {
        if (myWorld[x][y + 1] != Tileset.WALL) {
            myWorld[x][y] = originalType;
            originalType = myWorld[x][y + 1];
            myWorld[x][y + 1] = Tileset.AVATAR;
            y += 1;
        }
    }

    private void moveToWest(TETile[][] myWorld) {
        if (myWorld[x - 1][y] != Tileset.WALL) {
            myWorld[x][y] = originalType;
            originalType = myWorld[x - 1][y];
            myWorld[x - 1][y] = Tileset.AVATAR;
            x -= 1;
        }
    }

    private void moveToSouth(TETile[][] myWorld) {
        if (myWorld[x][y - 1] != Tileset.WALL) {
            myWorld[x][y] = originalType;
            originalType = myWorld[x][y - 1];
            myWorld[x][y - 1] = Tileset.AVATAR;
            y -= 1;
        }
    }

    private void moveToEast(TETile[][] myWorld) {
        if (myWorld[x + 1][y] != Tileset.WALL) {
            myWorld[x][y] = originalType;
            originalType = myWorld[x + 1][y];
            myWorld[x + 1][y] = Tileset.AVATAR;
            x += 1;
        }
    }

    public static Avatar getAvatar(TETile[][] myWorld) {
        for (int x = 0; x < myWorld.length; x += 1) {
            for (int y = 0; y < myWorld[0].length; y += 1) {
                if (myWorld[x][y] == Tileset.AVATAR) {
                    return new Avatar(x, y, myWorld[x][y]);
                }
            }
        }
        return null;
    }
}
