package it.leobia.register.exercise.parser;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import it.leobia.register.exercise.model.LogEntry;

/**
 * Parses one line of the requests log file: ({@code TIMESTAMP;BYTES;STATUS;REMOTE_ADDR})
 *
 */
public final class LogParser {

    private static final String SEPARATOR = ";";
    private static final int FIELD_COUNT = 4;


    /**
     * Parse a line of the file the request log file. It applies a basic validation:
     * - checks if the fields are exactly 4
     * - checks if the status is not empty
     * - checks if the ip adress is at least not empty and not containing spaces in between other chars
     * - checks if bytes are parsable and a positive number
     * - checks if the timestamp is parsable
     */
    public Optional<LogEntry> parse(String line) {
        if (line.isBlank()) {
            return Optional.empty();
        }

        String[] fields = line.split(SEPARATOR, -1);
        if (fields.length != FIELD_COUNT) {
            return Optional.empty();
        }

        String status = fields[2].strip();
        String ipAddress = fields[3].strip();
        if (status.isEmpty() || ipAddress.isEmpty() || ipAddress.chars().anyMatch(Character::isWhitespace)) {
            return Optional.empty();
        }

        try {
            LocalDateTime timestamp = LocalDateTime.parse(fields[0].strip());
            long bytes = Long.parseLong(fields[1].strip());
            if (bytes < 0) {
                return Optional.empty();
            }
            return Optional.of(new LogEntry(timestamp, bytes, status, ipAddress));
        } catch (DateTimeParseException | NumberFormatException e) {
            return Optional.empty();
        }
    }
}
