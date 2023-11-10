import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Percolation
 * Model of a percolation system using an N-by-N grid of sites.
 * Each site is either open or blocked. A full site is an open site that can be connected
 * to an open site in the top row via a chain of neighboring (left, right, up, down) open sites.
 * The system percolates if there is a full site in the bottom row.
 *
 * @author Terry
 */
public class Percolation {
    private int n;
    private WeightedQuickUnionUF fullSet;
    private WeightedQuickUnionUF percolateSet;
    private boolean[] openSet;      // Use index of the grid.
    private int openNum = 0;

    /**
     * Create n-by-n grid, with all sites initially blocked.
     */
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Grid size must be a natural number.");
        }

        this.n = n;
        fullSet = new WeightedQuickUnionUF(n * n + 1);
        percolateSet = new WeightedQuickUnionUF(n * n + 2);
        openSet = new boolean[n * n];
    }

    /**
     * Open the site (row, col) if it is not open already.
     */
    public void open(int row, int col) {
        checkBounds(row, col);

        int index = corToIndex(row, col);

        if (!isOpen(row, col)) {
            openSet[index] = true;
            openNum++;

            /* Open the top row and set it as percolate. */
            if (row == 1) {
                fullSet.union(index, n * n);
                percolateSet.union(index, n * n);
            }
            /* Open the bottom row. */
            if (row == n) {
                percolateSet.union(index, n * n + 1);
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
        int index = corToIndex(row, col);
        return fullSet.find(index) == fullSet.find(n * n);
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
        return percolateSet.find(n * n) == percolateSet.find(n * n + 1);
    }

    /**
     * Check if the row or column is outside its prescribed range.
     */
    private void checkBounds(int row, int col) {
        if (!isValidCell(row, col)) {
            throw new IndexOutOfBoundsException("Row or column is out of bounds.");
        }
    }

    /**
     * Convert the coordinates to the index of the grid for union(int p, int q).
     *
     * @return the index of the grid
     */
    private int corToIndex(int row, int col) {
        return (row - 1) * n + (col - 1);
    }

    /**
     * Connects open cells with their neighbors.
     */
    private void joinCells(int row, int col) {
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };

        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            if (isValidCell(newRow, newCol) && isOpen(newRow, newCol)) {
                int currentIndex = corToIndex(row, col);
                int neighborIndex = corToIndex(newRow, newCol);

                fullSet.union(currentIndex, neighborIndex);
                percolateSet.union(currentIndex, neighborIndex);
            }
        }
    }

    /**
     * Checks if the specified cell is within the valid range of the grid.
     */
    private boolean isValidCell(int row, int col) {
        return row >= 1 && row <= n && col >= 1 && col <= n;
    }

    /**
     * Test client (optional).
     */
    public static void main(String[] args) {
    }
}
