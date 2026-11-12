package it.leobia.register.exercise;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

class DigitArithmeticTest {

    @Test
    void multiplyExampleFromExercise() {
        // given
        int[] fifteen = {5, 1};
        int[] two = {2};

        // when
        int[] product = DigitArithmetic.multiply(fifteen, two);

        // then
        assertArrayEquals(new int[] {0, 3}, product);
    }

    @Test
    void multiplyPropagatesCarry() {
        // given
        int[] nineHundredNinetyNine = {9, 9, 9};

        // when
        int[] product = DigitArithmetic.multiply(nineHundredNinetyNine, nineHundredNinetyNine);

        // then
        assertArrayEquals(new int[] {1, 0, 0, 8, 9, 9}, product);
    }

    @Test
    void multiplyComputesFactorialOfHundred() {
        // given
        int[] factorial = {1};
        BigInteger expected = BigInteger.ONE;

        // when
        for (int factor = 2; factor <= 100; factor++) {
            factorial = DigitArithmetic.multiply(factorial, DigitArithmetic.parse(Integer.toString(factor)));
            expected = expected.multiply(BigInteger.valueOf(factor));
        }

        // then
        assertEquals(expected.toString(), DigitArithmetic.format(factorial));
    }
}
