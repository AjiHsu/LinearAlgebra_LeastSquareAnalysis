import java.util.ArrayList;
import java.util.List;

public class FunctionSet {
    private FunctionSet() {
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

    private static int[][] transpose(int[][] arr) {
        int m = arr.length;
        int n = arr[0].length;
        int[][] trans = new int[n][m];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                trans[j][i] = arr[i][j];
            }
        }
        return null;
    }

    private static int[][] multiply(int[][] arr1, int[][] arr2) {
        int m1 = arr1.length;
        int n1 = arr1[0].length;
        int m2 = arr2.length;
        int n2 = arr2[0].length;
        if (n1 != m2) {
            System.err.println("Error: n1 != m2 at function : multiply");
            System.exit(1);
            return null;
        }

        int[][] result = new int[m1][n2];

        for (int i = 0; i < m1; i++) {
            for (int j = 0; j < n2; j++) {
                for (int k = 0; k < m2; k++) { // m2 == n1
                    result[i][j] += arr1[i][k] * arr2[k][j];
                }
            }
        }

        return result;
    }

    private int[][] inverse(int[][] arr) {
        // todo
        return null;
    }

    private int[] findX(int[][] A, int[] b) { // (A^TA)^-1A^TB
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
