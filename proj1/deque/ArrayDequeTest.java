package deque;

import org.junit.Test;


import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.*;


/** Performs some basic linked list tests. */
public class ArrayDequeTest {

    @Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {
        ArrayDeque<String> ad1 = new ArrayDeque<>();

        assertTrue("A newly initialized LLDeque should be empty", ad1.isEmpty());
        ad1.addFirst("front");

        // The && operator is the same as "and" in Python.
        // It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, ad1.size());
        assertFalse("lld1 should now contain 1 item", ad1.isEmpty());

        ad1.addLast("middle");
        assertEquals(2, ad1.size());

        ad1.addLast("back");
        assertEquals(3, ad1.size());

        System.out.println("Printing out deque: ");
        ad1.printDeque();
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
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        ad1.addFirst(3);

        ad1.removeLast();
        ad1.removeFirst();
        ad1.removeLast();
        ad1.removeFirst();

        int size = lld1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);
    }

    @Test
    /* Check if you can create LinkedListDeques with different parameterized types*/
    public void multipleParamTest() {
        ArrayDeque<String> ad1 = new ArrayDeque<>();
        ArrayDeque<Double> ad2 = new ArrayDeque<>();
        ArrayDeque<Boolean> ad3 = new ArrayDeque<>();

        ad1.addFirst("string");
        ad2.addFirst(3.14159);
        ad3.addFirst(true);

        String s = ad1.removeFirst();
        double d = ad2.removeFirst();
        boolean b = ad3.removeFirst();
    }

    @Test
    /* check if null is return when removing from an empty LinkedListDeque. */
    public void emptyNullReturnTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();

        boolean passed1 = false;
        boolean passed2 = false;
        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, ad1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, ad1.removeLast());
    }

    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigLLDequeTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        for (int i = 0; i < 1000000; i++) {
            ad1.addLast(i);
        }

        for (double i = 0; i < 500000; i++) {
            assertEquals("Should have the same value", i, (double) ad1.removeFirst(), 0.0);
        }

        for (double i = 999999; i > 500000; i--) {
            assertEquals("Should have the same value", i, (double) ad1.removeLast(), 0.0);
        }
    }

    @Test
    /* Test for equal() */
    public void equalTest() {
        ArrayDeque<Integer> test = new ArrayDeque<>();
        ArrayDeque<Integer> equal = new ArrayDeque<>();
        LinkedListDeque<Integer> notEqual = new LinkedListDeque<>();
        ArrayDeque<Integer> stillNotEqual = new ArrayDeque<>();

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

        notEqual.addLast(10);
        notEqual.addLast(20);
        notEqual.addLast(30);
        notEqual.addLast(40);
        notEqual.addFirst(0);

        stillNotEqual.addLast(10);
        stillNotEqual.addLast(20);
        stillNotEqual.addLast(30);
        stillNotEqual.addLast(40);
        stillNotEqual.addFirst(100);

        assertTrue(test.equals(equal));
        assertTrue(test.equals(notEqual));
        assertFalse(test.equals(stillNotEqual));
    }

    @Test
    /* Test for iterator() */
    public void iteratorTest() {
        ArrayDeque<Integer> test = new ArrayDeque<>();
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

    @Test
    /* Test for get() */
    public void getTest() {
        ArrayDeque<Integer> test = new ArrayDeque<>();
        test.addLast(10);
        test.addLast(20);
        test.addLast(30);
        test.addLast(40);
        test.addFirst(0);
        test.removeFirst();
        test.removeLast();

        assertEquals(test.get(0), Integer.valueOf(10));
        assertEquals(test.get(1), Integer.valueOf(20));
        assertEquals(test.get(2), Integer.valueOf(30));
    }
}
