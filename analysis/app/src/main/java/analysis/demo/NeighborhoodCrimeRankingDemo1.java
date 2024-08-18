package analysis.demo;

import analysis.crimeDataRanking.NeighborhoodCrimeRankingResult;
import analysis.facade.CrimeAnalysisFacade;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * A demo class for analyzing and displaying the crime ranking of a neighborhood
 * based on a specific crime type.
 */
public class NeighborhoodCrimeRankingDemo1 {

    /**
     * The main method that runs the demo for analyzing and displaying the crime
     * ranking of a neighborhood based on a specific crime type.
     *
     * @param args Command-line arguments to specify the neighborhood and crime type.
     *             If no arguments are provided, the defaults are "Maple Leaf" and "Assault".
     */
    public static void main(String[] args) {
        // Default parameters
        String neighborhood = "Maple Leaf";
        String specificCrime = "Assault";

        // Check if command-line arguments are provided to override defaults
        if (args.length == 2) {
            neighborhood = args[0];
            specificCrime = args[1];
        } else if (args.length != 0) {
            System.err.println("Usage: java NeighborhoodCrimeRankingDemo1 <neighborhood> <specificCrime>");
            System.exit(1);
        }

        try {
            // Create an instance of CrimeAnalysisFacade
            CrimeAnalysisFacade facade = new CrimeAnalysisFacade();

            // Fetch the ranking result using the facade
            NeighborhoodCrimeRankingResult result = facade.getNeighborhoodRanking(neighborhood, specificCrime);

            // Prepare JSON output
            Gson gson = new Gson();
            JsonObject jsonOutput = new JsonObject();
            jsonOutput.addProperty("neighborhood", result.getNeighborhood());
            jsonOutput.addProperty("specificCrimeRanking", result.getRanking());
            jsonOutput.addProperty("crimeType", specificCrime);
            jsonOutput.addProperty("safetyLevel", result.getSafetyLevel());

            // Print the results to console
            System.err.println("The ranking of neighborhood '" + result.getNeighborhood() + "' by specific crime ('"
                    + specificCrime + "') is: " + result.getRanking());
            System.err.println("Safety level: " + result.getSafetyLevel());

            String jsonResult = gson.toJson(jsonOutput);
            System.err.println(jsonResult);

        } catch (Exception e) {
            System.err.println("An error occurred while fetching the neighborhood ranking:");
            e.printStackTrace();
        }
    }
}
