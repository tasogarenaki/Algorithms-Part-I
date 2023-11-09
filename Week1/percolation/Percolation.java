/**
 * Percolation
 * Model of a percolation system using an N-by-N grid of sites.
 * Each site is either open or blocked. A full site is an open site that can be connected
 * to an open site in the top row via a chain of neighboring (left, right, up, down) open sites.
 * The system percolates if there is a full site in the bottom row.
 *
 * @author Terry
 */

package percolation;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int N;
    private WeightedQuickUnionUF fullSet;
    private WeightedQuickUnionUF percolateSet;
    private boolean[] openSet;      // Use index of the grid.
    private int openNum = 0;

    /**
     * Create N-by-N grid, with all sites initially blocked.
     */
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("Grid size must be a natural number.");
        }

        this.N = N;
        fullSet = new WeightedQuickUnionUF(N * N + 1);
        percolateSet = new WeightedQuickUnionUF(N * N + 2);
        openSet = new boolean[N * N];
    }

    /**
     * Open the site (row, col) if it is not open already.
     */
    public void open(int row, int col) {
        checkBounds(row, col);

        if (!isOpen(row, col)) {
            openSet[corToIndex(row, col)] = true;
            openNum++;

            /* Open the top row and set it as percolate. */
            if (row == 0) {
                fullSet.union(corToIndex(row, col), N * N);
                percolateSet.union(corToIndex(row, col), N * N);
            }
            /* Open the bottom row. */
            if (row == N - 1) {
                fullSet.union(corToIndex(row, col), N * N + 1);
            }
        }
        joinCells(row, col);
    }

    /**
     * Is the site (row, col) open?
     */
    public boolean isOpen(int row, int col) {
        checkBounds(row, col);
        return openSet[corToIndex(row, col)];
    }

    /**
     * Is the site (row, col) full?
     */
    public boolean isFull(int row, int col) {
        checkBounds(row, col);
        return fullSet.connected(corToIndex(row, col), N * N);
    }

    /**
     * Number of open sites
     */
    public int numberOfOpenSites() {
        return openNum;
    }

    /**
     * Does the system percolate?
     */
    public boolean percolates() {
        return percolateSet.connected(N * N, N * N + 1);
    }

    /**
     * Check if the row or column is outside its prescribed range.
     */
    private void checkBounds(int row, int col) {
        if (row < 0 || col < 0 || row >= N || col >= N ) {
            throw new IndexOutOfBoundsException("Row or column is out of bounds.");
        }
    }

    /**
     * Convert the coordinates to the index of the grid for union(int p, int q).
     * @return the index of the grid
     */
    private int corToIndex(int row, int col) {
        return row * N + col;
    }

    /**
     * Connects open cells with their neighbors.
     */
    private void joinCells(int row, int col) {
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            if (newRow >= 0 && newRow < N && newCol >= 0 && newCol < N && isOpen(newRow, newCol)) {
                int currentIndex = corToIndex(row, col);
                int neighborIndex = corToIndex(newRow, newCol);

                fullSet.union(currentIndex, neighborIndex);
                percolateSet.union(currentIndex, neighborIndex);
            }
        }
    }

    /**
     * Test client (optional).
     */
    public static void main(String[] args) { }
}
