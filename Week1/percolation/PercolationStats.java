import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * PercolationStats
 * Estimates the percolation threshold (mu) using the Monte Carlo Method.
 *
 * @author Terry
 */
public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private int trials;
    private double[] thresholds;

    /* Perform independent trials on an n-by-n grid. */
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Grid size must be a natural number.");
        }
        this.trials = trials;
        thresholds = new double[trials];
        int row, col;

        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                do {
                    row = StdRandom.uniformInt(n) + 1;
                    col = StdRandom.uniformInt(n) + 1;
                } while (perc.isOpen(row, col));
                perc.open(row, col);
            }
            thresholds[i] = (double) perc.numberOfOpenSites() / (n * n);
        }
    }

    /**
     * Sample mean of percolation threshold
     * mu = (x1 + x2 + ... + xT) / T
     */
    public double mean() {
        return StdStats.mean(thresholds);
    }

    /**
     * Sigma^2: sample standard deviation of percolation threshold
     */
    public double stddev() {
        return StdStats.stddev(thresholds);
    }

    /**
     * Low endpoint of 95% confidence interval
     */
    public double confidenceLo() {
        return mean() - CONFIDENCE_95 * stddev() / Math.sqrt(trials);
    }

    /**
     * High endpoint of 95% confidence interval
     */
    public double confidenceHi() {
        return mean() + CONFIDENCE_95 * stddev() / Math.sqrt(trials);
    }

    // test client (see below)
    public static void main(String[] args) {
        PercolationStats percStats = new PercolationStats(Integer.parseInt(args[0]),
                                                          Integer.parseInt(args[1]));
        String confidence = "[" + percStats.confidenceLo() + ", " + percStats.confidenceHi() + "]";
        StdOut.println("mean                    = " + percStats.mean());
        StdOut.println("stddev                  = " + percStats.stddev());
        StdOut.println("95% confidence interval = " + confidence);
    }
}
