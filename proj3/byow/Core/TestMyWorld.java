package byow.Core;

import byow.TileEngine.TETile;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestMyWorld {

    @Test
    public void equalsTest() {
        Engine engine = new Engine();

        TETile[][] world1 = engine.interactWithInputString("N100S");
        TETile[][] world2 = engine.interactWithInputString("N100S");
        assertArrayEquals(world1, world2);

        TETile[][] world3 = engine.interactWithInputString("n5197880843569031643s");
        TETile[][] world4 = engine.interactWithInputString("n5197880843569031643s");
        TETile[][] world5 = engine.interactWithInputString("n5197880843569031643s");
        assertArrayEquals(world3, world4);
    }
}
