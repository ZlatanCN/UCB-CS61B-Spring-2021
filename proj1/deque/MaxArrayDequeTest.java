package deque;

import jh61b.junit.In;
import org.apache.commons.math3.analysis.function.Max;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;

import static org.junit.Assert.*;


/** Performs some basic linked list tests. */
public class MaxArrayDequeTest {
    @Test
    public void testMax() {
        MaxArrayDeque<Integer> deque = new MaxArrayDeque<Integer>(Comparator.naturalOrder());

        deque.addLast(5);
        deque.addLast(10);
        deque.addLast(3);
        deque.addLast(8);

        Integer max = deque.max();
        assertEquals(Integer.valueOf(10), max);
    }

    @Test
    public void testMaxWithComparator() {
        MaxArrayDeque<Integer> deque = new MaxArrayDeque<Integer>(Comparator.comparing(Math::abs));

        deque.addLast(-5);
        deque.addLast(10);
        deque.addLast(-3);
        deque.addLast(8);

        Integer maxAbs = deque.max(Comparator.comparing(Math::abs));
        assertEquals(maxAbs, Integer.valueOf(10));
    }

    @Test
    public void testMaxEmptyDeque() {
        MaxArrayDeque<Integer> deque = new MaxArrayDeque<Integer>(Comparator.naturalOrder());

        Integer max = deque.max();
        assertNull(max);
    }
}
