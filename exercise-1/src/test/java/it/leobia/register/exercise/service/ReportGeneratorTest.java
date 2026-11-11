package it.leobia.register.exercise.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.api.io.TempDir;

import it.leobia.register.exercise.writer.OutputFormat;

class ReportGeneratorTest {

    private static final String LOG = """
            2026-11-10T08:00:00;100;OK;10.0.0.1
            2026-11-10T09:00:00;300;OK;10.0.0.2
            2026-11-10T10:00:00;100;OK;10.0.0.1
            2026-11-10T11:00:00;999;Not Found;10.0.0.2
            not a log line
            2026-11-11T08:00:00;200;OK;10.0.0.2
            """;

    private final ReportGenerator generator = new ReportGenerator();

    @TempDir
    Path tempDir;

    @Test
    void logIsTurnedIntoCsvReport() throws IOException {
        // given
        Path input = Files.writeString(tempDir.resolve("requests.log"), LOG, StandardCharsets.UTF_8);
        Path output = tempDir.resolve("reports").resolve("ipaddr.csv");

        // when
        generator.generate(input, output, null, OutputFormat.CSV);

        // then
        assertEquals("ip_address,requests,requests_pct,bytes,bytes_pct\r\n"
                + "10.0.0.2,2,50.00,500,71.43\r\n"
                + "10.0.0.1,2,50.00,200,28.57\r\n", Files.readString(output, StandardCharsets.UTF_8));
    }

    @Test
    void onlyRecordsOfTheGivenDayAreReported() throws IOException {
        // given
        Path input = Files.writeString(tempDir.resolve("requests.log"), LOG, StandardCharsets.UTF_8);
        Path output = tempDir.resolve("ipaddr.csv");

        // when
        generator.generate(input, output, LocalDate.of(2026, 11, 10), OutputFormat.CSV);

        // then
        assertEquals("ip_address,requests,requests_pct,bytes,bytes_pct\r\n"
                + "10.0.0.1,2,66.67,200,40.00\r\n"
                + "10.0.0.2,1,33.33,300,60.00\r\n", Files.readString(output, StandardCharsets.UTF_8));
    }

    @Test
    void zeroTotalBytesGiveZeroBytesPercentages() throws IOException {
        // given
        Path input = Files.writeString(tempDir.resolve("requests.log"), """
                2026-11-10T08:00:00;0;OK;10.0.0.1
                2026-11-10T09:00:00;0;OK;10.0.0.2
                """, StandardCharsets.UTF_8);
        Path output = tempDir.resolve("ipaddr.csv");

        // when
        generator.generate(input, output, null, OutputFormat.CSV);

        // then
        assertEquals("ip_address,requests,requests_pct,bytes,bytes_pct\r\n"
                + "10.0.0.1,1,50.00,0,0.00\r\n"
                + "10.0.0.2,1,50.00,0,0.00\r\n", Files.readString(output, StandardCharsets.UTF_8));
    }

    @Test
    void missingInputFileThrowsNoSuchFileException() {
        // given
        Path input = tempDir.resolve("missing.log");
        Path output = tempDir.resolve("ipaddr.csv");

        // when
        Executable generate = () -> generator.generate(input, output, null, OutputFormat.CSV);

        // then
        assertThrows(NoSuchFileException.class, generate);
    }
}
