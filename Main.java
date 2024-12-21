import java.util.Date;

// test environment
public class Main {
    // test
    public static void main(String[] argv) {

        // input data must be linear independent : y(x1i, x2i) y(x1j, x2j) for all i != j : !(x1i == x1j && x2i == x2j) is always true
        double[] x1;

        double[] x2;

        double[] y;

        int maxSize = 1100000;
        int timeLimit = 10000;
        x1 = new double[maxSize];
        x2 = new double[maxSize];
        y = new double[maxSize];
        for (int i = 0; i < maxSize; i++) {
            x1[i] = i;
            x2[i] = i * 0.1289;
            y[i] = i / 20d + Math.sqrt(i);
        }

        Date start = new Date();
        double[] x = FunctionSet.run(x1, x2, y);
        Date end = new Date();
        if (start.getTime() - end.getTime() > timeLimit) System.err.println("Time limit exceeded");
        else System.out.println("time elapsed:" + (end.getTime() - start.getTime()) + "ms");

        System.out.println("the equation is: ");
        System.out.println("y = " + x[0] + "x1 + " + x[1] + "x2 = " + x[2]);

//        double[] y2 = new double[y.length];
//        for (int i = 0; i < y.length; i++) {
//            y2[i] = x1[i] * x[0] + x2[i] * x[1] + x[2];
//        }
//
//        System.out.println("y vs y' : ");
//        for (int i = 0; i < y2.length; i++) {
//            System.out.println(y[i] + " " + y2[i]);
//        }
    }
}