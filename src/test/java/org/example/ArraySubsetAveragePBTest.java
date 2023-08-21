package org.example;

import net.jqwik.api.*;
import net.jqwik.api.constraints.DoubleRange;
import net.jqwik.api.constraints.Size;
import net.jqwik.api.statistics.Histogram;
import net.jqwik.api.statistics.Statistics;
import net.jqwik.api.statistics.StatisticsReport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArraySubsetAveragePBTest {

    @Property
    @Report(Reporting.GENERATED)
    @StatisticsReport(format = Histogram.class)
    void shouldCalculateAverage(
            /* We generate a list with 100 numbers, ranging from -1000 to 1000. Range chosen randomly. */
            @ForAll @Size(value = 100) List<@DoubleRange(min = -1000, max = 1000) Double> numbers,
            /* We randomly pick a subset to search in */
            @ForAll("subsetIndexes") int[] indexes
    ) {

        /* we convert the list to an array, as expected by the method */
        double[] arr = listToArray(numbers);
        /* we take the two indexes from the indexes array */
        int startIndex = indexes[0];
        int endIndex = indexes[1];

        /* we expect the method to return average, calculated as
         * sum of the elements divided by the number of elements */
        double sum = 0;
        for (int i = startIndex; i <= endIndex; i++) {
            sum += arr[i];
        }

        double expectedAverage = sum / (endIndex - startIndex + 1);
        assertEquals(expectedAverage, ArraySubsetAverage.calculateAverage(arr, startIndex, endIndex));

        String elements = (startIndex == endIndex ? "one element" :
                ((startIndex == 0 && endIndex == arr.length-1) ? "whole array" : "more elements")) + " --";
        String average = "average: " + (expectedAverage == 0 ? "zero" : expectedAverage < 0 ? "negative" : "positive");

        Statistics.collect(elements, average);
    }

    @Property
    @Report(Reporting.GENERATED)
    @StatisticsReport(format = Histogram.class)
    void invalidIndexes(
            /* We generate a list with 100 numbers, ranging from -1000 to 1000. Range chosen randomly. */
            @ForAll @Size(value = 100) List<@DoubleRange(min = -1000, max = 1000) Double> numbers,
            /* We randomly pick a wrong subset */
            @ForAll("wrongIndexes") int[] indexes
    ) {

        /* we convert the list to an array, as expected by the method */
        double[] arr = listToArray(numbers);
        /* we take the two indexes from the indexes array */
        int startIndex = indexes[0];
        int endIndex = indexes[1];

        /* we expect the method to throw an exception */
        assertThrows(IllegalArgumentException.class, () -> ArraySubsetAverage.calculateAverage(arr, startIndex, endIndex));

        String greaterStart = (startIndex > endIndex ? "start > end" : "start <= end") + " --";
        String negativeStart = (startIndex < 0 ? "negative start" : "positive start") + " --";
        String biggerEnd = (endIndex >= arr.length ? "bigger end" : "lower end");

        Statistics.label("Reason").collect(greaterStart, negativeStart , biggerEnd);
    }

    @Test
    void InvalidArray() {
        Assertions.assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> ArraySubsetAverage.calculateAverage(null, 0, 10)), // null
                () -> assertThrows(IllegalArgumentException.class,
                        () -> ArraySubsetAverage.calculateAverage(new double[]{}, 0, 0)) // empty
        );
    }

    @Provide
    @SuppressWarnings("unused")
    Arbitrary<int[]> subsetIndexes() {
        Arbitrary<Integer> indexes = Arbitraries.integers().between(0, 99);
        return Combinators.combine(indexes, indexes)
                .filter((start, end) -> start <= end)
                .as((start, end) -> new int[] {start, end});
    }

    @Provide
    @SuppressWarnings("unused")
    Arbitrary<int[]> wrongIndexes() {
        Arbitrary<Integer> indexes = Arbitraries.integers().between(-199, 199);
        return Combinators.combine(indexes, indexes)
                .filter((start, end) -> start > end || start < 0 || end > 99)
                .as((start, end) -> new int[] {start, end});
    }

    private double[] listToArray(List<Double> list) {
        return list.stream().mapToDouble(x -> x).toArray();
    }
}