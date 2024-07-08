package analysis.access;

import tech.tablesaw.api.*;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A class for exporting analyzed crime data from data table to gson format.
 */
public class CrimeDataExporter {

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
}
