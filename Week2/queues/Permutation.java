/**
 * Permutation
 * A client program Permutation.java that takes an integer k as a command-line argument;
 * reads a sequence of strings from standard input using StdIn.readString();
 * and prints exactly k of them, uniformly at random. Print each item from the sequence at most
 * once.
 *
 * @author Terry
 */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        int k;
        RandomizedQueue<String> rq = new RandomizedQueue<String>();

        try {
            k = Integer.parseInt(args[0]);
        }
        catch (NumberFormatException e) {
            StdOut.println("It must be an integer.");
            return;
        }

        while (!StdIn.isEmpty()) {
            rq.enqueue(StdIn.readString());
        }

        for (int i = 0; i < k; i++) {
            StdOut.println(rq.dequeue());
        }
    }
}
