package analysis;

import access.CrimeDataRanker;
import org.json.JSONArray;
import access.CrimeDataFetcher;
import access.CrimeDataConverter;
import access.CrimeDataProcessor;
import tech.tablesaw.api.Table;

import java.util.Scanner;

public class NeighborhoodCrimeRankingDemo {
    public static void main(String[] args) {
        CrimeDataFetcher fetcher = new CrimeDataFetcher();
        CrimeDataConverter converter = new CrimeDataConverter();
        CrimeDataProcessor processor = new CrimeDataProcessor();

        // Fetch and convert data
        JSONArray data = fetcher.fetchData();
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

        if (specificCrime.isEmpty()) {
            // Rank neighborhoods by total crime and get the ranking of the specific neighborhood
            int ranking = ranker.getNeighborhoodRanking(neighborhood);
            if (ranking != -1) {
                System.out.println("The ranking of neighborhood '" + neighborhood + "' by total crime is: " + ranking);
            } else {
                System.out.println("The neighborhood '" + neighborhood + "' is not found in the ranking.");
            }
        } else {
            // Rank neighborhoods by specific crime and get the ranking of the specific neighborhood
            int ranking = ranker.getSpecificCrimeNeighborhoodRanking(specificCrime, neighborhood);
            if (ranking != -1) {
                System.out.println("The ranking of neighborhood '" + neighborhood + "' by specific crime ('" + specificCrime + "') is: " + ranking);
            } else {
                System.out.println("The neighborhood '" + neighborhood + "' is not found in the ranking for the specific crime ('" + specificCrime + "').");
            }
        }

        scanner.close();
    }
}
