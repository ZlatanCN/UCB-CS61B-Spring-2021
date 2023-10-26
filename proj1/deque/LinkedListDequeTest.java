package deque;

import jh61b.junit.In;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;


/** Performs some basic linked list tests. */
public class LinkedListDequeTest {

    @Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {
        LinkedListDeque<String> lld1 = new LinkedListDeque<String>();

		assertTrue("A newly initialized LLDeque should be empty", lld1.isEmpty());
		lld1.addFirst("front");

		// The && operator is the same as "and" in Python.
		// It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, lld1.size());
        assertFalse("lld1 should now contain 1 item", lld1.isEmpty());

		lld1.addLast("middle");
		assertEquals(2, lld1.size());

		lld1.addLast("back");
		assertEquals(3, lld1.size());

		System.out.println("Printing out deque: ");
		lld1.printDeque();
    }

    @Test
    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    public void addRemoveTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
		// should be empty
		assertTrue("lld1 should be empty upon initialization", lld1.isEmpty());

		lld1.addFirst(10);
		// should not be empty
		assertFalse("lld1 should contain 1 item", lld1.isEmpty());
        assertEquals(lld1.size(), 1);

		lld1.removeFirst();
		// should be empty
		assertTrue("lld1 should be empty after removal", lld1.isEmpty());
        assertEquals(lld1.size(), 0);
    }

    @Test
    /* Tests removing from an empty deque */
    public void removeEmptyTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addFirst(3);

        lld1.removeLast();
        lld1.removeFirst();
        lld1.removeLast();
        lld1.removeFirst();

        int size = lld1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);
    }

    @Test
    /* Check if you can create LinkedListDeques with different parameterized types*/
    public void multipleParamTest() {
        LinkedListDeque<String>  lld1 = new LinkedListDeque<String>();
        LinkedListDeque<Double>  lld2 = new LinkedListDeque<Double>();
        LinkedListDeque<Boolean> lld3 = new LinkedListDeque<Boolean>();

        lld1.addFirst("string");
        lld2.addFirst(3.14159);
        lld3.addFirst(true);

        assertEquals(lld1.get(0), "string");
        assertEquals(lld3.get(0), true);

        String s = lld1.removeFirst();
        double d = lld2.removeFirst();
        boolean b = lld3.removeFirst();

        assertNull(lld1.get(0));
        assertNull(lld2.get(0));
        assertNull(lld3.get(0));
    }

    @Test
    /* check if null is return when removing from an empty LinkedListDeque. */
    public void emptyNullReturnTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();

        boolean passed1 = false;
        boolean passed2 = false;
        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, lld1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, lld1.removeLast());
    }

    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigLLDequeTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        for (int i = 0; i < 1000000; i++) {
            lld1.addLast(i);
        }

        for (double i = 0; i < 500000; i++) {
            assertEquals("Should have the same value", i, (double) lld1.removeFirst(), 0.0);
        }

        for (double i = 999999; i > 500000; i--) {
            assertEquals("Should have the same value", i, (double) lld1.removeLast(), 0.0);
        }
    }

    @Test
    /* Test for get() and getRecursive() */
    public void getTest() {
        LinkedListDeque<Integer> test = new LinkedListDeque<>();

        test.addLast(10);
        test.addLast(20);
        test.addLast(30);
        test.addLast(40);
        test.addFirst(0);

        assertEquals(test.get(3), test.getRecursive(3));
        assertEquals(test.get(4), test.getRecursive(4));
        assertEquals(test.get(0), test.getRecursive(0));
    }

    @Test
    /* Test for equal() */
    public void equalTest() {
        LinkedListDeque<Integer> test = new LinkedListDeque<>();
        LinkedListDeque<Integer> equal = new LinkedListDeque<>();
        LinkedListDeque<Integer> stillNotEqual = new LinkedListDeque<>();

        test.addLast(10);
        test.addLast(20);
        test.addLast(30);
        test.addLast(40);
        test.addFirst(0);

        equal.addLast(10);
        equal.addLast(20);
        equal.addLast(30);
        equal.addLast(40);
        equal.addFirst(0);

        stillNotEqual.addLast(10);
        stillNotEqual.addLast(20);
        stillNotEqual.addLast(30);
        stillNotEqual.addLast(40);
        stillNotEqual.addFirst(100);

        assertTrue(test.equals(equal));
        assertFalse(test.equals(stillNotEqual));
    }

    @Test
    /* Test for iterator() */
    public void iteratorTest() {
        LinkedListDeque<Integer> test = new LinkedListDeque<>();
        ArrayList<Integer> testList = new ArrayList<>();

        test.addLast(10);
        test.addLast(20);
        test.addLast(30);
        test.addLast(40);
        test.addFirst(0);

        for (Integer i : test) {
            testList.add(i);
        }

        assertEquals(testList.get(0), test.get(0));
        assertEquals(testList.get(1), test.get(1));
        assertEquals(testList.get(2), test.get(2));
        assertEquals(testList.get(3), test.get(3));
        assertEquals(testList.get(4), test.get(4));
    }
}
