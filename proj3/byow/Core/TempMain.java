package byow.Core;

import byow.TileEngine.TERenderer;

public class TempMain {
    public static void main(String[] args) {
        World world = new World(81, 51, "N888S");
        world.createWorld();
        world.generateRooms();
        world.generateMaze();
        world.generateGates();
        world.generateWall();
        TERenderer ter = new TERenderer();
        ter.initialize(81, 51);
        ter.renderFrame(world.getWorld());
    }
}
