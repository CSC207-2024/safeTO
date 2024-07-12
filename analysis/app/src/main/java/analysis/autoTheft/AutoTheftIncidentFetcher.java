package analysis.autoTheft;

import access.CrimeDataConverter;
import access.CrimeDataFetcher;
import access.CrimeDataProcessor;
import analysis.IncidentFetcherInterface;
import org.json.JSONArray;
import tech.tablesaw.api.Table;
import tech.tablesaw.api.TextColumn;

import java.util.ArrayList;
import java.util.List;

/**
 * A class for fetching and processing auto theft incident data.
 */
public class AutoTheftIncidentFetcher implements IncidentFetcherInterface<AutoTheftData> {
    private final CrimeDataFetcher dataFetcher;
    private final CrimeDataConverter dataConverter;
    private final CrimeDataProcessor dataProcessor;

    /**
     * Constructs an AutoTheftIncidentFetcher with the specified data fetcher, data converter, and data processor.
     *
     * @param dataFetcher    The data fetcher for fetching raw data.
     * @param dataConverter  The data converter for converting raw data to Table format.
     * @param dataProcessor  The data processor for filtering and processing data.
     */
    public AutoTheftIncidentFetcher(CrimeDataFetcher dataFetcher, CrimeDataConverter dataConverter, CrimeDataProcessor dataProcessor) {
        this.dataFetcher = dataFetcher;
        this.dataConverter = dataConverter;
        this.dataProcessor = dataProcessor;
    }

    /**
     * Fetches auto theft incident data, converts it to a Table, and processes it to filter auto theft records.
     *
     * @return A list of auto theft data records.
     */
    @Override
    public List<AutoTheftData> fetchCrimeData() {
        List<AutoTheftData> autoTheftDataList = new ArrayList<>();
        JSONArray data = dataFetcher.fetchData();
        if (data == null) {
            return autoTheftDataList;
        }

        Table table = dataConverter.jsonToTable(data);
        if (table == null) {
            return autoTheftDataList;
        }

        // Set the table in the processor
        dataProcessor.setTable(table);

        // Filter data by "MCI_CATEGORY" column where value is "Auto Theft"
        Table filteredTable = dataProcessor.filterBy("MCI_CATEGORY", "Auto Theft");

        for (int i = 0; i < filteredTable.rowCount(); i++) {
            try {
                String eventUniqueId = getStringValue(filteredTable, "EVENT_UNIQUE_ID", i);
                int occYear = filteredTable.intColumn("OCC_YEAR").get(i);
                String occMonth = getStringValue(filteredTable, "OCC_MONTH", i);
                int occDay = filteredTable.intColumn("OCC_DAY").get(i);
                double latitude = filteredTable.doubleColumn("LAT_WGS84").get(i);
                double longitude = filteredTable.doubleColumn("LONG_WGS84").get(i);

                AutoTheftData autoTheftData = new AutoTheftData(
                        eventUniqueId, occYear, occMonth, occDay, "Auto Theft", latitude, longitude);
                autoTheftDataList.add(autoTheftData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return autoTheftDataList;
    }

    /**
     * Retrieves a string value from the specified column and row index of a Table.
     *
     * @param table      The Table from which to retrieve the value.
     * @param columnName The name of the column.
     * @param rowIndex   The index of the row.
     * @return The string value from the specified column and row index.
     */
    private String getStringValue(Table table, String columnName, int rowIndex) {
        if (table.column(columnName) instanceof TextColumn) {
            return table.textColumn(columnName).get(rowIndex);
        } else {
            return table.stringColumn(columnName).get(rowIndex);
        }
    }
}
