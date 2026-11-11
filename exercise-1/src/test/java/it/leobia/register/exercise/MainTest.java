package it.leobia.register.exercise;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MainTest {

    private static final String LOG = """
            2026-11-10T08:00:00;100;OK;10.0.0.1
            """;

    @TempDir
    Path baseDir;

    @Test
    void dateAndFormatOptionsWriteJsonReport() throws IOException {
        // given
        writeLog();

        // when
        int exitCode = Main.run(new String[] {"--date", "2026-11-10", "--format", "json"}, baseDir);

        // then
        assertEquals(0, exitCode);
        assertTrue(Files.exists(baseDir.resolve("reports").resolve("ipaddr.json")));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            // invalid format
            "--format xml",
            // invalid date
            "--date 10/11/2026",
            // missing value
            "--date"
    })
    void invalidArgumentsGiveError(String arguments) throws IOException {
        // given
        writeLog();

        // when
        int exitCode = Main.run(arguments.split(" "), baseDir);

        // then
        assertEquals(1, exitCode);
    }

    private void writeLog() throws IOException {
        Path logfiles = Files.createDirectories(baseDir.resolve("logfiles"));
        Files.writeString(logfiles.resolve("requests.log"), LOG, StandardCharsets.UTF_8);
    }
}
