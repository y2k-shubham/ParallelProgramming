package commons;

public class Week1 {

    public static void printResult(String name, long timeInNanos, String resultName, double result) {
        System.out.printf("  %s completed in %8.3f milliseconds with %s = %8.5f\n", name, ((timeInNanos * 1.0d) / 1e6), resultName, result);
    }

    public static double[] genArraySeq(int length) {
        double[] arr = new double[length];

        for (int i = 1; i <= length; i++) {
            arr[i - 1] = i;
        }

        return arr;
    }
}
