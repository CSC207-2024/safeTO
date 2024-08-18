package analysis.facade;

import analysis.breakAndEnter.*;
import access.data.InterfaceDataFetcher;
import access.convert.CrimeDataConverter;
import access.manipulate.CrimeDataProcessor;
import analysis.utils.GeoUtils;
import com.google.gson.JsonArray;
import tech.tablesaw.api.Table;
import tech.tablesaw.api.TextColumn;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A facade class that provides a simplified interface to the break and enter analysis system.
 */
public class BreakAndEnterFacade {

    private final BreakAndEnterCalculator breakAndEnterCalculator;

    // Constructor now accepts an InterfaceDataFetcher
    public BreakAndEnterFacade(InterfaceDataFetcher dataFetcher, CrimeDataConverter converter, CrimeDataProcessor processor) {
        List<BreakAndEnterData> breakAndEnterData = fetchAndProcessData(dataFetcher, converter, processor);
        this.breakAndEnterCalculator = new BreakAndEnterCalculator(breakAndEnterData);
    }


    private List<BreakAndEnterData> fetchAndProcessData(InterfaceDataFetcher fetcher, CrimeDataConverter converter, CrimeDataProcessor processor) {
        List<BreakAndEnterData> breakAndEnterDataList = new ArrayList<>();
        JsonArray data = fetcher.fetchData();
        if (data == null) {
            return breakAndEnterDataList;
        }

        Table table = converter.jsonToTable(data);
        if (table == null) {
            return breakAndEnterDataList;
        }

        // Set the table in the processor
        processor.setTable(table);

        // Filter data by "MCI_CATEGORY" column where value is "Break and Enter"
        Table filteredTable = processor.filterBy("MCI_CATEGORY", "Break and Enter");

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

    private String getStringValue(Table table, String columnName, int rowIndex) {
        if (table.column(columnName) instanceof TextColumn) {
            return table.textColumn(columnName).get(rowIndex);
        } else {
            return table.stringColumn(columnName).get(rowIndex);
        }
    }

    public BreakAndEnterResult analyze(double latitude, double longitude, int radius, int threshold) {
        List<BreakAndEnterData> pastYearData = breakAndEnterCalculator.getCrimeDataWithinRadiusPastYear(latitude, longitude, radius);
        List<BreakAndEnterData> allKnownData = breakAndEnterCalculator.getCrimeDataWithinRadius(latitude, longitude, radius);

        pastYearData.sort(Comparator.comparing(BreakAndEnterData::getOccYear)
                .thenComparing(BreakAndEnterData::getOccMonth)
                .thenComparing(BreakAndEnterData::getOccDay));

        allKnownData.sort(Comparator.comparing(BreakAndEnterData::getOccYear)
                .thenComparing(BreakAndEnterData::getOccMonth)
                .thenComparing(BreakAndEnterData::getOccDay));

        List<BreakAndEnterResult.Incident> pastYearIncidents = new ArrayList<>();
        for (BreakAndEnterData data : pastYearData) {
            double distance = GeoUtils.calculateDistance(latitude, longitude, data.getLatitude(), data.getLongitude());
            String occurDate = data.getOccYear() + "-" + data.getOccMonth() + "-" + data.getOccDay();
            pastYearIncidents.add(new BreakAndEnterResult.Incident(occurDate, distance));
        }

        List<BreakAndEnterResult.Incident> allKnownIncidents = new ArrayList<>();
        for (BreakAndEnterData data : allKnownData) {
            double distance = GeoUtils.calculateDistance(latitude, longitude, data.getLatitude(), data.getLongitude());
            String occurDate = data.getOccYear() + "-" + data.getOccMonth() + "-" + data.getOccDay();
            allKnownIncidents.add(new BreakAndEnterResult.Incident(occurDate, distance));
        }

        double lambda = breakAndEnterCalculator.calculateAnnualAverageIncidents(allKnownData);
        double probability = breakAndEnterCalculator.calculatePoissonProbability(lambda, threshold);

        String probabilityMessage = String.format("Based on past data, within %dm of radius, there's a %.1f%% chance that break and enters happen more than %d time(s) within a year.", radius, probability * 100, threshold);
        String warning = probability > 0.15 ? "WARNING: Don't live here!" : "Safe to live here!";

        return new BreakAndEnterResult(pastYearIncidents, allKnownIncidents, probability, probabilityMessage, warning);
    }
}
