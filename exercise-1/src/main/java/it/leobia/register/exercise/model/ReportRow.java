package it.leobia.register.exercise.model;

import java.math.BigDecimal;

public record ReportRow(
        String ipAddress,
        long requests,
        BigDecimal requestsPercentage,
        long bytes,
        BigDecimal bytesPercentage) {}
