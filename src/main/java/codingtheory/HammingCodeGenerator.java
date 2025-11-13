package codingtheory;

import java.util.Arrays;

public class HammingCodeGenerator {
    GF2Math math;
    int p; // number of parity bits
    int n; // number of bits in crossword
    int k; // number of message bits
    int[][] H; // parity matrix (p x n)
    int[][] G; // generator matrix (k x n)

    public HammingCodeGenerator(int p) {
        this.p = p;
        this.math = new GF2Math();
        this.n = (1 << p) - 1; // 2^p - 1
        this.k = this.n - this.p;

        // Initialize the final matrices
        this.H = new int[p][n];
        this.G = new int[k][n];

        // Build the matrices upon creation
        generateH();
        generateG();

    } 
    // get Generator Matrix
    void generateG() {
        int[][] PT = new int[p][k];
        for (int i = 0; i < p; i++) {
            for (int j = 0; j < k; j++) {
                PT[i][j] = this.H[i][j];
            }
        }

        int[][] P = math.transpose(PT);
        int[][] Ik = math.createIdentityMatrix(k);

        for (int i = 0; i < k; i++) {
            for (int j = 0; j < k; j++) {
                this.G[i][j] = Ik[i][j];
            }
            for (int j = 0; j < p; j++) {
                this.G[i][k + j] = P[i][j];
            }
        }
    }

    // get Parity Matrix
    void generateH() {
        // create a vector of n
        int[][] vectors = vectors(this.p);
        // transpose it
        int[][] transposed = math.transpose(vectors);
        // rearrange it
        rearrange(transposed);
        // that's it
        this.H = transposed;

    }

    // find all non-zero vectors of a particular length
    // there will be fixed of them
    // 2^n-1 of them
    int[][] vectors(int n) {
        // very famous recursion pattern
        // as taught by the one and only
        // striver
        // pick-noPick
        int[][] ans = new int[(1 << n) - 1][n];
        vectors(n, 0, new int[n], ans, 0);
        return ans;
    }

    int vectors(int n, int i, int[] vec, int[][] ans, int j) {
        if (i == n) {
            if (!isAllZero(vec)) {
                ans[j] = vec.clone();
                return j + 1;
            }
            return j;
        }
        vec[i] = 1;
        j = vectors(n, i + 1, vec, ans, j);

        vec[i] = 0;
        j = vectors(n, i + 1, vec, ans, j);

        return j;
    }

    boolean isAllZero(int[] vec) {
        for (int v : vec) {
            if (v != 0) return false;
        }
        return true;
    }

    void rearrange(int[][] H_matrix) {
        // get identity matrix at end 
        // dekh bawey
        // lots of code for this
        // LLM helped me write parts of this one
        // cause i was too stupid
        int p_rows = H_matrix.length; // p
        int n_cols = H_matrix[0].length; // n
        int k_cols = n_cols - p_rows; // k

        // We want to place the p identity columns (e.g., [1,0,0], [0,1,0], [0,0,1])
        // at the end of the matrix, from column k to column n-1.
        
        for (int i = 0; i < p_rows; i++) {
            // We are looking for the identity vector that has a '1' at row 'i'.
            // This vector should be at column 'k_cols + i'.
            
            // 1. Find the target column vector (e.g., [1,0,0] for i=0, [0,1,0] for i=1)
            int[] targetColVec = new int[p_rows];
            targetColVec[i] = 1;

            // 2. Find where this vector is *currently* located.
            int found_at_col = -1;
            for (int j = 0; j < n_cols; j++) {
                if (isColumnEqual(H_matrix, j, targetColVec)) {
                    found_at_col = j;
                    break;
                }
            }

            // 3. Swap the found column (at found_at_col)
            //    with the desired column (at k_cols + i)
            if (found_at_col != -1 && found_at_col != (k_cols + i)) {
                swapColumns(H_matrix, found_at_col, k_cols + i);
            }
        }
    }

    boolean isColumnEqual(int[][] matrix, int colIdx, int[] vec) {
        int p_rows = matrix.length;
        if (vec.length != p_rows) return false;
        
        for (int i = 0; i < p_rows; i++) {
            if (matrix[i][colIdx] != vec[i]) {
                return false;
            }
        }
        return true;
    }

    // returns -1 if ones are more than one in given column
    // or i otherwise
    int checkColumns(int[][] matrix, int col1) {
        int count = 0;
        int pos = -1;
        for(int i=0; i<matrix.length; i++) {
            if(matrix[i][col1] == 1) {
                count++;
                pos=i;
            }
        }
        if(count == 1) return pos;
        return -1;
    }

    void swapColumns(int[][] matrix, int col1, int col2) {
        for(int i=0; i<matrix.length; i++) {
            int temp = matrix[i][col1];
            matrix[i][col1] = matrix[i][col2];
            matrix[i][col2] = temp;
        }
    }

    public static void printMatrix(String title, int[][] matrix) {
        if (matrix == null || matrix.length == 0) {
            System.out.println(title + " (empty)");
            return;
        }
        System.out.println(title);
        for (int[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
        System.out.println();
    }

    public static void main(String[] args) {
        System.out.println("--- Generating Hamming (7,4) Code (p=3) ---");
        HammingCodeGenerator gen7_4 = new HammingCodeGenerator(3);

        printMatrix("H (Parity-Check Matrix, 3x7):", gen7_4.H);
        printMatrix("G (Generator Matrix, 4x7):", gen7_4.G);

        System.out.println("--- Generating Hamming (15,11) Code (p=4) ---");
        HammingCodeGenerator gen15_11 = new HammingCodeGenerator(4);
        printMatrix("H (Parity-Check Matrix, 4x15):", gen15_11.H);
        printMatrix("G (Generator Matrix, 11x15):", gen15_11.G);
    }
}
