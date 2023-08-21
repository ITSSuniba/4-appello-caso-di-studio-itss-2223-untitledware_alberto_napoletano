package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class FindBestSeatTest {
    @ParameterizedTest
    @MethodSource("vectorsNullProvider")
    @DisplayName("Check what method does if Vectors are null")
    void VectorsNull(double[] prices, boolean[] taken){
        assertThrows(IllegalArgumentException.class,()->FindBestSeat.getBestPrice(prices, taken, 3));
    }

    static Stream<Arguments> vectorsNullProvider(){
        return Stream.of(
                arguments(null, new boolean[]{true, true, false}), // prices null       T1
                arguments(new double[]{5.0, 3.0, 5.5, 4.2}, null), // taken null        T2
                arguments(null, null) // both null                                      T3
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {-48 /* T4 */, 0 /* T17 */})
    @DisplayName("Check what method does if the number of seats is zero (boundary off-point) or below")
    void NumberOfSeatsZeroOrBelow(int numberOfSeats){
        assertThrows(IllegalArgumentException.class,
                () -> FindBestSeat.getBestPrice(new double[]{5.0, 3.0}, new boolean[]{true, false}, numberOfSeats));
    }

    @Test
    @DisplayName("Buying tickets with zero or negative prices. *Includes boundary cases*")
    void ZeroOrNegativePrices(){
        Assertions.assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> FindBestSeat.getBestPrice(
                        new double[]{2.0,3.0,-3.0,2.0,-4.5,-3.5},
                        new boolean[]{false,false,false,false,true,true},
                        3)),                                                                // T5
                () -> assertThrows(IllegalArgumentException.class, () -> FindBestSeat.getBestPrice(
                        new double[]{2.0,3.0,0.0,12.0,0.0,13.5},
                        new boolean[]{false,false,false,false,true,true},
                        3)),                                                                // T14
                () -> assertEquals(4.01, FindBestSeat.getBestPrice(
                        new double[]{2.0,3.0,0.01,2.0,4.5,3.5},
                        new boolean[]{false,false,false,false,true,true},
                        3))                                                                 // T15
        );
    }

    @Test
    @DisplayName("Check what method does if the number of seats is above availability. *Includes boundary cases*")
    void NumberOfSeatsAboveAvailable(){
        Assertions.assertAll(
                () -> assertEquals(0.0, FindBestSeat.getBestPrice(
                        new double[]{5.0, 3.0, 9.0}, new boolean[]{true, true, true}, 3)),               // T6
                () -> assertEquals(7.0, FindBestSeat.getBestPrice(
                        new double[]{5.0, 3.0, 9.0, 4.0}, new boolean[]{true, false, true, false}, 10)), // T8
                () -> assertEquals(14.0, FindBestSeat.getBestPrice(
                        new double[]{5.0, 3.0, 9.0, 8.0}, new boolean[]{false, true, false, true}, 4)),  // T9
                ()-> assertEquals(7.0, FindBestSeat.getBestPrice(
                        new double[]{7.0}, new boolean[]{false}, 3)),                                    // T12
                () -> assertEquals(0.0, FindBestSeat.getBestPrice(
                        new double[]{}, new boolean[]{}, 3))                                             // T13
        );
    }

    @ParameterizedTest
    @MethodSource("differentSizeVectorsProvider")
    @DisplayName("Check what method does if prices vector's length is not equal taken vector's length")
    void PriceVectorNotEqualTakenVector(double[] prices, boolean[] taken){
        assertThrows(IllegalArgumentException.class,
                () -> FindBestSeat.getBestPrice(prices, taken, 3));
    }

    static Stream<Arguments> differentSizeVectorsProvider(){
        return Stream.of(
                arguments(new double[]{5.0, 3.0}, new boolean[]{true, true, false, true, false}),   // T7
                arguments(new double[]{5.0, 3.0}, new boolean[]{true, true, false}),                // T18
                arguments(new double[]{5.0, 3.0, 7.0}, new boolean[]{true, true})                   // T19
        );
    }

    @Test
    @DisplayName("Check what method does if the number of seats is below availability")
    void NumberOfSeatsBelowAvailable(){
        assertEquals(7.0, FindBestSeat.getBestPrice(
                new double[]{2.0,3.0,3.0,2.0,4.5,3.5},
                new boolean[]{false,false,false,false,true,true},
                3));                                    // T10
    }

    @Test
    @DisplayName("Buying tickets for a total price greater than 100 for having a discount. *Includes boundary cases*")
    void TotalPriceGreaterThanOneHundred(){
        Assertions.assertAll(
                ()-> assertEquals(118.5 - 5, FindBestSeat.getBestPrice(
                        new double[]{50.5, 30, 49.5, 69}, new boolean[]{true, true, false, false}, 2)),     // T11
                ()-> assertEquals(100, FindBestSeat.getBestPrice(
                        new double[]{50.5, 30, 49.5, 69}, new boolean[]{false, true, false, false}, 2)),    // T24
                ()-> assertEquals(100.01 - 5, FindBestSeat.getBestPrice(
                        new double[]{50.5, 30, 49.51, 69}, new boolean[]{false, true, false, false}, 2),    // T25
                        2)
        );
    }

    @Test
    @DisplayName("Check what method does if only 1 seat is requested")
    void OneRequestedSeatBoundaryCase(){
        assertEquals(2.0, FindBestSeat.getBestPrice(
                new double[]{2.0,3.0,3.0,2.0,4.5,3.5},
                new boolean[]{false,false,false,false,true,true},
                1));                                            // T16
    }

    @Test
    @DisplayName("Check what method does if the number of seats equals availability")
    void NumberOfSeatsEqualsAvailable(){
        Assertions.assertAll(
                ()-> assertEquals(10.0, FindBestSeat.getBestPrice(
                        new double[]{2.0,4.5,3.5},
                        new boolean[]{false,false,false},
                        3)),                                        // T20
                ()-> assertEquals(10.0, FindBestSeat.getBestPrice(
                        new double[]{2.0,4.5,3.5},
                        new boolean[]{false,false,false},
                        4)),                                        // T21
                ()-> assertEquals(7.0, FindBestSeat.getBestPrice(
                        new double[]{2.0,3.0,3.0,2.0,4.5,3.5},
                        new boolean[]{false,false,true,false,true,true},
                        3)),                                        // T22
                ()-> assertEquals(5.5, FindBestSeat.getBestPrice(
                        new double[]{2.0,4.5,3.5},
                        new boolean[]{false,true,false},
                        3))                                         // T23
        );
    }

    @Test
    void MoreDecimalDigits(){
        assertEquals(60.70, FindBestSeat.getBestPrice(
                new double[]{50, 3.4721, 90.2345, 7.231}, new boolean[]{false, false, false, false}, 3));   // T26
    }


    // EXPLORE WHAT THE PROGRAM DOES FOR VARIOUS INPUTS

    @Test
    @Disabled
    void BuyTwoSeats(){
        assertEquals(12.98, FindBestSeat.getBestPrice(
                new double[]{7.99,4.99,9.90,10,8}, new boolean[]{false, false, true, true, false}, 2));
    }

    @Test
    @Disabled
    void BuyAllAvailableSeats(){
        assertEquals(25.89, FindBestSeat.getBestPrice(
                new double[]{7.99,4.99,9.90,10,8}, new boolean[]{false, true, false, true, false}, 3));
    }

    @Test
    @Disabled
    void RequestedSeatsMoreThanAvailable(){
        assertEquals(4.99, FindBestSeat.getBestPrice(
                new double[]{7.99,4.99,9.90,10,8}, new boolean[]{true, false, true, true, true}, 4));
    }

    @Test
    @Disabled
    void MultipleSeatsWithSamePrice(){
        assertEquals(17.97, FindBestSeat.getBestPrice(
                new double[]{7.99,4.99,9.90,10,4.99}, new boolean[]{false, false, false, false, false}, 3));
    }
}
