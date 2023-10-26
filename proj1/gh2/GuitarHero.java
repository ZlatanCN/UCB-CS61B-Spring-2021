package gh2;

import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

public class GuitarHero {
    public static final String KEYBOARD = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
    private static GuitarString[] frequencies;

    public static double addSamples(GuitarString[] freq) {
        double sample = 0.0;
        for (int i = 0; i < KEYBOARD.length(); i++) {
            sample += freq[i].sample();
        }
        return sample;
    }

    public static void ticAll(GuitarString[] freq) {
        for (int i = 0; i < KEYBOARD.length(); i++) {
            freq[i].tic();
        }
    }

    public static void storeFrequencies(int lengtn) {
        for (int i = 0; i < lengtn; i++) {
            double frequency = 440 * Math.pow(2, (double) (i - 24) / 12);
            // System.out.println(frequency);
            frequencies[i] = new GuitarString(frequency);
        }
    }

    public static void main(String[] args) {
        frequencies = new GuitarString[KEYBOARD.length()];
        storeFrequencies(KEYBOARD.length());
        while (true) {
            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int index = KEYBOARD.indexOf(key);
                if (index == -1) {
                    continue;
                } else {
                    frequencies[KEYBOARD.indexOf(key)].pluck();
                }
            }

            double sample = addSamples(frequencies);
            StdAudio.play(sample);
            ticAll(frequencies);
        }
    }
}
