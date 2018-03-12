package com.islomar;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;


public class ParallelSorting {
    public static void main(String... args) {
        int[] numbers = ThreadLocalRandom.current()
                .ints(100_000_000)
                .toArray();
        testSorting(numbers);
    }

    private static void testSorting(int[] numbers) {
        int[] numbersSequential = numbers.clone();
        int[] numbersParallel = numbers.clone();

        sort("sequential", numbersSequential, Arrays::sort);
        sort("parallel", numbersParallel, Arrays::parallelSort);
    }

    private static void sort(String description, int[] numbers, Consumer<int[]> sortingAlgorithm) {
        long time = System.currentTimeMillis();
        try {
            sortingAlgorithm.accept(numbers);
        } finally {
            time = System.currentTimeMillis() - time;
            System.out.println(description + " - time = " + time + " ms");
        }
    }

}
