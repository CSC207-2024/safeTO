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

public class AutoTheftIncidentFetcher implements IncidentFetcherInterface<AutoTheftData> {
    private final CrimeDataFetcher dataFetcher;
    private final CrimeDataConverter dataConverter;
    private final CrimeDataProcessor dataProcessor;

    public AutoTheftIncidentFetcher(CrimeDataFetcher dataFetcher, CrimeDataConverter dataConverter, CrimeDataProcessor dataProcessor) {
        this.dataFetcher = dataFetcher;
        this.dataConverter = dataConverter;
        this.dataProcessor = dataProcessor;
    }

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

    private String getStringValue(Table table, String columnName, int rowIndex) {
        if (table.column(columnName) instanceof TextColumn) {
            return table.textColumn(columnName).get(rowIndex);
        } else {
            return table.stringColumn(columnName).get(rowIndex);
        }
    }
}
