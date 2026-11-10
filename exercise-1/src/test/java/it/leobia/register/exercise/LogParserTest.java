package it.leobia.register.exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class LogParserTest {

    private static final LocalDateTime TIMESTAMP = LocalDateTime.of(2026, 11, 10, 8, 15, 30);

    private final LogParser parser = new LogParser();

    @Test
    void validLineIsParsedWithEveryField() {
        // given
        String line = "2026-11-10T08:15:30;1024;OK;192.168.0.1";

        // when
        Optional<LogRecord> result = parser.parse(line);

        // then
        assertEquals(Optional.of(new LogRecord(TIMESTAMP, 1024L, "OK", "192.168.0.1")), result);
    }

    @Test
    void surroundingWhitespaceInFieldsIsTrimmed() {
        // given
        String line = "  2026-11-10T08:15:30 ;\t1024  ; OK ; 192.168.0.1 ";

        // when
        Optional<LogRecord> result = parser.parse(line);

        // then
        assertEquals(Optional.of(new LogRecord(TIMESTAMP, 1024L, "OK", "192.168.0.1")), result);
    }

    @Test
    void nonOkStatusIsStillValid() {
        // given
        String line = "2026-11-10T08:15:30;512;Not Found;10.0.0.1";

        // when
        Optional<LogRecord> result = parser.parse(line);

        // then
        assertEquals(Optional.of(new LogRecord(TIMESTAMP, 512L, "Not Found", "10.0.0.1")), result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "2026-11-10T08:15:30;1024;OK",
            "2026-11-10T08:15:30;1024;OK;10.0.0.1;extra"
    })
    void wrongFieldCountIsMalformed(String line) {
        // given a line with 3 or 5 fields

        // when
        Optional<LogRecord> result = parser.parse(line);

        // then
        assertEquals(Optional.empty(), result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"test", "2026-11-10T08:15:30+01:00", "2026-11-10T08:15:30Z"})
    void unparseableTimestampIsMalformed(String timestamp) {
        // given
        String line = timestamp + ";1024;OK;10.0.0.1";

        // when
        Optional<LogRecord> result = parser.parse(line);

        // then
        assertEquals(Optional.empty(), result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", "-5"})
    void nonNumericOrNegativeBytesIsMalformed(String bytes) {
        // given
        String line = "2026-11-10T08:15:30;" + bytes + ";OK;10.0.0.1";

        // when
        Optional<LogRecord> result = parser.parse(line);

        // then
        assertEquals(Optional.empty(), result);
    }
}
