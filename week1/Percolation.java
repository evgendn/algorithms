import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[] grid;
    private final int gridDim;
    private final WeightedQuickUnionUF uf;
    private final WeightedQuickUnionUF uf2;
    private int numberOfOpenedSites;
    private final int topPoint;
    private final int bottomPoint;

    // Neighbors coordinate(left, top, right, bottom)
    private final int[] nx = { -1, 0, 1, 0 };
    private final int[] ny = { 0, -1, 0, 1 };

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new java.lang.IllegalArgumentException();
        }

        this.grid = new boolean[n * n];
        this.gridDim = n;
        // Add two complement points for using them as virtual nodes(top and bottom)
        int ufSize = (n + 2) * (n + 2);
        this.uf = new WeightedQuickUnionUF(ufSize);
        this.uf2 = new WeightedQuickUnionUF(ufSize - 1);
        this.numberOfOpenedSites = 0;

        // Virtual nodes on grid, connected to top and bottom rows
        this.topPoint = 0;
        this.bottomPoint = ufSize - 1;
        for (int i = 0; i < n; i++) {
            this.uf.union(topPoint, i);
            this.uf.union(bottomPoint, this.dim2ToDim1(n - 1, i));

            this.uf2.union(topPoint, i);
        }
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        row--;
        col--;
        if (!checkBounds(row, col)) {
            throw new java.lang.IllegalArgumentException();
        }

        if (this.isOpen(row + 1, col + 1)) {
            return;
        }

        int point = this.dim2ToDim1(row, col);

        this.grid[point] = true;
        this.numberOfOpenedSites++;

        for (int i = 0; i < this.nx.length; i++) {
            int nRow = row + this.nx[i];
            int nCol = col + this.ny[i];
            int nPoint = this.dim2ToDim1(nRow, nCol);

            if (this.checkBounds(nRow, nCol)) {
                if (this.isOpen(nRow + 1, nCol + 1)) {
                    this.uf.union(point, nPoint);
                    this.uf2.union(point, nPoint);
                }
            }
        }
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        row--;
        col--;
        if (!checkBounds(row, col)) {
            throw new java.lang.IllegalArgumentException();
        }
        int index = this.dim2ToDim1(row, col);
        return this.grid[index];
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        row--;
        col--;
        if (!checkBounds(row, col)) {
            throw new java.lang.IllegalArgumentException();
        }

        int point = this.dim2ToDim1(row, col);
        boolean connected = this.uf2.connected(this.topPoint, point);
        return this.isOpen(row + 1, col + 1) && connected;
    }

    private boolean checkBounds(int row, int col) {
        if (row < 0 || row >= this.gridDim || col < 0 || col >= this.gridDim) {
            return false;
        }
        return true;
    }

    private int dim2ToDim1(int i, int j) {
        return i * this.gridDim + j;
    }

    // number of open sites
    public int numberOfOpenSites() {
        return this.numberOfOpenedSites;
    }

    // does the system percolate?
    public boolean percolates() {
        if (this.gridDim == 1) {
            return this.isOpen(1, 1);
        }
        return this.uf.connected(this.topPoint, this.bottomPoint);
    }

    public static void main(String[] args) {
        Percolation p = new Percolation(4);
        p.open(1, 1);
        p.open(2, 1);
        p.open(2, 2);
        // p.open(2, 3);
        p.open(2, 4);
        p.open(3, 4);
        p.open(3, 3);
        p.open(3, 2);
        p.open(4, 2);
        System.out.println(p.isFull(3, 3));
        System.out.println(p.percolates());
    }
}
