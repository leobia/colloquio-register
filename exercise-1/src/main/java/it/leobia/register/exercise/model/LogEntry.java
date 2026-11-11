package it.leobia.register.exercise.model;

import java.time.LocalDateTime;

/** Represents a line of the request log file */
public record LogEntry(LocalDateTime timestamp, long bytes, String status, String ipAddress) {}
