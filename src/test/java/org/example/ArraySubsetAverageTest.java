package org.example;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ArraySubsetAverageTest {

    @ParameterizedTest
    @MethodSource("illegalTestParameters")
    void IllegalArgumentTest(double[] arr, int start, int end) {
        assertThrows(IllegalArgumentException.class, () ->
                ArraySubsetAverage.calculateAverage(arr, start, end));
    }

    static Stream<Arguments> illegalTestParameters() {
        return Stream.of(
                arguments(null, 0, 10), // array null                                                              T1
                arguments(new double[]{5.0, 3.0, 5.5, 4.2}, -10, 3), // startIndex lower than 0                    T2
                arguments(new double[]{5.0, 3.0, 5.5, 4.2}, 0, 4), // endIndex greater than (arr length - 1)       T3
                arguments(new double[]{5.0, 3.0, 5.5, 4.2}, 8, 3) // startIndex greater than endIndex              T4
        );
    }

    @Test
    void LoopTest() {
        assertEquals(5.5, ArraySubsetAverage.calculateAverage(
                new double[]{5.0, 3.0, 5.5, 4.2}, 2, 2)); // one iteration      T5
    }


    // EXPLORE WHAT THE PROGRAM DOES FOR VARIOUS INPUTS

    @Test
    @Disabled
    void SimpleCase() {
        assertEquals(2, ArraySubsetAverage.calculateAverage(new double[]{3,5,1,0,9}, 1, 3));
    }

    @Test
    @Disabled
    void NegativeNumberCase() {
        double delta = 0.01;
        assertEquals(-1.33, ArraySubsetAverage.calculateAverage(new double[]{3,-5,1,0,-9}, 1, 3), delta);
    }

    @Test
    @Disabled
    void EmptyArray() {
        assertThrows(IllegalArgumentException.class,
                () -> ArraySubsetAverage.calculateAverage(new double[]{}, 0, 0));
    }
}
