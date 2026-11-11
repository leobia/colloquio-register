package it.leobia.register.exercise.writer;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import it.leobia.register.exercise.model.ReportRow;

public interface ReportWriter {

    /***
    * Writes the report rows in one output format. Implementations do not close the {@link Writer}
    * @param rows
    * @param out
    * @throws IOException
    */
    void write(List<ReportRow> rows, Writer out) throws IOException;
}
