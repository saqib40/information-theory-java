package codingtheory;

interface GF2 {
    // for primitives
    int add(int a, int b);
    int multiply(int a, int b);

    // for vectors
    int[] add(int[] v1, int[] v2);
    int dotProduct(int[] v1, int[] v2);

    // for matrices
    int[] vectorMatrixMultiply(int[] vector, int[][] matrix);
    int[][] transpose(int[][] matrix);

    // create an identity matrix of n*n
    int[][] createIdentityMatrix(int n);

    // for cyclic
    int[] polynomialDivide(int[] dividend, int[] divisor);
}

public class GF2Math implements GF2 {
    @Override
    public int add(int a, int b) {
        return a^b;
    }
    @Override
    public int multiply(int a, int b) {
        return a & b;
    }
    @Override
    public int[] add(int[] v1, int[] v2) {
        int[] ans = new int[v1.length];
        for(int i=0; i<v1.length; i++) {
            ans[i] = add(v1[i], v2[i]);
        }
        return ans;
    }
    @Override
    public int dotProduct(int[] v1, int[] v2) {
        int sum = 0;
        for(int i = 0; i < v1.length; i++) {
            int product = multiply(v1[i], v2[i]);
            sum = add(sum, product);
        }
        return sum;
    }
    @Override
    public int[] vectorMatrixMultiply(int[] vector, int[][] matrix) {
        int k = vector.length;
        int numCols = matrix[0].length;
        int[] result = new int[numCols];
        for (int j = 0; j < numCols; j++) {
            int sum = 0;
            for (int i = 0; i < k; i++) {
                int product = multiply(vector[i], matrix[i][j]);
                sum = add(sum, product);
            }
            result[j] = sum;
        }
        return result;
    }
    @Override
    public int[][] transpose(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        int[][] transposed = new int[cols][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                transposed[j][i] = matrix[i][j];
            }
        }
        return transposed;
    }
    @Override
    public int[][] createIdentityMatrix(int n) {
        int[][] I = new int[n][n];
        for(int i=0; i<n; i++) {
            I[i][i] = 1;
        }
        return I;
    }
    @Override
    public int[] polynomialDivide(int[] dividend, int[] divisor) {
        
    }
}

