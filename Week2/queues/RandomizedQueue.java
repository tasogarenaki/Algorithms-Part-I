/**
 * RandomizedQueue
 * The item removed is chosen uniformly at random from among the items in the data structure.
 *
 * @author Terry
 */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private static final int INITIAL_LENGTH = 2;
    private static final int GROWTH_FACTOR = 2;
    private static final int DOWN_USAGE_FACTOR = 4;
    private Item[] elements;
    private int size;

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int[] randomIndex;
        private int total;

        public RandomizedQueueIterator() {
            randomIndex = new int[size];
            total = 0;

            for (int i = 0; i < size; i++) {
                randomIndex[i] = i;
            }

            StdRandom.shuffle(randomIndex);
        }

        public boolean hasNext() {
            return total < size && size != 0;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("There are no more items to return");
            }

            int theIndex = randomIndex[total++];
            return elements[theIndex];
        }
    }


    /** construct an empty randomized queue */
    public RandomizedQueue() {
        elements = (Item[]) new Object[INITIAL_LENGTH];
        size = 0;
    }

    /** is the randomized queue empty? */
    public boolean isEmpty() {
        return size == 0;
    }

    /** return the number of items on the randomized queue */
    public int size() {
        return size;
    }

    /** add the item */
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item can not be null.");
        }

        if (size == elements.length) {
            resize(size * GROWTH_FACTOR);
        }

        elements[size++] = item;
    }

    /** remove and return a random item */
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException(
                    "Removing an item from an empty queue is not possible.");
        }

        int index = StdRandom.uniformInt(size);
        Item item = elements[index];
        elements[index] = elements[--size];
        elements[size] = null;

        if (size > 0 && size == elements.length / DOWN_USAGE_FACTOR) {
            resize(elements.length / GROWTH_FACTOR);
        }

        return item;
    }

    /** return a random item (but do not remove it) */
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException(
                    "Removing an item from an empty queue is not possible.");
        }

        return elements[StdRandom.uniformInt(size)];
    }

    /** return an independent iterator over items in random order */
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    /** Resizes the array to the specified new size. */
    private void resize(int newSize) {
        Item[] copy = (Item[]) new Object[newSize];
        for (int i = 0; i < size; i++) {
            copy[i] = elements[i];
        }
        elements = copy;
    }

    /** unit testing (required) */
    public static void main(String[] args) {
        testIsEmpty();
        testSize();
        testEnqueue();
        testDequeue();
        testSample();
        testIterator();
    }

    private static void testIsEmpty() {
        StdOut.println("Test Empty.");

        RandomizedQueue<String> rq = new RandomizedQueue<String>();

        StdOut.println("size == 0");
        rq.size = 0;
        compare("rq.isEmpty()", rq.isEmpty(), true);

        StdOut.println("size != 0");
        rq.size = 1;
        compare("rq.isEmpty()", rq.isEmpty(), false);
    }

    private static void testSize() {
        StdOut.println("\nTest Size.");

        RandomizedQueue<String> rq = new RandomizedQueue<String>();

        StdOut.println("size == 0");
        rq.size = 0;
        compare("rq.size()", rq.size(), 0);

        StdOut.println("size == 1");
        rq.size = 1;
        compare("rq.size()", rq.size(), 1);
    }

    private static void testEnqueue() {
        StdOut.println("\nTest Enqueue.");

        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        StdOut.println("Enqueue 1 item.");
        rq.enqueue("A");
        compare("rq.size()", rq.size(), 1);

        StdOut.println("Enqueue 2 more items.");
        rq.enqueue("a");
        rq.enqueue("b");
        compare("rq2.size()", rq.size(), 3);

        try {
            rq.enqueue(null);
            StdOut.println(
                    "ERROR!!! rq.enqueue() with item null did not raise IllegalArgumentException");
        }
        catch (IllegalArgumentException ignored) {
        }
    }

    private static void testDequeue() {
        StdOut.println("\nTest Dequeue,");

        RandomizedQueue<String> rq = new RandomizedQueue<String>();

        try {
            rq.dequeue();
            StdOut.println("ERROR!!! rq.dequeue() did not raise NoSuchElementException");
        }
        catch (NoSuchElementException ignored) {
        }

        StdOut.println("When there is only one element.");
        rq.enqueue("A");
        compare("rq.dequeue()", rq.dequeue(), "A");
        compare("rq.size()", rq.size(), 0);
    }

    private static void testSample() {
        StdOut.println("\nTest Sample.");

        RandomizedQueue<String> rq = new RandomizedQueue<String>();

        try {
            rq.sample();
            StdOut.println("ERROR!!! rq.sample() did not raise NoSuchElementException");
        }
        catch (NoSuchElementException ignored) {
        }

        StdOut.println("Enqueue 2 items x and y.");
        rq.enqueue("x");
        rq.enqueue("y");
        rq.dequeue();
        StdOut.println("Sample: " + rq.sample());
        if (rq.sample() == null) {
            StdOut.println("ERROR!!! Return a null sample");
        }
    }

    private static void testIterator() {
        StdOut.println("\nTest Iterator.");

        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        Iterator<String> it = rq.iterator();
        StdOut.println("When it is empty:");
        compare("it.hasNext()", it.hasNext(), false);

        try {
            it.next();
            StdOut.println("ERROR!!! it.next() did not raise NoSuchElementException");
        }
        catch (NoSuchElementException ignored) {
        }

        StdOut.println("Enqueue a, b, c, d, e.");
        rq.enqueue("a");
        rq.enqueue("b");
        rq.enqueue("c");
        rq.enqueue("d");
        rq.enqueue("e");
        StdOut.println("Dequeue a random item.");
        rq.dequeue();

        StdOut.println("The rest:");
        for (String item : rq) {
            StdOut.print(item);
        }

        try {
            it.remove();
            StdOut.println("ERROR!!! it.remove() did not raise UnsupportedOperationException");
        }
        catch (UnsupportedOperationException ignored) {
        }
    }

    private static void compare(String command, Object result, Object expected) {
        StdOut.println(command + " -> " + result + " = " + expected);
        String errorMessage =
                "ERROR!!! Result and expected dont match." +
                        " Result: " + result + " Expected: " + expected;
        if (result != expected) StdOut.println(errorMessage);
    }
}
