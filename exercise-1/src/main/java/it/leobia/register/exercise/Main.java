package it.leobia.register.exercise;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Locale;

import it.leobia.register.exercise.service.ReportGenerator;
import it.leobia.register.exercise.writer.OutputFormat;

public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        System.exit(run(args, Path.of("")));
    }

    /**
     * Reads {@code --date} and {@code --format} by name and generates the report from {@code baseDir}.
     *
     * @return {@code 0} on success, {@code 1} on any error
     */
    static int run(String[] args, Path baseDir) {
        List<String> arguments = List.of(args);
        int dateIndex = arguments.indexOf("--date");
        int formatIndex = arguments.indexOf("--format");

        try {
            LocalDate day = dateIndex == -1 ? null : LocalDate.parse(args[dateIndex + 1]);
            OutputFormat format = formatIndex == -1 ? OutputFormat.CSV : toOutputFormat(args[formatIndex + 1]);

            Path output = baseDir.resolve("reports").resolve("ipaddr." + format.extension());

            new ReportGenerator().generate(baseDir.resolve("logfiles/requests.log"), output, day, format);
            System.out.println("Report written to " + output);
            return 0;
        } catch (IOException | DateTimeParseException | IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Error: " + e.getMessage());
            return 1;
        }
    }

    private static OutputFormat toOutputFormat(String value) {
        return OutputFormat.valueOf(value.toUpperCase(Locale.ROOT));
    }
}
