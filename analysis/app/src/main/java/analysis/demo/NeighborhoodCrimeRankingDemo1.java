package analysis.demo;

import access.data.CrimeDataFetcher;
import access.convert.CrimeDataConverter;
import access.manipulate.CrimeDataProcessor;
import analysis.crimeDataRanking.CrimeDataRanker;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import tech.tablesaw.api.Table;

public class NeighborhoodCrimeRankingDemo1 {

    public static void main(String[] args) {
        // Predefined parameters
        String neighborhood = "Maple Leaf (29)";
        String specificCrime = "Assault";

        CrimeDataFetcher fetcher = new CrimeDataFetcher();
        CrimeDataConverter converter = new CrimeDataConverter();
        CrimeDataProcessor processor = new CrimeDataProcessor();

        // Fetch and convert data
        JsonArray data = fetcher.fetchData();
        Table table = converter.jsonToTable(data);
        processor.setTable(table);

        // Create an instance of CrimeDataRanker
        CrimeDataRanker ranker = new CrimeDataRanker(processor);




        Gson gson = new Gson();
        JsonObject jsonOutput = new JsonObject();
        jsonOutput.addProperty("neighborhood", neighborhood);

        int totalNeighborhoods = table.stringColumn("NEIGHBOURHOOD_140").unique().size();

        // Rank neighborhoods by specific crime and get the ranking of the specific neighborhood
        int ranking = ranker.getSpecificCrimeNeighborhoodRanking(specificCrime, neighborhood);
        String safetyLevel;
        if (ranking != -1) {
            safetyLevel = ranker.getSafetyLevel(ranking, totalNeighborhoods);
            jsonOutput.addProperty("specificCrimeRanking", ranking);
            jsonOutput.addProperty("crimeType", specificCrime);
            jsonOutput.addProperty("safetyLevel", safetyLevel);
            System.out.println("The ranking of neighborhood '" + neighborhood + "' by specific crime ('" + specificCrime + "') is: " + ranking);
            System.out.println("Safety level: " + safetyLevel);
        } else {
            safetyLevel = "Not applicable";
            jsonOutput.addProperty("specificCrimeRanking", "Not found");
            jsonOutput.addProperty("crimeType", specificCrime);
            jsonOutput.addProperty("safetyLevel", safetyLevel);
            System.out.println("The neighborhood '" + neighborhood + "' is not found in the ranking for the specific crime ('" + specificCrime + "').");
        }

        String jsonResult = gson.toJson(jsonOutput);
        System.out.println(jsonResult);
    }
}
