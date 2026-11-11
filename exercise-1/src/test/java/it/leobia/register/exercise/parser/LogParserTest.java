package it.leobia.register.exercise.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import it.leobia.register.exercise.model.LogEntry;

class LogParserTest {

    private static final LocalDateTime TIMESTAMP = LocalDateTime.of(2026, 11, 10, 8, 15, 30);

    private final LogParser parser = new LogParser();

    @Test
    // No value source here because we test directly against a LogEntry
    void validLinesAreParsedWithEveryField() {
        // given
        String lineOk = "2026-11-10T08:15:30;1024;OK;192.168.0.1";
        String lineNotFound = "2026-11-10T08:15:30;512;Not Found;10.0.0.1";

        // when
        Optional<LogEntry> resultOk = parser.parse(lineOk);
        Optional<LogEntry> resultNotFound = parser.parse(lineNotFound);

        // then
        assertEquals(Optional.of(new LogEntry(TIMESTAMP, 1024L, "OK", "192.168.0.1")), resultOk);
        assertEquals(Optional.of(new LogEntry(TIMESTAMP, 512L, "Not Found", "10.0.0.1")), resultNotFound);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            // wrong field count: 3 or 5 fields
            "2026-11-10T08:15:30;1024;OK",
            "2026-11-10T08:15:30;1024;OK;10.0.0.1;extra",
            // unparseable timestamp, or with an offset / zone
            "test;1024;OK;10.0.0.1",
            "2026-11-10T08:15:30+01:00;1024;OK;10.0.0.1",
            "2026-11-10T08:15:30Z;1024;OK;10.0.0.1",
            // non-numeric or negative bytes
            "2026-11-10T08:15:30;abc;OK;10.0.0.1",
            "2026-11-10T08:15:30;-5;OK;10.0.0.1"
    })
    void malformedLineGivesEmptyResult(String line) {
        // given a malformed line

        // when
        Optional<LogEntry> result = parser.parse(line);

        // then
        assertEquals(Optional.empty(), result);
    }
}
