import java.util.ArrayList;
import java.util.Arrays;

// test environment
public class Main {
    // test
    public static void main(String[] argv) {
        double[] x1 = { 1, 3, 4, 5, 24, 203, 23920 };
        double[] x2 = { 24, 245, 2513, 3333, 9045, 10293, 105000 };
        double[] y = { 90, 209, 329, 4444, 5789, 7890, 9013 };

        double[] x = FunctionSet.run(x1, x2, y);

        for (int i = 0; i < x.length; i++) {
            System.out.print(x[i] + " ");
        }
        System.out.println();
    }
}