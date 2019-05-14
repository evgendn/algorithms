import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {
    private double[] thresholds;
    private final int gridSize;

    private double mean;
    private double stddev;
    private double confidenceLo;
    private double confidenceHi;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new java.lang.IllegalArgumentException();
        }

        this.gridSize = n * n;
        this.thresholds = new double[trials];

        this.runExpirement(n, trials);
    }

    private void runExpirement(int n, int trials) {
        for (int tries = 0; tries < trials; tries++) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                int i = StdRandom.uniform(n) + 1;
                int j = StdRandom.uniform(n) + 1;

                p.open(i, j);
            }
            this.thresholds[tries] = (double) p.numberOfOpenSites() / (double) this.gridSize;
        }

        this.mean = StdStats.mean(this.thresholds);
        this.stddev = StdStats.stddev(this.thresholds);
        this.confidenceLo = this.mean - (1.96 * Math.sqrt(this.stddev)) / Math.sqrt(trials);
        this.confidenceHi = this.mean + (1.96 * Math.sqrt(this.stddev)) / Math.sqrt(trials);
    }

    // sample mean of percolation threshold
    public double mean() {
        return this.mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return this.confidenceLo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return confidenceHi;
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);

        PercolationStats ps = new PercolationStats(n, t);
        System.out.println("mean                    = " + ps.mean());
        System.out.println("stddev                  = " + ps.stddev());
        System.out.println("95% confidence interval = " + ps.confidenceLo()
                                   + ", " + ps.confidenceHi());
    }
}
