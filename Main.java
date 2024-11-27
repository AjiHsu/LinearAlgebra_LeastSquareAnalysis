import java.util.ArrayList;
import java.util.Arrays;

// test environment
public class Main {
    // through
    public static void main(String[] argv) {
        double[][] A = {{1, 1, 1}, {2, 3, 4}, {4, 9, 16}, {5, 25, 125}};
        double[][] b = {{6}, {14}, {50}, {205}};

        double[] x = FunctionSet.run(A, b);

        for (int i = 0; i < x.length; i++) {
            System.out.print(x[i] + " ");
        }
        System.out.println();
    }
}