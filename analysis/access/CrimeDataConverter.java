package analysis.access;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import tech.tablesaw.api.Table;
import tech.tablesaw.io.csv.CsvReadOptions;
import java.io.StringReader;
import com.google.gson.*;
import tech.tablesaw.api.*;
import java.util.List;
import static tech.tablesaw.api.ColumnType.*;

/**
 * A class that is responsible for converting input data into other formats.
 */
public class CrimeDataConverter {

    private final String[] jsonKeys = {
            "EVENT_UNIQUE_ID", "OCC_YEAR", "OCC_MONTH", "OCC_DAY", "OCC_DOW", "OCC_HOUR",
            "LAT_WGS84", "LONG_WGS84", "NEIGHBOURHOOD_158", "HOOD_158", "PREMISES_TYPE", "MCI_CATEGORY"
    };

    /**
     * A helper method converts a JSONArray of JSONObjects into a CSV-formatted
     * StringBuilder.
     * Each JSONObject in the JSONArray has a nested JSONObject under the
     * "attributes" key
     * which contains the actual data to be converted into CSV format.
     *
     * @param data The JSONArray to be converted into CSV format.
     * @return StringBuilder containing the CSV formatted data.
     * @throws JSONException If an error occurs during the parsing of the JSON data.
     */
    private StringBuilder jsonToString(JSONArray data) throws JSONException {
        StringBuilder builder = new StringBuilder();
        builder.append(String.join(",", jsonKeys)).append("\n");
        for (int i = 0; i < data.length(); i++) {
            JSONObject obj = data.getJSONObject(i);
            appendRow(builder, obj);
        }
        return builder;
    }

    /**
     * A private helper method which appends a row to the StringBuilder in CSV
     * format.
     *
     * @param builder The StringBuilder to which the CSV row will be appended.
     * @param obj     The JSONObject containing the data to be appended as a CSV
     *                row.
     */
    private void appendRow(StringBuilder builder, JSONObject obj) {
        for (String key : jsonKeys) {
            builder.append(obj.optString(key, "")).append(",");
        }
        builder.setLength(builder.length() - 1);
        builder.append("\n");
    }

    /**
     * Converts a CSV formatted StringBuilder into a Tablesaw Table.
     *
     * @param data The JSONArray to be converted into Table
     * @return Table containing the data represented by the StringBuilder.
     */
    public Table jsonToTable(JSONArray data) {

        StringBuilder builderData = null;
        try {
            builderData = jsonToString(data);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        StringReader reader = new StringReader(builderData.toString());
        CsvReadOptions options = CsvReadOptions.builder(reader)
                .header(true)
                .separator(',')
                .build();

        try {
            return Table.read().usingOptions(options);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * A method converts the given Table of data into a JSON string with dynamic column names.
     *
     * @param table Tablesaw data table.
     * @return A JSON string representing the data.
     */
    public String tableToJson(Table table) {
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

}
