package access;

import tech.tablesaw.api.Table;
import org.jfree.chart.JFreeChart;

/**
 * An interface for exporting objects.
 */
public interface Exportable {
    void writeToJson(Table table, String outputPath);
    void exportToSVG(JFreeChart chart, String outputPath);
}
