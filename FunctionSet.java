import java.util.ArrayList;
import java.util.Arrays;

// limit number of rows cannot exceed 10000
public class FunctionSet {
    public static final double doubleError = 0.001;
    public static final double extremeBound = 2d;

    private FunctionSet() { // cannot create an object
    }

    public static double[] run(double[] x1, double[] x2, double[] y) {
        double[] equation = find2VariableCurve(x1, x2, y);
        ArrayList<Integer> extremes = findExtreme(x1, x2, y, equation);
        x1 = removeExtreme(x1, extremes);
        x2 = removeExtreme(x2, extremes);
        y = removeExtreme(y, extremes);
        equation = find2VariableCurve(x1, x2, y);
        return equation;
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

    private static boolean isZeroVector(double[] v) {
        for (int i = 0; i < v.length; i++) {
            if (v[i] > doubleError) return false;
        }
        return true;
    }

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
                Q[j][i] = qs.get(i)[j];
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

    private static double[] findX(double[][] A, double[][] b) { // b is column vector {{}, {}, {} ...}
        // x = ((R^T)R)^(-1)(R^T)(Q^T)b --> (R^T)Rx = (Q^T)b
        // let Rx = c, (Q^T)b = c -> solve x
        ArrayList<double[][]> QR = QRFactorization(A);
        double[][] Q = QR.get(0); // m x k
        double[][] R = QR.get(1); // k x k, A : m x n, n = k
        int k = QR.get(0)[0].length;
        int m = A.length;
        int n = A[0].length;
        if (n != k) {
            System.err.println("Error: n != k at function : findX");
            System.exit(1);
        }
        // ----------

        double[][] c = multiply(transpose(Q), b); // k x 1, n x 1
        double[][] x = new double[k][1]; // (R^T_col = i) * x_i  // k x 1, n x 1

        for (int i = k - 1; i >= 0; i--) {
            double temp = 0;
            for (int j = k - 1; j > i; j--) {
                temp += R[i][j] * x[j][0];
            }
            x[i][0] = (c[i][0] - temp) / R[i][i];
        }

        double[] vx = new double[k];
        for (int i = 0; i < k; i++) {
            vx[i] = x[i][0];
        }

        return vx;
    }

    private static double[] find2VariableCurve(double[] x1, double[] x2, double[] y) {
        // y = ax1 + bx2 + c

        // A:           b:  x:
        // x11 x12 1    y1  a
        // x21 x22 1    y2  b
        // x31 x32 1    y3  c
        // .   .   .    .   .
        // .   .   .    .   .

        double[][] A = new double[x1.length][3];
        for (int i = 0; i < x1.length; i++) {
            for (int j = 0; j < 3; j++) {
                if (j == 2) A[i][j] = 1;
                else if (j == 1) A[i][j] = x2[i];
                else A[i][j] = x1[i];
            }
        }
        double[][] b = new double[y.length][1];
        for (int i = 0; i < y.length; i++) b[i][0] = y[i];

        return findX(A, b);
    }

    // ---------- eliminateExtreme : return a set of index that is to be removed
    private static ArrayList<Integer> findExtreme(double[] x1, double[] x2, double[] y, double[] equation) { // equation { a, b, c } where y = ax1 + bx2 + c
        double[] e = new double[x1.length];

        for (int i = 0; i < x1.length; i++) {
            e[i] = Math.abs((equation[0] * x1[i] + equation[1] * x2[i] + equation[2]) - y[i]);
        }

        // find sigma
        double sigma = 0;
        double average = 0;
        for (int i = 0; i < e.length; i++) average += e[i];
        average /= e.length;
        for (int i = 0; i < e.length; i++) sigma += (e[i] - average) * (e[i] - average);
        sigma /= e.length;
        sigma = Math.sqrt(sigma);
        if (sigma < doubleError) return new ArrayList<>();

        // find extreme
        for (int i = 0; i < e.length; i++) e[i] /= sigma;

        ArrayList<Integer> extremes = new ArrayList<>();
        for (int i = 0; i < e.length; i++) {
            if (e[i] > extremeBound) extremes.add(i);
        }

        return extremes;
    }

    private static double[] removeExtreme(double[] x, ArrayList<Integer> extremes) {
        double[] result = new double[x.length - extremes.size()];
        int k = 0;
        int t = 0;
        for (int i = 0; i < x.length; i++) {
            if (t < extremes.size() && i == extremes.get(t)) {
                t++;
            } else {
                result[k++] = x[i];
            }
        }
        return result;
    }
}