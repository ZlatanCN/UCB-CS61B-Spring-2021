package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import timingtest.AList;

import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing<Integer> correct = new AListNoResizing<>();
        BuggyAList<Integer> wrong = new BuggyAList<>();

        correct.addLast(4);
        correct.addLast(5);
        correct.addLast(6);

        wrong.addLast(4);
        wrong.addLast(5);
        wrong.addLast(6);

        assertEquals(correct.size(), wrong.size());
        assertEquals(correct.removeLast(), wrong.removeLast());
        assertEquals(correct.removeLast(), wrong.removeLast());
        assertEquals(correct.removeLast(), wrong.removeLast());
    }

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> S = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 3);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                S.addLast(randVal);
                assertEquals(L.getLast(), S.getLast());
            } else if (operationNumber == 1) {
                // size
                int sizeL = L.size();
                int sizeS = S.size();
                assertEquals(sizeL, sizeS);
            } else {
                if (L.size() == 0 && S.size() == 0) {
                    continue;
                }
                int removedValueL = L.removeLast();
                int removedValueS = S.removeLast();
                assertEquals(removedValueL, removedValueS);
            }
        }
    }
}
