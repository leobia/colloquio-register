package it.leobia.register.exercise;

import java.util.Arrays;

public final class DigitArithmetic {

    private DigitArithmetic() {
    }

    // Column multiplication using only additions
    public static int[] multiply(int[] left, int[] right) {
        // Early exit if one of the two is zero
        if (isZero(left) || isZero(right)) {
            return new int[] {0};
        }

        // 23 x 15: left is [3, 2] and right is [5, 1]
        int[] result = {0};

        for (int position = 0; position < right.length; position++) {
            // The row is 23 for the units and 230 for the tens
            int[] row = shift(left, position);

            // Add 23 five times, then 230 once
            for (int times = 0; times < right[position]; times++) {
                result = add(result, row);
            }
        }

        return result;
    }

    private static boolean isZero(int[] number) {
        return number.length == 1 && number[0] == 0;
    }

    // Column addition, for example 99 + 3 = 102 is [9, 9] + [3] = [2, 0, 1]
    private static int[] add(int[] a, int[] b) {
        int maxLength = Math.max(a.length, b.length);
        // One extra cell in case the last column carries
        int[] result = new int[maxLength + 1];

        // First add the columns, treating a missing digit as 0
        for (int i = 0; i < maxLength; i++) {
            int aDigit = i < a.length ? a[i] : 0;
            int bDigit = i < b.length ? b[i] : 0;
            result[i] = aDigit + bDigit;
        }

        // Then fix the carries: a column above 9 keeps the units and passes 1 to the next column
        for (int i = 0; i < maxLength; i++) {
            if (result[i] > 9) {
                result[i] -= 10;
                result[i + 1] += 1;
            }
        }

        // If nothing was carried into the extra cell, drop it so 2 + 3 gives [5] and not [5, 0]
        if (result[maxLength] == 0) {
            return Arrays.copyOf(result, maxLength);
        }
        return result;
    }

    // Moves the number up by some columns, filling with zeros: [3, 2] moved by 1 is [0, 3, 2], so 23 becomes 230
    private static int[] shift(int[] number, int positions) {
        int[] result = new int[number.length + positions];
        for (int i = 0; i < number.length; i++) {
            result[i + positions] = number[i];
        }
        return result;
    }

    // Turns "15" into [5, 1]
    public static int[] parse(String value) {
        int[] digits = new int[value.length()];
        for (int i = 0; i < value.length(); i++) {
            digits[i] = value.charAt(value.length() - 1 - i) - '0';
        }
        return digits;
    }

    // Turns [5, 1] into "15"
    public static String format(int[] number) {
        String text = "";
        for (int i = number.length - 1; i >= 0; i--) {
            text += number[i];
        }
        return text;
    }
}
