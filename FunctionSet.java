import java.util.ArrayList;
import java.util.List;

// limit number of rows cannot exceed 10000
public class FunctionSet {
    public static final double doubleError = 0.001;
    public static final int limitRun = 10000;

    private FunctionSet() { // cannot create an object
    }

    public static List<int[]> run(int[] x1, int[] x2, int[] y) {
        List<int[]> res = new ArrayList<>();
        res.add(find2Variable1DCurve(x1, x2, y));
        res.add(find1DCurve(x1, y));
        res.add(find2DCurve(x1, y));
        res.add(find1DCurve(x2, y));
        res.add(find2DCurve(x2, y));
        return res;
    }

    private static double[][] transpose(double[][] arr) {
        int m = arr.length;
        int n = arr[0].length;
        double[][] trans = new double[n][m];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                trans[j][i] = arr[i][j];
            }
        }
        return trans;
    }

    private static double[][] transpose(double[] vector) { // 1D transpose
        double[][] trans = new double[vector.length][1];

        for (int i = 0; i < vector.length; i++) {
            trans[i][0] = vector[i];
        }

        return trans;
    }

    private static double[][] multiply(double[][] arr1, double[][] arr2) {
        int m1 = arr1.length;
        int n1 = arr1[0].length;
        int m2 = arr2.length;
        int n2 = arr2[0].length;
        if (n1 != m2) {
            System.err.println("Error: n1 != m2 at function : multiply");
            System.exit(1);
            return null;
        }

        double[][] result = new double[m1][n2];

        for (int i = 0; i < m1; i++) {
            for (int j = 0; j < n2; j++) {
                for (int k = 0; k < m2; k++) { // m2 == n1
                    result[i][j] += arr1[i][k] * arr2[k][j];
                }
            }
        }

        return result;
    }

    private static double[] minus(double[] a, double[] b) {
        if (a.length != b.length) {
            System.err.println("Error: a.length != b.length at function : minus");
            System.exit(1);
        }
        double[] result = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] - b[i];
        }
        return result;
    }

    private static boolean isZeroVector(int[] v) {
        for (int i = 0; i < v.length; i++) {
            if (v[i] != 0) return false;
        }
        return true;
    }

    /* test passed (1) */
    private static boolean isZeroVector(double[] v) {
        for (int i = 0; i < v.length; i++) {
            if (v[i] > doubleError) return false;
        }
        return true;
    }

    /* test passed */
    private static ArrayList<double[]> GramSchmidtProcess(double[][] A) {
        int m = A.length; // row number
        int n = A[0].length; // Rn
        int idx = 0;
        ArrayList<double[]> Q = new ArrayList<>();
        double[] v;
        double len;

        // first step
        v = A[idx++]; // error vector (let e1 = v1)
        len= 0; // length of vector
        for (double i : v) {
            len += i * i;
        }

        len = Math.sqrt(len);
        for (int i = 0; i < n; i++) { // normalization
            v[i] /= len;
        }

        // step iteration
        while (!isZeroVector(v)) {
            Q.add(v); // add to Q

            if (idx >= m) break;

            // find error vector
            v = A[idx++];
            for (double[] q : Q) { // vk = sigma(k-1, 1)_qi^T vk qi
                double t = 0;
                for (int i = 0; i < q.length; i++) t += q[i] * v[i];
                for (int i = 0; i < v.length; i++) v[i] -= t * q[i];
            }

            // normalization
            len= 0; // length of vector
            for (double i : v) {
                len += i * i;
            }

            len = Math.sqrt(len);
            for (int i = 0; i < n; i++) { // normalization
                v[i] /= len;
            }
        }

        return Q;
    }

    private static int[][] QRFactorization(int[][] A) { // int[0] = Q, int[1] = R
        // todo
        return null;
    }

    private static int[] findX(int[][] A, int[] b) { // (A^TA)^-1A^TB or A=QR
        // todo
        return null;
    }

    private static int[] findError(int[][] A, int[] b) { // b - p = b - Ax
        // todo
        return null;
    }

    private static int[] find1DCurve(int[] x, int[] y) {
        // function : y = ax + b

        // A:
        // x1 1
        // x2 1
        // x3 1
        // .  .
        // .  .

        // b:
        // y1
        // y2
        // y3
        // .
        // .

        // x:
        // a
        // b

        int len = x.length;
        // todo
        return null;
    }

    private static int[] find2DCurve(int[] x, int[] y) {
        // function : y = ax^2 + bx + c

        // A:
        // x1^2 x1 1
        // x2^2 x2 1
        // x3^2 x3 1
        // .    .  .
        // .    .  .

        // b:
        // y1
        // y2
        // y3
        // .
        // .

        // x:
        // a
        // b
        // c

        // todo
        return null;
    }

    private static int[] find2Variable1DCurve(int[] x1, int[] x2, int[] y) {
        // y = ax1 + bx2 + c

        // A:
        // x11 x12 1
        // x21 x22 1
        // x31 x32 1
        // .   .   .
        // .   .   .

        // b:
        // y1
        // y2
        // y3
        // .
        // .

        // x:
        // a
        // b
        // c

        // todo
        return null;
    }

}