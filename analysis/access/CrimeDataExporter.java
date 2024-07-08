package analysis.access;

import com.google.gson.*;
import tech.tablesaw.api.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import static tech.tablesaw.api.ColumnType.*;

/**
 * A class for exporting analyzed crime data from data table to gson format.
 */
public class CrimeDataExporter {

    /**
     * A helper method converts the given Table of data into a JSON string with dynamic column names.
     *
     * @param table Tablesaw data table.
     * @return A JSON string representing the data.
     */
    private String convertTableToJson(Table table) {
        JsonObject root = new JsonObject();
        JsonArray featuresArray = new JsonArray();

        List<Row> rows = (List<Row>) table.rows();

        for (Row row : rows) {
            JsonObject feature = new JsonObject();
            JsonObject attributes = new JsonObject();

            for (String columnName : table.columnNames()) {
                ColumnType columnType = table.column(columnName).type();
                if (columnType.equals(STRING)) {
                    attributes.addProperty(columnName, row.getString(columnName));
                } else if (columnType.equals(INTEGER)) {
                    attributes.addProperty(columnName, row.getInt(columnName));
                } else if (columnType.equals(DOUBLE)) {
                    attributes.addProperty(columnName, row.getDouble(columnName));
                } else {
                    attributes.addProperty(columnName, row.getObject(columnName).toString());
                }
            }
            feature.add("attributes", attributes);
            featuresArray.add(feature);
        }

        root.add("features", featuresArray);
        Gson gson = new Gson();
        return gson.toJson(root);
    }

    /**
     * A method exports the given Table of data to a JSON file at the specified path.
     *
     * @param table Tablesaw data table.
     * @param outputPath The path where the JSON output should be saved.
     */
    public void writeToJson(Table table, String outputPath) {
        String jsonString = convertTableToJson(table);
        try (FileWriter file = new FileWriter(outputPath)) {
            file.write(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
