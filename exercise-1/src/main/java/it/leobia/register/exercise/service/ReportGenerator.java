package it.leobia.register.exercise.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import it.leobia.register.exercise.model.LogEntry;
import it.leobia.register.exercise.model.ReportRow;
import it.leobia.register.exercise.parser.LogParser;
import it.leobia.register.exercise.writer.OutputFormat;

public final class ReportGenerator {

    private static final Logger LOGGER = Logger.getLogger(ReportGenerator.class.getName());
    private static final String OK_STATUS = "OK";
    private static final int REQUESTS_INDEX = 0;
    private static final int BYTES_INDEX = 1;

    private static final int SCALE = 2;
    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    private final LogParser parser = new LogParser();

    /**
     * Generates the output report reading line by line the input file.
     * Filters out malformed lines and applies aggregators and order.
     *
     * @throws java.nio.file.NoSuchFileException if {@code input} does not exist
     */
    public void generate(Path input, Path output, LocalDate day, OutputFormat format) throws IOException {
        Map<String, long[]> trafficByIp = new HashMap<>();

        try (BufferedReader reader = Files.newBufferedReader(input, StandardCharsets.UTF_8)) {
            String currentLine;
            long currentLineNumber = 0;

            while ((currentLine = reader.readLine()) != null) {
                currentLineNumber++;
                Optional<LogEntry> entry = parser.parse(currentLine);
                if (entry.isEmpty()) {
                    LOGGER.warning("Skipping malformed line " + currentLineNumber);
                } else if (isIncludedInReport(entry.get(), day)) {
                    // one {requests, bytes} array per IP, created on its first record
                    long[] traffic = trafficByIp.computeIfAbsent(entry.get().ipAddress(), ip -> new long[2]);
                    traffic[REQUESTS_INDEX]++;
                    traffic[BYTES_INDEX] += entry.get().bytes();
                }
            }
        }

        List<ReportRow> rows = toRows(trafficByIp);
        Files.createDirectories(output.toAbsolutePath().getParent());
        try (Writer out = Files.newBufferedWriter(output, StandardCharsets.UTF_8)) {
            format.writer().write(rows, out);
        }
    }

    private static boolean isIncludedInReport(LogEntry entry, LocalDate day) {
        return OK_STATUS.equals(entry.status())
                && (day == null || day.equals(entry.timestamp().toLocalDate()));
    }

    /**
     * One row per IP address. Calculates percentage and sorts the results by:
     * requests DESC, then bytes DESC, then IP address ascending.
     */
    private static List<ReportRow> toRows(Map<String, long[]> trafficByIp) {
        long totalRequests = 0;
        long totalBytes = 0;
        for (long[] traffic : trafficByIp.values()) {
            totalRequests += traffic[REQUESTS_INDEX];
            totalBytes += traffic[BYTES_INDEX];
        }

        List<ReportRow> rows = new ArrayList<>();
        for (Map.Entry<String, long[]> ipTraffic : trafficByIp.entrySet()) {
            long requests = ipTraffic.getValue()[REQUESTS_INDEX];
            long bytes = ipTraffic.getValue()[BYTES_INDEX];
            rows.add(new ReportRow(ipTraffic.getKey(),
                    requests, percentage(requests, totalRequests),
                    bytes, percentage(bytes, totalBytes)));
        }
        rows.sort(Comparator
                .comparingLong(ReportRow::requests)
                .thenComparingLong(ReportRow::bytes)
                .reversed()
                .thenComparing(ReportRow::ipAddress));
        return rows;
    }

    private static BigDecimal percentage(long value, long total) {
        if (total == 0) {
            return BigDecimal.ZERO.setScale(SCALE);
        }
        return BigDecimal.valueOf(value)
                .multiply(HUNDRED)
                .divide(BigDecimal.valueOf(total), SCALE, RoundingMode.HALF_UP);
    }
}
