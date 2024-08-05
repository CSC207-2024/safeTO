package analysis.crimeDataRanking;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import access.data.CrimeDataFetcher;
import access.convert.CrimeDataConverter;
import access.manipulate.CrimeDataProcessor;
import tech.tablesaw.api.Table;

import java.util.Scanner;

/**
 * A demo class for ranking neighborhoods based on crime data.
 */
public class NeighborhoodCrimeRankingDemo {

    /**
     * The main method that runs the demo for ranking neighborhoods based on crime data.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        CrimeDataFetcher fetcher = new CrimeDataFetcher();
        CrimeDataConverter converter = new CrimeDataConverter();
        CrimeDataProcessor processor = new CrimeDataProcessor();

        // Fetch and convert data
        JsonArray data = fetcher.fetchData();
        Table table = converter.jsonToTable(data);
        processor.setTable(table);

        // Create an instance of CrimeDataRanker
        CrimeDataRanker ranker = new CrimeDataRanker(processor);

        // Read input from the user
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the neighborhood name: ");
        String neighborhood = scanner.nextLine();
        System.out.print("Enter the specific crime type (leave blank for total crime ranking): ");
        String specificCrime = scanner.nextLine();

        Gson gson = new Gson();
        JsonObject jsonOutput = new JsonObject();
        jsonOutput.addProperty("neighborhood", neighborhood);

        if (specificCrime.isEmpty()) {
            // Rank neighborhoods by total crime and get the ranking of the specific neighborhood
            int ranking = ranker.getNeighborhoodRanking(neighborhood);
            if (ranking != -1) {
                jsonOutput.addProperty("totalCrimeRanking", ranking);
                System.out.println("The ranking of neighborhood '" + neighborhood + "' by total crime is: " + ranking);
            } else {
                jsonOutput.addProperty("totalCrimeRanking", "Not found");
                System.out.println("The neighborhood '" + neighborhood + "' is not found in the ranking.");
            }
        } else {
            // Rank neighborhoods by specific crime and get the ranking of the specific neighborhood
            int ranking = ranker.getSpecificCrimeNeighborhoodRanking(specificCrime, neighborhood);
            if (ranking != -1) {
                jsonOutput.addProperty("specificCrimeRanking", ranking);
                jsonOutput.addProperty("crimeType", specificCrime);
                System.out.println("The ranking of neighborhood '" + neighborhood + "' by specific crime ('" + specificCrime + "') is: " + ranking);
            } else {
                jsonOutput.addProperty("specificCrimeRanking", "Not found");
                jsonOutput.addProperty("crimeType", specificCrime);
                System.out.println("The neighborhood '" + neighborhood + "' is not found in the ranking for the specific crime ('" + specificCrime + "').");
            }
        }
/*
        String jsonResult = gson.toJson(jsonOutput);
        System.out.println(jsonResult);

        scanner.close();*/
    }
}
