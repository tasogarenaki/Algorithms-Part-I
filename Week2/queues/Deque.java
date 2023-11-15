/**
 * Deque
 * A double-ended queue (deque) data structure.
 *
 * @author Terry
 */


import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private int size;
    private Node first;
    private Node last;

    /** Helper - Linked List Class */
    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }

    /** Deque Iterator */
    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("There are no more items to return");
            }

            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    /** construct an empty deque */
    public Deque() {
        size = 0;
        first = null;
        last = null;
    }

    /** is the deque empty? */
    public boolean isEmpty() {
        return size == 0;
    }

    /** return the number of items on the deque */
    public int size() {
        return size;
    }

    /** add the item to the front */
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item can not be null.");
        }

        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        first.prev = null;

        if (isEmpty()) {
            last = first;
        }
        else {
            oldFirst.prev = first;
        }

        size++;
    }

    /** add the item to the back */
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("An item cannot be null.");
        }

        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        last.prev = oldLast;

        if (isEmpty()) {
            first = last;
        }
        else {
            oldLast.next = last;
        }

        size++;
    }

    /** remove and return the item from the front */
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException(
                    "Removing an item from an empty queue is not possible.");
        }

        Item item = first.item;
        first = first.next;
        size--;

        if (isEmpty()) {
            last = null;
        }
        else {
            first.prev = null;
        }

        return item;
    }

    /** remove and return the item from the back */
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException(
                    "Removing an item from an empty queue is not possible.");
        }

        Item item = last.item;
        last = last.prev;
        size--;

        if (isEmpty()) {
            first = null;
        }
        else {
            last.next = null;
        }

        return item;
    }

    /** return an iterator over items in order from front to back */
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        testEmpty();
        testAddFirstRemoveFirst();
        testAddFirstRemoveLast();
        testAddLastRemoveFirst();
        testAddLastRemoveLast();
    }

    /******************************************************
     *                     Unit Tests
     ******************************************************/
    private static void testEmpty() {
        System.out.println("Test Empty.");
        Deque<String> d1 = new Deque<String>();
        compare("d1.iterator().hasNext()", d1.iterator().hasNext(), false);
        compare("d1.isEmpty()", d1.isEmpty(), true);
        compare("d1.size()", d1.size(), 0);
    }

    private static void testAddFirstRemoveFirst() {
        System.out.println("\nTest add first then remove first.");
        Deque<String> d1 = new Deque<String>();
        System.out.println("d1.addFirst(\"test\");");
        d1.addFirst("test");
        compare("d1.iterator().hasNext()", d1.iterator().hasNext(), true);
        compare("d1.iterator().next()", d1.iterator().next(), "test");
        compare("d1.isEmpty()", d1.isEmpty(), false);
        compare("d1.size()", d1.size(), 1);
        compare("d1.removeFirst()", d1.removeFirst(), "test");
        compare("d1.isEmpty()", d1.isEmpty(), true);
    }

    private static void testAddFirstRemoveLast() {
        System.out.println("\nTest add first then remove last.");
        Deque<String> d1 = new Deque<String>();
        System.out.println("d1.addFirst(\"test\");");
        d1.addFirst("test");
        compare("d1.iterator().hasNext()", d1.iterator().hasNext(), true);
        compare("d1.iterator().next()", d1.iterator().next(), "test");
        compare("d1.isEmpty()", d1.isEmpty(), false);
        compare("d1.size()", d1.size(), 1);
        compare("d1.removeLast()", d1.removeLast(), "test");
        compare("d1.size()", d1.size(), 0);
    }

    private static void testAddLastRemoveFirst() {
        System.out.println("\nTest add last then remove first.");
        Deque<String> d1 = new Deque<String>();
        System.out.println("d1.addLast(\"test\");");
        d1.addLast("test");
        compare("d1.iterator().hasNext()", d1.iterator().hasNext(), true);
        compare("d1.iterator().next()", d1.iterator().next(), "test");
        compare("d1.isEmpty()", d1.isEmpty(), false);
        compare("d1.size()", d1.size(), 1);
        compare("d1.removeFirst()", d1.removeFirst(), "test");
        compare("d1.isEmpty()", d1.isEmpty(), true);
        compare("d1.size()", d1.size(), 0);
    }

    private static void testAddLastRemoveLast() {
        System.out.println("\nTest add last then remove last.");
        Deque<String> d1 = new Deque<String>();
        System.out.println("d1.addLast(\"test\");");
        d1.addLast("test");
        compare("d1.iterator().hasNext()", d1.iterator().hasNext(), true);
        compare("d1.iterator().next()", d1.iterator().next(), "test");
        compare("d1.isEmpty()", d1.isEmpty(), false);
        compare("d1.size()", d1.size(), 1);
        compare("d1.removeLast()", d1.removeLast(), "test");
        compare("d1.isEmpty()", d1.isEmpty(), true);
        compare("d1.size()", d1.size(), 0);
    }

    private static void compare(String command, Object result, Object expected) {
        System.out.println(command + " -> " + result + " = " + expected);
        String errorMessage =
                "ERROR!!! Result and expected dont match." +
                        " Result: " + result + " Expected: " + expected;
        if (result != expected) System.out.println(errorMessage);
    }
}
