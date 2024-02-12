package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;

import java.io.File;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 81;
    public static final int HEIGHT = 51;

    public Engine() {
        //ter.initialize(WIDTH, HEIGHT);
    }

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        drawMenu();
        char key = ' ';
        String seed = "";
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                key = StdDraw.nextKeyTyped();
                if (key == 'n' || key == 'N') {
                    seed = drawNewGame();
                    break;
                } else if (key == 'l' || key == 'L') {
                    drawLoadGame();
                    seed = Utils.readContentsAsString(Utils.join("byow/Core", "save.txt"));
                    break;
                } else if (key == 'q' || key == 'Q') {
                    System.exit(0);
                }
            }
        }
        TETile[][] myWorld = interactWithInputString(seed);
        StringBuilder input = new StringBuilder(seed);
        Avatar avatar = Avatar.getAvatar(myWorld);
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char action = StdDraw.nextKeyTyped();
                if (action == 'q') {
                    File save = Utils.join("byow/Core", "save.txt");
                    Utils.writeContents(save, input.toString());
                    System.exit(0);
                }
                avatar.moveTo(action, myWorld);
                input.append(action);
            }
            ter.renderFrame(myWorld);
        }
    }

    private void drawMenu() {
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT * 3 / 4, "CS61B: Build Your Own World (BYOW)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "New Game (N)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 2, "Load Game (L)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2 - 4, "Quit (Q)");
        StdDraw.show();
    }

    private String drawNewGame() {
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "Please enter a seed followed by 'S'");
        StdDraw.show();
        StringBuilder seed = new StringBuilder();
        char key = ' ';
        while (key != 's' && key != 'S') {
            if (StdDraw.hasNextKeyTyped()) {
                key = StdDraw.nextKeyTyped();
                seed.append(key);
                StdDraw.clear(StdDraw.BLACK);
                StdDraw.text(WIDTH / 2, HEIGHT / 2, "Please enter a seed followed by 'S'");
                StdDraw.text(WIDTH / 2, HEIGHT / 2 - 2, seed.toString());
                StdDraw.show();
                StdDraw.pause(500);
            }
        }
        return "N" + seed.toString();
    }

    private void drawLoadGame() {
        StdDraw.setPenColor(StdDraw.WHITE);
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.text(WIDTH / 2, HEIGHT / 2, "Loading last game...");
        StdDraw.show();
        StdDraw.pause(1000);
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        if (input.charAt(0) == 'L' || input.charAt(0) == 'l') {
            File oldWorld = Utils.join("byow/Core", "save.txt");
            if (!oldWorld.exists() || oldWorld.length() == 0) {
                System.exit(0);
            }
            input = Utils.readContentsAsString(oldWorld) + input.substring(1);
        }
        World world = new World(WIDTH, HEIGHT, input);
        TETile[][] finalWorldFrame = world.generateCompleteWorld();
        //ter.renderFrame(world.getWorld());
        return finalWorldFrame;
    }

    public static void main(String[] args) {
        Engine engine = new Engine();
        TETile[][] myWorld = engine.interactWithInputString("n5197880843569031643s");
        System.out.println(myWorld.equals(engine.interactWithInputString("n5197880843569031643s")));
        //engine.interactWithKeyboard();
    }
}
