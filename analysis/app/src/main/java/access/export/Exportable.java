package access.export;

import tech.tablesaw.api.Table;
import org.jfree.chart.JFreeChart;

/**
 * An interface for exporting objects.
 */
public interface Exportable {
    void writeToJson(Table table, String outputPath);
    void writeToJson(String jsonString, String outputPath);
}
