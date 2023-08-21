package org.example;

public class ArraySubsetAverage {

    public static double calculateAverage(double[] arr, int startIndex, int endIndex) {
        if (arr == null) {
            throw new IllegalArgumentException("L'array di input non può essere null.");
        }
        if (startIndex < 0 || endIndex >= arr.length) {
            throw new IllegalArgumentException("Gli indici di inizio e fine " +
                    "devono essere compresi tra 0 e " + (arr.length - 1));
        }
        if (startIndex > endIndex) {
            throw new IllegalArgumentException("L'indice di inizio " +
                    "deve essere minore o uguale all'indice di fine.");
        }

        double sum = 0;
        for (int i = startIndex; i <= endIndex; i++) {
            sum += arr[i];
        }

        return sum / (endIndex - startIndex + 1);
    }
}
