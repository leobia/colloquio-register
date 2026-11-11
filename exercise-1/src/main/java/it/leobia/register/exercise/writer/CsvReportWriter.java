package it.leobia.register.exercise.writer;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import it.leobia.register.exercise.model.ReportRow;

public final class CsvReportWriter implements ReportWriter {

    private static final String HEADER = "ip_address,requests,requests_pct,bytes,bytes_pct";
    private static final String LINE_END = "\r\n";

    @Override
    public void write(List<ReportRow> rows, Writer out) throws IOException {
        out.write(HEADER + LINE_END);
        for (ReportRow row : rows) {
            out.write(String.join(",",
                    row.ipAddress(),
                    Long.toString(row.requests()),
                    row.requestsPercentage().toPlainString(),
                    Long.toString(row.bytes()),
                    row.bytesPercentage().toPlainString()));
            out.write(LINE_END);
        }
    }
}
