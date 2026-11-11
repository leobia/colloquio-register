package it.leobia.register.exercise.writer;

/** The report output modes */
public enum OutputFormat {

    CSV("csv", new CsvReportWriter()),
    JSON("json", new JsonReportWriter());

    private final String extension;
    private final ReportWriter writer;

    OutputFormat(String extension, ReportWriter writer) {
        this.extension = extension;
        this.writer = writer;
    }

    public String extension() {
        return extension;
    }

    public ReportWriter writer() {
        return writer;
    }
}
