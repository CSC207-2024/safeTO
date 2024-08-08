package access.export;

import access.convert.CrimeDataConverter;
import tech.tablesaw.api.*;
import java.io.FileWriter;
import java.io.IOException;


/**
 * A class for exporting analyzed crime.
 */
public class CrimeDataExporter implements Exportable {

    private final CrimeDataConverter converter = new CrimeDataConverter();

    /**
     * A method exports the given Table of data to a JSON file at the specified path.
     *
     * @param table Tablesaw data table.
     * @param outputPath The path where the JSON output should be saved.
     */
    @Override
    public void writeToJson(Table table, String outputPath) {
        String jsonString = converter.tableToJson(table);
        try (FileWriter file = new FileWriter(outputPath)) {
            file.write(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Overloaded method to export the given JSON string to a JSON file at the specified path.
     *
     * @param jsonString The JSON string to be written to the file.
     * @param outputPath The path where the JSON output should be saved.
     */
    @Override
    public void writeToJson(String jsonString, String outputPath) {
        // Change the keys in the JSON string
        try (FileWriter file = new FileWriter(outputPath)) {
            file.write(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
