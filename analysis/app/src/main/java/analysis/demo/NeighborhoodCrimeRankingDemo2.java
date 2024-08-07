package analysis.demo;

import analysis.crimeDataRanking.NeighborhoodCrimeRankingResult;
import analysis.facade.CrimeAnalysisFacade;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class NeighborhoodCrimeRankingDemo2 {

    public static void main(String[] args) {
        try {
            // Predefined parameters
            String neighborhood = "Willowdale East (51)";

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

            // Print the results
            System.out.println("The ranking of neighborhood '" + result.getNeighborhood() + "' by total crime is: " + result.getRanking());
            System.out.println("Safety level: " + result.getSafetyLevel());

            String jsonResult = gson.toJson(jsonOutput);
            System.out.println(jsonResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
