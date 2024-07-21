package analysis.carTheft;

import access.convert.CrimeDataConverter;
import access.data.CrimeDataFetcher;
import access.manipulate.CrimeDataProcessor;
import analysis.interfaces.IncidentFetcherInterface;
import com.google.gson.JsonArray;
import tech.tablesaw.api.Row;
import tech.tablesaw.api.Table;

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
        JsonArray data = dataFetcher.fetchData();
        if (data == null) {
            return autoTheftDataList;
        }

        Table table = dataConverter.jsonToTable(data);
        if (table == null) {
            return autoTheftDataList;
        }

        // Set the table in the processor
        dataProcessor.setTable(table);

        // Select only the necessary columns
        ArrayList<String> selectedColumns = new ArrayList<>();
        selectedColumns.add("EVENT_UNIQUE_ID");
        selectedColumns.add("OCC_YEAR");
        selectedColumns.add("OCC_MONTH");
        selectedColumns.add("OCC_DAY");
        selectedColumns.add("LAT_WGS84");
        selectedColumns.add("LONG_WGS84");
        selectedColumns.add("MCI_CATEGORY");
        table = dataProcessor.selectColumn(selectedColumns);

        // Filter data by "MCI_CATEGORY" column where value is "Auto Theft"
        Table filteredTable = dataProcessor.filterBy("MCI_CATEGORY", "Auto Theft");

        for (Row row : filteredTable) {
            try {
                String eventUniqueId = row.getString("EVENT_UNIQUE_ID");
                int occYear = row.getInt("OCC_YEAR");
                String occMonth = row.getString("OCC_MONTH");
                int occDay = row.getInt("OCC_DAY");
                double latitude = row.getDouble("LAT_WGS84");
                double longitude = row.getDouble("LONG_WGS84");

                AutoTheftData autoTheftData = new AutoTheftData(
                        eventUniqueId, occYear, occMonth, occDay, "Auto Theft", latitude, longitude);
                autoTheftDataList.add(autoTheftData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return autoTheftDataList;
    }
}
