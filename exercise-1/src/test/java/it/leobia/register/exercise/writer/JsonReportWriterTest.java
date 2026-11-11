package it.leobia.register.exercise.writer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;

import it.leobia.register.exercise.model.ReportRow;

class JsonReportWriterTest {

    private final JsonReportWriter writer = new JsonReportWriter();

    @Test
    void rowsAreWrittenAsArrayOfObjects() throws IOException {
        // given
        List<ReportRow> rows = List.of(
                new ReportRow("10.0.0.1", 2L, new BigDecimal("66.67"), 1L, new BigDecimal("25.00")),
                new ReportRow("10.0.0.2", 1L, new BigDecimal("33.33"), 3L, new BigDecimal("75.00")));
        StringWriter out = new StringWriter();

        // when
        writer.write(rows, out);

        // then
        assertEquals("""
                [{"ipAddress":"10.0.0.1","requests":2,"requestsPercentage":66.67,"bytes":1,"bytesPercentage":25.00},\
                {"ipAddress":"10.0.0.2","requests":1,"requestsPercentage":33.33,"bytes":3,"bytesPercentage":75.00}]""",
                out.toString());
    }

}
