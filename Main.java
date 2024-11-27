// test environment
public class Main {
    // test
    public static void main(String[] argv) {

        // input data must be linear independent : y(x1i, x2i) y(x1j, x2j) for all i != j : !(x1i == x1j && x2i == x2j) is always true
        double[] x1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        double[] x2 = { 10, 9, 8, 7, 6, 5, 4, 3, 2, 1 };
        double[] y  = { 15, 18, 21, 24, 27, 30, 33, 36, 39, 42 };

        double[] x = FunctionSet.run(x1, x2, y);

        for (int i = 0; i < x.length; i++) {
            System.out.print(x[i] + " ");
        }
        System.out.println();
    }
}