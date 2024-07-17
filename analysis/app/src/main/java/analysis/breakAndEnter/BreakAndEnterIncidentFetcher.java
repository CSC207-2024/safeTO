package analysis.breakAndEnter;

import access.convert.CrimeDataConverter;
import access.data.CrimeDataFetcher;
import access.manipulate.CrimeDataProcessor;
import analysis.IncidentFetcherInterface;
import com.google.gson.JsonArray;
//import org.json.JSONArray;
import tech.tablesaw.api.Table;
import tech.tablesaw.api.TextColumn;

import java.util.ArrayList;
import java.util.List;

/**
 * A class for fetching and processing break and enter incident data.
 */
public class BreakAndEnterIncidentFetcher implements IncidentFetcherInterface<BreakAndEnterData> {
    private final CrimeDataFetcher dataFetcher;
    private final CrimeDataConverter dataConverter;
    private final CrimeDataProcessor dataProcessor;

    /**
     * Constructs a BreakAndEnterIncidentFetcher with the specified data fetcher, data converter, and data processor.
     *
     * @param dataFetcher    The data fetcher for fetching raw data.
     * @param dataConverter  The data converter for converting raw data to Table format.
     * @param dataProcessor  The data processor for filtering and processing data.
     */
    public BreakAndEnterIncidentFetcher(CrimeDataFetcher dataFetcher, CrimeDataConverter dataConverter, CrimeDataProcessor dataProcessor) {
        this.dataFetcher = dataFetcher;
        this.dataConverter = dataConverter;
        this.dataProcessor = dataProcessor;
    }

    /**
     * Fetches break and enter incident data, converts it to a Table, and processes it to filter break and enter records.
     *
     * @return A list of break and enter data records.
     */
    @Override
    public List<BreakAndEnterData> fetchCrimeData() {
        List<BreakAndEnterData> breakAndEnterDataList = new ArrayList<>();
        JsonArray data = dataFetcher.fetchData();
        if (data == null) {
            return breakAndEnterDataList;
        }

        Table table = dataConverter.jsonToTable(data);
        if (table == null) {
            return breakAndEnterDataList;
        }

        // Set the table in the processor
        dataProcessor.setTable(table);

        // Filter data by "MCI_CATEGORY" column where value is "Break and Enter"
        Table filteredTable = dataProcessor.filterBy("MCI_CATEGORY", "Break and Enter");

        for (int i = 0; i < filteredTable.rowCount(); i++) {
            try {
                String eventUniqueId = getStringValue(filteredTable, "EVENT_UNIQUE_ID", i);
                int occYear = filteredTable.intColumn("OCC_YEAR").get(i);
                String occMonth = getStringValue(filteredTable, "OCC_MONTH", i);
                int occDay = filteredTable.intColumn("OCC_DAY").get(i);
                double latitude = filteredTable.doubleColumn("LAT_WGS84").get(i);
                double longitude = filteredTable.doubleColumn("LONG_WGS84").get(i);

                BreakAndEnterData breakAndEnterData = new BreakAndEnterData(
                        eventUniqueId, occYear, occMonth, occDay, "Break and Enter", latitude, longitude);
                breakAndEnterDataList.add(breakAndEnterData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return breakAndEnterDataList;
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
