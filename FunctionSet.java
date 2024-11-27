import java.util.ArrayList;
import java.util.Arrays;
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

    /* test passed (1) */
    private static boolean isZeroVector(double[] v) {
        for (int i = 0; i < v.length; i++) {
            if (v[i] > doubleError) return false;
        }
        return true;
    }

    /* test passed */
    private static ArrayList<double[]> GramSchmidtProcess(ArrayList<double[]> A) {
        int m = A.size(); // row number
        int n = A.get(0).length; // Rn
        int idx = 0;
        ArrayList<double[]> Q = new ArrayList<>();
        double[] v;
        double len;

        // first step
        v = Arrays.copyOf(A.get(idx++), n); // error vector (let e1 = v1)
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
            v = Arrays.copyOf(A.get(idx++), n);
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

    /* test passed */
    private static ArrayList<double[][]> QRFactorization(double[][] A) { // get(0) = Q; get(1) = R
        ArrayList<double[]> as = new ArrayList<>(); // column vectors of A : v
        for (int i = 0; i < A[0].length; i++) {
            double[] t = new double[A.length];
            for (int j = 0; j < A.length; j++) {
                t[j] = A[j][i];
            }
            as.add(t);
        }

        ArrayList<double[]> qs = GramSchmidtProcess(as); // orthonormal column vectors : q
        int m = qs.get(0).length; // dim of column vectors
        int k = qs.size(); // number of orthonormal vectors

        // Q
        double[][] Q = new double[m][k]; // col i = qi
        for (int i = 0; i < k; i++) {
            for (int j = 0; j < m; j++) {
                Q[i][j] = qs.get(j)[i];
            }
        }

        // R
        double[][] R = new double[k][k]; // Rij = qTi * vj
        for (int i = 0; i < k; i++) {
            for (int j = i; j < k; j++) {
                for (int t = 0; t < m; t++) {
                    R[i][j] += as.get(j)[t] * qs.get(i)[t];
                }
            }
        }

        ArrayList<double[][]> result = new ArrayList<>();
        result.add(Q);
        result.add(R);
        return result;
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