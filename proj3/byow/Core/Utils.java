package byow.Core;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class Utils {
    public static int getRandomX(Random rand, World world) {
        int x = rand.nextInt(world.getWidth());
        if (x % 2 == 0) {
            x += 1;
        }
        return x;
    }

    public static int getRandomY(Random rand, World world) {
        int y = rand.nextInt(world.getHeight());
        if (y % 2 == 0) {
            y += 1;
        }
        return y;
    }

    public static int getRandomLength(Random rand, int bound) {
        int length = rand.nextInt(bound) + 2;
        if (length == 3) {
            length += 2;
        }
        if (length % 2 == 0) {
            length += 3;
        }
        return length;
    }

    /** Return the concatentation of FIRST and OTHERS into a File designator,
     *  analogous to the {@link java.nio.file.Paths#get(String, String[])}
     *  method. */
    public static File join(String first, String... others) {
        return Paths.get(first, others).toFile();
    }

    /** Return the concatentation of FIRST and OTHERS into a File designator,
     *  analogous to the {@link java.nio.file.Paths#get(String, String[])}
     *  method. */
    public static File join(File first, String... others) {
        return Paths.get(first.getPath(), others).toFile();
    }

    /** Write the result of concatenating the bytes in CONTENTS to FILE,
     *  creating or overwriting it as needed.  Each object in CONTENTS may be
     *  either a String or a byte array.  Throws IllegalArgumentException
     *  in case of problems. */
    public static void writeContents(File file, Object... contents) {
        try {
            if (file.isDirectory()) {
                throw
                        new IllegalArgumentException("cannot overwrite directory");
            }
            BufferedOutputStream str =
                    new BufferedOutputStream(Files.newOutputStream(file.toPath()));
            for (Object obj : contents) {
                if (obj instanceof byte[]) {
                    str.write((byte[]) obj);
                } else {
                    str.write(((String) obj).getBytes(StandardCharsets.UTF_8));
                }
            }
            str.close();
        } catch (IOException | ClassCastException excp) {
            throw new IllegalArgumentException(excp.getMessage());
        }
    }

    /** Return the entire contents of FILE as a byte array.  FILE must
     *  be a normal file.  Throws IllegalArgumentException
     *  in case of problems. */
    public static byte[] readContents(File file) {
        if (!file.isFile()) {
            throw new IllegalArgumentException("must be a normal file");
        }
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException excp) {
            throw new IllegalArgumentException(excp.getMessage());
        }
    }

    /** Return the entire contents of FILE as a String.  FILE must
     *  be a normal file.  Throws IllegalArgumentException
     *  in case of problems. */
    public static String readContentsAsString(File file) {
        return new String(readContents(file), StandardCharsets.UTF_8);
    }
}
