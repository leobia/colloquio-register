package it.leobia.register.exercise;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

class SmokeTest {

    @Test
    void mainRunsWithoutArguments() {
        assertDoesNotThrow(() -> Main.main(new String[0]));
    }
}
