package analysis.access;

import org.jfree.chart.JFreeChart;
import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.jfree.graphics2d.svg.SVGUtils;
import tech.tablesaw.api.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A class for exporting analyzed crime data from data table to gson format.
 */
public class CrimeDataExporter implements Exportable{

    private final CrimeDataConverter converter = new CrimeDataConverter();

    /**
     * A method exports the given Table of data to a JSON file at the specified path.
     *
     * @param table Tablesaw data table.
     * @param outputPath The path where the JSON output should be saved.
     */
    public void writeToJson(Table table, String outputPath) {
        String jsonString = converter.tableToJson(table);
        try (FileWriter file = new FileWriter(outputPath)) {
            file.write(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * A method that exports the given JFreeChart object to an SVG file at the specified path.
     * @param chart JFreeChart object to be exported.
     * @param outputPath The path where the SVG output should be saved.
     */
    @Override
    public void exportToSVG(JFreeChart chart, String outputPath) {
        SVGGraphics2D svgGraphics2D = new SVGGraphics2D(800, 600);
        chart.draw(svgGraphics2D, new java.awt.geom.Rectangle2D.Double(0, 0, 800, 600));
        try {
            SVGUtils.writeToSVG(new File(outputPath), svgGraphics2D.getSVGElement());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
