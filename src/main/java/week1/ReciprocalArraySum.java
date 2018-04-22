package week1;


import commons.Week1;
import edu.rice.pcdp.PCDP;

import java.util.concurrent.atomic.AtomicReference;

public class ReciprocalArraySum {

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
        double sum1 = 0.0d;
        double sum2 = 0.0d;
        AtomicReference<Double> atomicReference = new AtomicReference<Double>(0.0d);

        // find sum of lower-half
        PCDP.async(() -> {
            for (int i = 0; i < arr.length / 2; i++) {
                atomicReference.set(atomicReference.get() + 1 / arr[i]);
            }
        });

        // find sum of upper-half
        for (int i = arr.length / 2 + 1; i < arr.length; i++) {
            sum2 += 1 / arr[i];
        }

        // Combine both sums
        double sum = sum1 + atomicReference.get();

        return sum;
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

        Week1.printResult(taskName, timeInNanos, "sum", sum);
    }

    public static void main(String[] args) {
        int times = 1;
        int length = (int) 1e6;

        ReciprocalArraySum reciprocalArraySum = new ReciprocalArraySum();
        double[] arr = Week1.genArraySeq(length);

        reciprocalArraySum.runner(arr, times, false);
        reciprocalArraySum.runner(arr, times, true);
    }
}
