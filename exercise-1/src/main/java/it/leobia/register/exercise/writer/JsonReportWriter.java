package it.leobia.register.exercise.writer;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import it.leobia.register.exercise.model.ReportRow;

/** Writes the rows as a JSON array on a single line, one object per row. */
public final class JsonReportWriter implements ReportWriter {

    @Override
    public void write(List<ReportRow> rows, Writer out) throws IOException {
        out.write("[");
        String separator = "";
        for (ReportRow currentRow : rows) {
            out.write(separator);
            out.write(toJson(currentRow));
            separator = ",";
        }
        out.write("]");
    }

    private static String toJson(ReportRow row) {
        return """
                {"ipAddress":"%s","requests":%d,"requestsPercentage":%s,"bytes":%d,"bytesPercentage":%s}"""
                .formatted(row.ipAddress(),
                        row.requests(),
                        row.requestsPercentage().toPlainString(),
                        row.bytes(),
                        row.bytesPercentage().toPlainString());
    }
}
