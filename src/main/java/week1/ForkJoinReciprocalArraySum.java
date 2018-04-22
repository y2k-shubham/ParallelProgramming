package week1;

import commons.Week1;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ForkJoinReciprocalArraySum {

    class SumArray extends RecursiveAction {

        private int SEQUENTIAL_THRESHOLD;
        private double[] arr;
        private int lo;
        private int hi;
        private double result;

        public SumArray(double[] arr, int lo, int hi, int SEQUENTIAL_THRESHOLD) {
            this.arr = arr;
            this.lo = lo;
            this.hi = hi;
            this.SEQUENTIAL_THRESHOLD = SEQUENTIAL_THRESHOLD;
            this.result = 0.0d;
        }

        public double getResult() {
            return result;
        }

        @Override
        protected void compute() {
            if ((hi - lo + 1) <= SEQUENTIAL_THRESHOLD) {
                for (int i = lo; i <= hi; i++) {
                    result += 1 / arr[i];
                }
            } else {
                int mid = (lo + hi) / 2;

                SumArray left = new SumArray(arr, lo, mid, SEQUENTIAL_THRESHOLD);
                SumArray right = new SumArray(arr, mid + 1, hi, SEQUENTIAL_THRESHOLD);

                // can be replaced by invokeAll
                 invokeAll(left, right);
                //left.fork();
                //right.compute();
                //left.join();

                result = left.getResult() + right.getResult();
            }
        }
    }

    private double seqArraySum(double[] arr) {
        double sum1 = 0.0d;
        double sum2 = 0.0d;

        // find sum of lower-half
        for (int i = 0; i < arr.length / 2; i++) {
            sum1 += 1 / arr[i];
        }

        // find sum of upper-half
        for (int i = arr.length / 2 + 1; i < arr.length; i++) {
            sum2 += 1 / arr[i];
        }

        // Combine both sums
        double sum = sum1 + sum2;

        return sum;
    }

    private double parArraySum(double[] arr) {
        SumArray t = new SumArray(arr, 0, arr.length - 1, 1000);
        ForkJoinPool.commonPool().invoke(t);

        return t.getResult();
    }

    private void runner(double[] arr, int times, boolean parallel) {
        long startTime = System.nanoTime();
        String taskName = (parallel) ? "parArraySum" : "seqArraySum";

        double sum = 0.0d;
        if (parallel) {
            for (int i = 1; i <= times; i++) sum = parArraySum(arr);
        } else {
            for (int i = 1; i <= times; i++) sum = seqArraySum(arr);
        }

        long endTime = System.nanoTime();
        long timeInNanos = endTime - startTime;

        Week1.printResult(taskName, timeInNanos / times, "sum", sum);
    }

    public static void main(String[] args) {
        int times = 100;
        int length = (int) 1e7;

        ForkJoinReciprocalArraySum forkJoinReciprocalArraySum = new ForkJoinReciprocalArraySum();
        double[] arr = Week1.genArraySeq(length);

        forkJoinReciprocalArraySum.runner(arr, times, false);
        forkJoinReciprocalArraySum.runner(arr, times, true);
    }
}
