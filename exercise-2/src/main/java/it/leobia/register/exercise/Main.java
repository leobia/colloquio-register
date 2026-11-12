package it.leobia.register.exercise;

public final class Main {

    private Main() {
    }

    // Prints the product of the numbers passed as arguments
    public static void main(String[] args) {
        // Checks if args are only digits
        for (String arg : args) {
            if (!arg.matches("[0-9]+")) {
                System.err.println("Not a non-negative integer: \"" + arg + "\"");
                System.exit(1);
            }
        }

        // Start from 1 so that no arguments prints 1, the empty product
        int[] product = {1};
        for (String arg : args) {
            product = DigitArithmetic.multiply(product, DigitArithmetic.parse(arg));
        }
        System.out.println(DigitArithmetic.format(product));
    }
}
