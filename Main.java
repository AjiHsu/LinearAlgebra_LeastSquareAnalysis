// test environment
public class Main {
    // test
    public static void main(String[] argv) {

        // input data must be linear independent : y(x1i, x2i) y(x1j, x2j) for all i != j : !(x1i == x1j && x2i == x2j) is always true
        double[] x1 = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0,
                11.0, 12.0, 13.0, 14.0, 15.0, 16.0, 17.0, 18.0, 19.0, 20.0, 21};
        double[] x2 = {0.5, 1.5, 2.5, 3.5, 4.5, 5.5, 6.5, 7.5, 8.5, 9.5,
                10.5, 11.5, 12.5, 13.5, 14.5, 15.5, 16.5, 17.5, 18.5, 19.5, 20};
        double[] y = {-185.24, 47.8, 86.1, 124.3, 162.5, 200.8, 239.0, 277.3, 315.5,
                353.7, 391.9, 430.2, 468.5, 506.7, 545.0, 583.2, 621.4,
                659.7, 697.9, 736.1, 1500.0};

        double[] x = FunctionSet.run(x1, x2, y);

        for (int i = 0; i < x.length; i++) {
            System.out.print(x[i] + " ");
        }
        System.out.println();
    }
}