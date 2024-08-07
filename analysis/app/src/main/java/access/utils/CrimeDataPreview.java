package access.utils;

import access.data.CrimeDataFetcher;
import access.convert.CrimeDataConverter;
import tech.tablesaw.api.Table;
import com.google.gson.JsonArray;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

public class CrimeDataPreview {

    public static void main(String[] args) {
        // Fetch and convert data
        CrimeDataFetcher fetcher = new CrimeDataFetcher();
        CrimeDataConverter converter = new CrimeDataConverter();

        JsonArray data = fetcher.fetchData();
        Table table = converter.jsonToTable(data);

        // Get the last few rows of the table
        int numRows = 20; // Number of rows to fetch
        Table previewTable = table.last(numRows);

        // Print the preview table
        System.err.println(previewTable.print());
        /*
         * // List unique values in NEIGHBOURHOOD_140 and MCI_CATEGORY
         * Set<String> uniqueNeighborhoods = new TreeSet<>((a, b) -> {
         * try {
         * return Integer.compare(Integer.parseInt(a), Integer.parseInt(b));
         * } catch (NumberFormatException e) {
         * return a.compareTo(b);
         * }
         * });
         * uniqueNeighborhoods.addAll(table.stringColumn("NEIGHBOURHOOD_140").asSet());
         * 
         * Set<String> uniqueMciCategories = new
         * TreeSet<>(table.stringColumn("MCI_CATEGORY").asSet());
         * 
         * // Print unique values
         * System.err.println("Unique NEIGHBOURHOOD_140 values (sorted):");
         * for (String neighborhood : uniqueNeighborhoods) {
         * System.err.println(neighborhood);
         * }
         * 
         * System.err.println("Unique MCI_CATEGORY values:");
         * for (String mciCategory : uniqueMciCategories) {
         * System.err.println(mciCategory);
         * }
         * 
         * // Optionally save unique values to files
         * saveUniqueValuesToFile("unique_neighborhoods.txt", uniqueNeighborhoods);
         * saveUniqueValuesToFile("unique_mci_categories.txt", uniqueMciCategories);
         * }
         * 
         *//**
            * Saves a set of unique values to a file.
            *
            * @param fileName The name of the file to save the values to.
            * @param values   The set of unique values to save.
            *//*
               * private static void saveUniqueValuesToFile(String fileName, Set<String>
               * values) {
               * try (FileWriter writer = new FileWriter(fileName)) {
               * for (String value : values) {
               * writer.write(value + "\n");
               * }
               * } catch (IOException e) {
               * e.printStackTrace();
               * }
               */
    }
}
