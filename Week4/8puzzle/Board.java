/**
 * The `Board` class represents a game board for the 8-puzzle problem.
 * It provides methods to initialize the board, calculate distances,
 * and perform operations such as swapping tiles and finding neighboring boards.
 *
 * @author Terry
 */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class Board {
    private final int n;
    private final int hammingDistance;
    private final int manhanttanDistance;
    private final int[][] tiles;
    private static final int BLANK = 0;
    private int blankX, blankY;

    /**
     * Initializes a new board with the given 2D array of tiles.
     * Calculates Hamming and Manhattan distances.
     *
     * @param tiles The 2D array of tiles.
     */
    public Board(int[][] tiles) {
        n = tiles.length;
        int tempHamming = 0;
        int tempManhattan = 0;

        this.tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.tiles[i][j] = tiles[i][j];
                int goalValue = i * n + j + 1;

                if (tiles[i][j] == BLANK) {
                    blankX = i;
                    blankY = j;
                }
                else if (tiles[i][j] != goalValue) {
                    int goalX = (tiles[i][j] - 1) / n;
                    int goalY = (tiles[i][j] - 1) % n;

                    tempHamming++;
                    tempManhattan += Math.abs(i - goalX) + Math.abs(j - goalY);
                }
            }
        }
        hammingDistance = tempHamming;
        manhanttanDistance = tempManhattan;
    }

    /**
     * @return The string representation of the board.
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dimension()).append("\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    /**
     * @return The dimension of the board.
     */
    public int dimension() {
        return n;
    }

    /**
     * @return the number of tiles out of place (Hamming distance).
     */
    public int hamming() {
        return hammingDistance;
    }

    /**
     * @return the sum of Manhattan distances between tiles and the goal.
     */
    public int manhattan() {
        return manhanttanDistance;
    }

    /**
     * Checks if the board is the goal board.
     */
    public boolean isGoal() {
        return hamming() == 0;
    }

    /**
     * Checks if the board is equal to the specified object.
     *
     * @param y The object to compare.
     */
    public boolean equals(Object y) {
        if (y == this) {
            return true;
        }
        if (y == null || y.getClass() != this.getClass()) {
            return false;
        }

        Board that = (Board) y;
        if (n != that.dimension()) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] != that.tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * @return an iterable collection of neighboring boards.
     */
    public Iterable<Board> neighbors() {
        Queue<Board> neighbors = new Queue<>();
        int[] dx = { -1, 1, 0, 0 };
        int[] dy = { 0, 0, -1, 1 };

        for (int i = 0; i < dx.length; i++) {
            int x = blankX + dx[i];
            int y = blankY + dy[i];
            boolean isValid = x >= 0 && x < n && y >= 0 && y < n;

            if (isValid) {
                // Swap adjacent tiles
                swap(tiles, blankX, blankY, x, y);
                neighbors.enqueue(new Board(tiles));
                // Undo the swap to restore the original board
                swap(tiles, blankX, blankY, x, y);
            }
        }
        return neighbors;
    }

    /**
     * Swaps two tiles on the board.
     *
     * @param tiles The 2D array representing the board.
     * @param x1    The x-coordinate of the first tile.
     * @param y1    The y-coordinate of the first tile.
     * @param x2    The x-coordinate of the second tile.
     * @param y2    The y-coordinate of the second tile.
     */
    private void swap(int[][] tiles, int x1, int y1, int x2, int y2) {
        int temp = tiles[x1][y1];
        tiles[x1][y1] = tiles[x2][y2];
        tiles[x2][y2] = temp;
    }

    /**
     * Exchanges any pair of tiles to create a twin board.
     *
     * @return The twin board.
     * @throws IllegalArgumentException if a valid twin board cannot be found.
     */
    public Board twin() {
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n - 1; ++j) {
                if (tiles[i][j] != BLANK && tiles[i][j + 1] != BLANK) {
                    swap(tiles, i, j, i, j + 1);
                    Board result = new Board(tiles);
                    swap(tiles, i, j, i, j + 1);
                    return result;
                }
            }
        }
        throw new IllegalArgumentException("Unable to find a valid twin board.");
    }

    /**
     * Unit testing for the `Board` class.
     * Reads input and performs various board operations for testing purposes.
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board b = new Board(tiles);
        StdOut.println(b.toString());
        StdOut.println("Dimension: " + b.dimension());
        StdOut.println("Hamming distance: " + b.hamming());
        StdOut.println("Manhattan distance: " + b.manhattan());

        Board t = b.twin();
        StdOut.println("Block twin: " + t.toString());
        StdOut.println(b.equals(t));

        for (Board it : b.neighbors()) {
            StdOut.println(it.toString());
            StdOut.println("Dimension: " + it.dimension());
            StdOut.println("Hamming distance: " + it.hamming());
            StdOut.println("Manhattan distance: " + it.manhattan());
        }
    }
}
