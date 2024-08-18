package analysis.demo;

import analysis.crimeDataRanking.NeighborhoodCrimeRankingResult;
import analysis.facade.CrimeAnalysisFacade;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * A demo class for analyzing and displaying the crime ranking of a neighborhood
 * based on total crime data.
 */
public class NeighborhoodCrimeRankingDemo2 {

    /**
     * The main method that runs the demo for analyzing and displaying the crime
     * ranking of a neighborhood based on total crime data.
     *
     * @param args Command-line argument to specify the neighborhood.
     *             If no arguments are provided, the default is "Willowdale East".
     */
    public static void main(String[] args) {
        // Default neighborhood
        String neighborhood = "Willowdale East";

        // Check for command-line arguments to override the default neighborhood
        if (args.length == 1) {
            neighborhood = args[0];
        } else if (args.length > 1) {
            System.err.println("Usage: java NeighborhoodCrimeRankingDemo2 <neighborhood>");
            System.exit(1);
        }

        try {
            // Create an instance of CrimeAnalysisFacade
            CrimeAnalysisFacade facade = new CrimeAnalysisFacade();

            // Fetch the ranking result using the facade
            NeighborhoodCrimeRankingResult result = facade.getNeighborhoodRanking(neighborhood, null);

            // Prepare JSON output
            Gson gson = new Gson();
            JsonObject jsonOutput = new JsonObject();
            jsonOutput.addProperty("neighborhood", result.getNeighborhood());
            jsonOutput.addProperty("totalCrimeRanking", result.getRanking());
            jsonOutput.addProperty("safetyLevel", result.getSafetyLevel());

            // Print the results to console
            System.err.println("The ranking of neighborhood '" + result.getNeighborhood() + "' by total crime is: "
                    + result.getRanking());
            System.err.println("Safety level: " + result.getSafetyLevel());

            String jsonResult = gson.toJson(jsonOutput);
            System.err.println(jsonResult);

        } catch (Exception e) {
            System.err.println("An error occurred while fetching the neighborhood ranking:");
            e.printStackTrace();
        }
    }
}
