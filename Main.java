import java.util.ArrayList;
// test environment
public class Main {
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
        v = A.get(idx++); // error vector (let e1 = v1)
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
            v = A.get(idx++);
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
        ArrayList<double[]> arr = new ArrayList<>();
        arr.add(new double[] { 1, -1, 0 });
        arr.add(new double[] { 2, 0, -2 });
        arr.add(new double[] { 3, -3, 3 });

        ArrayList<double[]> Q = GramSchmidtProcess(arr);
        for (double[] i : Q) {
            for (double j : i) System.out.print(j + " ");
            System.out.println();
        }
    }
}