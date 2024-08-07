package access.utils;

import access.data.CrimeDataFetcher;
import access.convert.CrimeDataConverter;
import com.google.gson.JsonArray;
import tech.tablesaw.api.Table;

public class CrimeDataUpdate {

    public static Table updateAndFetchData() {
        // Update the cache with the latest data and get new data
        CacheDataUpdater cacheDataUpdater = new CacheDataUpdater("../cache/");
        cacheDataUpdater.updateCache();

        // Fetch and convert data
        CrimeDataFetcher fetcher = new CrimeDataFetcher();
        CrimeDataConverter converter = new CrimeDataConverter();

        JsonArray data = fetcher.fetchData();
        return converter.jsonToTable(data);
    }

    public static Table getNewDataAsTable() {
        // Update the cache with the latest data and get new data
        CacheDataUpdater cacheDataUpdater = new CacheDataUpdater("../cache/");
        JsonArray newData = cacheDataUpdater.updateCache();

        // Convert new data to Table
        CrimeDataConverter converter = new CrimeDataConverter();
        return converter.jsonToTable(newData);
    }

    public static void main(String[] args) {
        // Update and fetch the latest data
        Table updatedTable = updateAndFetchData();
        Table newDataTable = getNewDataAsTable();

        // Print the updated table
        System.err.println("Updated Table:");
        System.err.println(updatedTable.print());

        // Print the new data table
        System.err.println("New Data Table:");
        System.err.println(newDataTable.print());
    }
}
