import java.util.ArrayList;
// test environment
public class Main {
    private static boolean isZeroVector(double[] v) {
        for (int i = 0; i < v.length; i++) {
            if (v[i] > FunctionSet.doubleError) return false;
        }
        return true;
    }

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

    public static void main(String[] argv) {
        double[][] arr =  {
                { 1, -1, 0 },
                { 1, 0, -2 },
                { 3, -1, 3 }
        };
        ArrayList<double[]> Q = GramSchmidtProcess(arr);
        for (double[] i : Q) {
            for (double j : i) System.out.print(j + " ");
            System.out.println();
        }
    }
}