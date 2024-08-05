package analysis.crimeDataPreview;

import access.data.CrimeDataFetcher;
import access.convert.CrimeDataConverter;
import tech.tablesaw.api.Table;
import com.google.gson.JsonArray;

public class CrimeDataPreview {

    public static void main(String[] args) {
        // Fetch and convert data
        CrimeDataFetcher fetcher = new CrimeDataFetcher();
        CrimeDataConverter converter = new CrimeDataConverter();

        JsonArray data = fetcher.fetchData();
        Table table = converter.jsonToTable(data);

        // Get the first few rows of the table
        int numRows = 5; // Number of rows to fetch
        Table previewTable = table.first(numRows);

        // Print the preview table
        System.out.println(previewTable.print());
    }
}
