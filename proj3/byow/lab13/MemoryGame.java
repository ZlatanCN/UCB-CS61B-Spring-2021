package byow.lab13;

import byow.Core.RandomUtils;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    /** The width of the window of this game. */
    private int width;
    /** The height of the window of this game. */
    private int height;
    /** The current round the user is on. */
    private int round;
    /** The Random object used to randomly generate Strings. */
    private Random rand;
    /** Whether or not the game is over. */
    private boolean gameOver;
    /** Whether or not it is the player's turn. Used in the last section of the
     * spec, 'Helpful UI'. */
    private boolean playerTurn;
    /** The characters we generate random Strings from. */
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    /** Encouraging phrases. Used in the last section of the spec, 'Helpful UI'. */
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        long seed = Long.parseLong(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, long seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        this.rand = new Random(seed);
        this.round = 1;
        this.gameOver = false;
        this.playerTurn = false;
    }

    /** Generate a random string of length n.
     * 1. Create a new StringBuilder.
     * 2. Repeat n times:
     *    Randomly choose characters and append them to the StringBuilder.
     */
    public String generateRandomString(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            int randomIndex = rand.nextInt(CHARACTERS.length);
            String randomLetter = Character.toString(CHARACTERS[randomIndex]);
            sb.append(randomLetter);
        }
        return sb.toString();
    }

    /** Display the given string in the center of the screen.
     * 1. Clear the screen.
     * 1. Set the font to be a large, bold font.
     * 2. Set the pen color to be white.
     * 4. Display the string in the center of the screen.
     */
    public void drawFrame(String s) {
        StdDraw.clear(Color.BLACK);
        Font font = new Font("Arial", Font.BOLD, 30);
        StdDraw.setFont(font);
        Color color = Color.WHITE;
        StdDraw.setPenColor(color);
        StdDraw.text(width / 2, height / 2, s);
        StdDraw.show();
    }

    /** Flash each character in the string letters, making sure to blank the screen between letters.
     * 1. For each character in letters:
     *    a. Display the character.
     *    b. Pause for 1000 milliseconds.
     *    c. Clear the screen.
     *    d. Pause for 500 milliseconds.
     */
    public void flashSequence(String letters) {
        for (int i = 0; i < letters.length(); i++) {
            String letter = Character.toString(letters.charAt(i));
            drawFrame(letter);
            StdDraw.pause(1000);
            drawFrame("");
            StdDraw.pause(500);
        }
    }

    /** Read n letters of player input.
     * 1. Create a new StringBuilder.
     * 2. Repeat n times:
     *    a. If a key is being pressed, append the key to the StringBuilder.
     *    b. Display the string in the center of the screen.
     * 3. Return the StringBuilder as a string.
     */
    public String solicitNCharsInput(int n) {
        StringBuilder sb = new StringBuilder();
        while (sb.length() < n) {
            if (StdDraw.hasNextKeyTyped()) {
                sb.append(StdDraw.nextKeyTyped());
                drawFrame(sb.toString());
            }
        }
        return sb.toString();
    }

    /** Start the game.
     * 1. Display a start screen.
     * 2. Start the first round.
     * 3. Repeat the following until the game is over:
     *    a. If it is the player's turn, solicit input.
     *    b. Otherwise, flash a random string and pause for 1000 milliseconds.
     *    c. If the input is correct, increment the round.
     *    d. Otherwise, end the game.
     * 4. Display a game over screen.
     */
    public void startGame() {
        drawFrame("Round: " + round);
        while (!gameOver) {
            playerTurn = false;
            drawFrame("Round: " + round);
            StdDraw.pause(1000);

            String randomString = generateRandomString(round);
            flashSequence(randomString);

            String inputString = solicitNCharsInput(round);
            StdDraw.pause(1000);
            if (inputString.equals(randomString)) {
                round += 1;
            } else {
                gameOver = true;
                drawFrame("Game Over! You made it to round: " + round);
            }
        }
    }

}
