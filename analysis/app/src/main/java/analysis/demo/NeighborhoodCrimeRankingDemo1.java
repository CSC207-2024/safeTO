package analysis.demo;

import access.data.CrimeDataFetcher;
import access.data.InterfaceDataFetcher;
import access.convert.CrimeDataConverter;
import access.manipulate.CrimeDataProcessor;
import analysis.crimeDataRanking.CrimeDataRanker;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import tech.tablesaw.api.Table;

public class NeighborhoodCrimeRankingDemo1 {

    public static void main(String[] args) {
        // Default parameters
        String neighborhood = "Maple Leaf (29)";
        String specificCrime = "Assault";

        // Check if command-line arguments are provided to override defaults
        if (args.length == 2) {
            neighborhood = args[0];
            specificCrime = args[1];
        } else if (args.length != 0) {
            System.err.println("Usage: java NeighborhoodCrimeRankingDemo1 <neighborhood> <specificCrime>");
            System.exit(1);
        }

        // Use the interface to instantiate the data fetcher
        InterfaceDataFetcher fetcher = new CrimeDataFetcher();
        CrimeDataConverter converter = new CrimeDataConverter();
        CrimeDataProcessor processor = new CrimeDataProcessor();

        // Fetch and convert data
        JsonArray data = fetcher.fetchData();
        if (data == null) {
            System.err.println("Failed to fetch data.");
            System.exit(1);
        }

        Table table = converter.jsonToTable(data);
        if (table == null) {
            System.err.println("Failed to convert data to Table.");
            System.exit(1);
        }

        processor.setTable(table);

        // Rank neighborhoods based on crime data
        JsonObject jsonOutput = rankNeighborhoodCrime(neighborhood, specificCrime, table, processor);

        // Print the JSON result
        Gson gson = new Gson();
        String jsonResult = gson.toJson(jsonOutput);
        System.err.println(jsonResult);
    }

    /**
     * Ranks a neighborhood based on a specific crime type and returns the result in a JSON object.
     *
     * @param neighborhood The name of the neighborhood to rank.
     * @param specificCrime The specific crime type to consider.
     * @param table The processed crime data table.
     * @param processor The processor used to manipulate and filter the data.
     * @return A JSON object containing the neighborhood's ranking and safety level.
     */
    private static JsonObject rankNeighborhoodCrime(String neighborhood, String specificCrime, Table table, CrimeDataProcessor processor) {
        CrimeDataRanker ranker = new CrimeDataRanker(processor);
        JsonObject jsonOutput = new JsonObject();
        jsonOutput.addProperty("neighborhood", neighborhood);

        int totalNeighborhoods = table.stringColumn("NEIGHBOURHOOD_140").unique().size();
        int ranking = ranker.getSpecificCrimeNeighborhoodRanking(specificCrime, neighborhood);

        String safetyLevel;
        if (ranking != -1) {
            safetyLevel = ranker.getSafetyLevel(ranking, totalNeighborhoods);
            jsonOutput.addProperty("specificCrimeRanking", ranking);
            jsonOutput.addProperty("crimeType", specificCrime);
            jsonOutput.addProperty("safetyLevel", safetyLevel);
            System.err.println("The ranking of neighborhood '" + neighborhood + "' by specific crime ('" + specificCrime
                    + "') is: " + ranking);
            System.err.println("Safety level: " + safetyLevel);
        } else {
            safetyLevel = "Not applicable";
            jsonOutput.addProperty("specificCrimeRanking", "Not found");
            jsonOutput.addProperty("crimeType", specificCrime);
            jsonOutput.addProperty("safetyLevel", safetyLevel);
            System.err.println("The neighborhood '" + neighborhood
                    + "' is not found in the ranking for the specific crime ('" + specificCrime + "').");
        }

        return jsonOutput;
    }
}
