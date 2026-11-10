package it.leobia.register.exercise;

import java.time.LocalDateTime;

/** Represents a line of the request log file */
public record LogRecord(LocalDateTime timestamp, long bytes, String status, String ipAddress) {}
