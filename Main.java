import java.util.ArrayList;
import java.util.Arrays;

// test environment
public class Main {

    /* test passed (1) */
    private static boolean isZeroVector(double[] v) {
        for (int i = 0; i < v.length; i++) {
            if (v[i] > FunctionSet.doubleError) return false;
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

    public static void main(String[] argv) {
        double[][] arr = { { 1, 1, 0 }, { 1, 0, 1 }, { 0, 1, 1 } };

        ArrayList<double[][]> ans = QRFactorization(arr);
        for (double[][] Q : ans) {
            for (double[] i : Q) {
                for (double j : i) System.out.print(j + " ");
                System.out.println();
            }
            System.out.println("-----------------------------------------------");
        }
    }
}