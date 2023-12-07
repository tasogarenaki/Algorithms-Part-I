/**
 * The Solver class represents a solver for the 8-Puzzle problem using the A* algorithm.
 *
 * @author Terry
 */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private final Stack<Board> solutionStack = new Stack<Board>();
    private int moves;

    /**
     * The SearchNode class represents a node in the search tree used by the A* algorithm.
     */
    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final SearchNode prev;
        private final int moves;
        private final int priority;
        private final int manhattanDistance;
        private final boolean isTwin;

        /**
         * Constructs a SearchNode with the given parameters.
         *
         * @param board  The current board state.
         * @param moves  Number of moves taken to reach this node.
         * @param prev   Reference to the previous node in the search path.
         * @param isTwin Flag indicating if this node corresponds to a twin board.
         */
        public SearchNode(Board board, int moves, SearchNode prev, boolean isTwin) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
            this.isTwin = isTwin;
            this.manhattanDistance = board.manhattan();
            this.priority = this.manhattanDistance + moves;
        }

        /**
         * Compares two SearchNodes based on their priorities.
         *
         * @param node The SearchNode to compare with.
         * @return The result of the comparison.
         */
        public int compareTo(SearchNode node) {
            return (priority != node.priority) ? Integer.compare(priority, node.priority) :
                   Integer.compare(manhattanDistance, node.manhattanDistance);
        }
    }

    /**
     * Constructs a Solver with the initial board using the A* algorithm.
     *
     * @param initial The initial board state.
     */
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("Initial board cannot be null.");
        }
        MinPQ<SearchNode> searchNodes = new MinPQ<SearchNode>();
        // Add initial search nodes for the original and twin boards
        searchNodes.insert(new SearchNode(initial, 0, null, false));
        searchNodes.insert(new SearchNode(initial.twin(), 0, null, true));

        // A* algorithm loop
        while (!searchNodes.isEmpty()) {
            SearchNode node = searchNodes.delMin();
            if (node.board.isGoal()) {
                if (node.isTwin) {
                    moves = -1;
                }
                else {
                    moves = node.moves;
                    saveSolution(node);
                }
                break;
            }

            for (Board neighbor : node.board.neighbors()) {
                if (node.prev == null || !neighbor.equals(node.prev.board)) {
                    searchNodes.insert(new SearchNode(neighbor, node.moves + 1, node, node.isTwin));
                }
            }
        }
    }

    /**
     * Saves the solution path in the solutionStack.
     *
     * @param node The final node in the solution path.
     */
    private void saveSolution(SearchNode node) {
        while (node != null) {
            solutionStack.push(node.board);
            node = node.prev;
        }
    }

    /**
     * Checks if the initial board is solvable.
     *
     * @return True if solvable, false otherwise.
     */
    public boolean isSolvable() {
        return moves != -1;
    }

    /**
     * Gets the minimum number of moves to solve the initial board.
     *
     * @return The number of moves or -1 if unsolvable.
     */
    public int moves() {
        return moves;
    }

    /**
     * Gets the sequence of boards in the shortest solution path.
     *
     * @return Iterable of boards or null if unsolvable.
     */
    public Iterable<Board> solution() {
        if (isSolvable()) {
            return solutionStack;
        }
        else {
            return null;
        }
    }

    /**
     * Main method for testing the Solver class.
     *
     * @param args Command-line arguments (expects the filename containing the initial board).
     */
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
