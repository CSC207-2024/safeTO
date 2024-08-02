package access.convert;

import com.google.gson.*;
import tech.tablesaw.api.Table;
import tech.tablesaw.api.ColumnType;
import tech.tablesaw.api.Row;
import tech.tablesaw.io.csv.CsvReadOptions;

import java.io.StringReader;

/**
 * A class that is responsible for converting input data into other formats.
 */
public class CrimeDataConverter {

    private final String[] jsonKeys = {
            "EVENT_UNIQUE_ID", "OCC_YEAR", "OCC_MONTH", "OCC_DAY", "OCC_DOW", "OCC_HOUR",
            "LAT_WGS84", "LONG_WGS84", "NEIGHBOURHOOD_140", "HOOD_140", "PREMISES_TYPE", "MCI_CATEGORY"
    };

    /**
     * A helper method converts a JsonArray of JsonObjects into a CSV-formatted
     * StringBuilder.
     * Each JsonObject in the JsonArray has a nested JsonObject under the
     * "attributes" key
     * which contains the actual data to be converted into CSV format.
     *
     * @param data The JsonArray to be converted into CSV format.
     * @return StringBuilder containing the CSV formatted data.
     */
    private StringBuilder jsonToString(JsonArray data) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.join(",", jsonKeys)).append("\n");
        for (JsonElement element : data) {
            JsonObject obj = element.getAsJsonObject();
            appendRow(builder, obj);
        }
        return builder;
    }

    /**
     * A private helper method which appends a row to the StringBuilder in CSV
     * format.
     *
     * @param builder The StringBuilder to which the CSV row will be appended.
     * @param obj     The JsonObject containing the data to be appended as a CSV
     *                row.
     */
    private void appendRow(StringBuilder builder, JsonObject obj) {
        JsonObject attributes = obj.has("attributes") ? obj.getAsJsonObject("attributes") : obj;

        for (String key : jsonKeys) {
            builder.append(attributes.has(key) ? attributes.get(key).getAsString() : "").append(",");
        }
        builder.setLength(builder.length() - 1);
        builder.append("\n");
    }

    /**
     * Converts a CSV formatted StringBuilder into a Tablesaw Table.
     *
     * @param data The JsonArray to be converted into Table
     * @return Table containing the data represented by the StringBuilder.
     */
    public Table jsonToTable(JsonArray data) {
        StringBuilder builderData = jsonToString(data);
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
        JsonArray attributesArray = new JsonArray();

        Iterable<Row> rows = table;

        for (Row row : rows) {
            JsonObject attributes = new JsonObject();

            for (String columnName : table.columnNames()) {
                ColumnType columnType = table.column(columnName).type();
                if (columnType.equals(ColumnType.STRING)) {
                    attributes.addProperty(columnName, row.getString(columnName));
                } else if (columnType.equals(ColumnType.INTEGER)) {
                    attributes.addProperty(columnName, row.getInt(columnName));
                } else if (columnType.equals(ColumnType.DOUBLE)) {
                    attributes.addProperty(columnName, row.getDouble(columnName));
                } else {
                    attributes.addProperty(columnName, row.getObject(columnName).toString());
                }
            }
            attributesArray.add(attributes);
        }

        root.add("attributes", attributesArray);
        Gson gson = new Gson();
        return gson.toJson(root);
    }

    /**
     * A helper method that changes the key of a JsonString.
     *
     * @param jsonString The JSON string to be modified.
     * @return A JSON string with modified keys.
     */
    public String changeJsonKeys(String jsonString) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);

        JsonArray attributes = jsonObject.getAsJsonArray("attributes");

        for (JsonElement element : attributes) {
            JsonObject obj = element.getAsJsonObject();
            changeKey(obj, "Count [MCI_CATEGORY]", "INCIDENTS");
        }

        Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();
        return prettyGson.toJson(jsonObject);
    }

    /**
     * A private helper method that changes the key of a JsonObject.
     *
     * @param obj     The JsonObject to be modified.
     * @param oldKey  The old key to be replaced.
     * @param newKey  The new key to replace the old key.
     */
    private void changeKey(JsonObject obj, String oldKey, String newKey) {
        if (obj.has(oldKey)) {
            JsonElement value = obj.remove(oldKey);
            obj.add(newKey, value);
        }
    }
}

