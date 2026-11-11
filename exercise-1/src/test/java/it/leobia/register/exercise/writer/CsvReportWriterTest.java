package it.leobia.register.exercise.writer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

import it.leobia.register.exercise.model.ReportRow;

class CsvReportWriterTest {

    private final CsvReportWriter writer = new CsvReportWriter();

    @Test
    void rowsAreWrittenAfterHeaderWithCrlf() throws IOException {
        // given
        List<ReportRow> rows = List.of(
                new ReportRow("10.0.0.1", 2L, new BigDecimal("66.67"), 1L, new BigDecimal("25.00")),
                new ReportRow("10.0.0.2", 1L, new BigDecimal("33.33"), 3L, new BigDecimal("75.00")));
        StringWriter out = new StringWriter();

        // when
        writer.write(rows, out);

        // then
        assertEquals("ip_address,requests,requests_pct,bytes,bytes_pct\r\n"
                + "10.0.0.1,2,66.67,1,25.00\r\n"
                + "10.0.0.2,1,33.33,3,75.00\r\n", out.toString());
    }

    @Test
    void noRowsGivesHeaderOnly() throws IOException {
        // given
        StringWriter out = new StringWriter();

        // when
        writer.write(List.of(), out);

        // then
        assertEquals("ip_address,requests,requests_pct,bytes,bytes_pct\r\n", out.toString());
    }
}
