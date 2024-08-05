package analysis.breakAndEnter;

import access.convert.CrimeDataConverter;
import access.data.CrimeDataFetcher;
import access.manipulate.CrimeDataProcessor;
import analysis.utils.GeoUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A demo class for analyzing and displaying break and enter data within a specified radius.
 */
public class BreakAndEnterModelDemo {

    /**
     * The main method that runs the demo for analyzing and displaying break and enter data.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        CrimeDataFetcher fetcher = new CrimeDataFetcher();
        CrimeDataConverter converter = new CrimeDataConverter();
        CrimeDataProcessor processor = new CrimeDataProcessor();

        BreakAndEnterIncidentFetcher breakAndEnterIncidentFetcher = new BreakAndEnterIncidentFetcher(fetcher, converter, processor);
        BreakAndEnterCalculator breakAndEnterCalculator = new BreakAndEnterCalculator(breakAndEnterIncidentFetcher);

        double latitude = 43.8062893908425;
        double longitude = -79.1803868891903;
        int radius = 200;
        int threshold = 3; // Set threshold

        // Get break and enter data for the past year
        List<BreakAndEnterData> pastYearData = breakAndEnterCalculator.getCrimeDataWithinRadiusPastYear(latitude, longitude, radius);
        pastYearData.sort(Comparator.comparing(BreakAndEnterData::getOccYear)
                .thenComparing(BreakAndEnterData::getOccMonth)
                .thenComparing(BreakAndEnterData::getOccDay)); // Sort by date

        List<BreakAndEnterResult.Incident> pastYearIncidents = new ArrayList<>();
        for (BreakAndEnterData data : pastYearData) {
            double distance = GeoUtils.calculateDistance(latitude, longitude, data.getLatitude(), data.getLongitude());
            String occurDate = data.getOccYear() + "-" + data.getOccMonth() + "-" + data.getOccDay();
            pastYearIncidents.add(new BreakAndEnterResult.Incident(occurDate, distance));
        }

        // Get all break and enter data within the radius
        List<BreakAndEnterData> allData = breakAndEnterCalculator.getCrimeDataWithinRadius(latitude, longitude, radius);
        allData.sort(Comparator.comparing(BreakAndEnterData::getOccYear)
                .thenComparing(BreakAndEnterData::getOccMonth)
                .thenComparing(BreakAndEnterData::getOccDay));

        List<BreakAndEnterResult.Incident> allKnownIncidents = new ArrayList<>();
        for (BreakAndEnterData data : allData) {
            double distance = GeoUtils.calculateDistance(latitude, longitude, data.getLatitude(), data.getLongitude());
            String occurDate = data.getOccYear() + "-" + data.getOccMonth() + "-" + data.getOccDay();
            allKnownIncidents.add(new BreakAndEnterResult.Incident(occurDate, distance));
        }

        // Calculate the average annual rate of incidents
        double lambda = breakAndEnterCalculator.calculateAnnualAverageIncidents(allData); // Use the average annual rate of incidents as the λ value
        double probability = breakAndEnterCalculator.calculatePoissonProbability(lambda, threshold);

        // Create warning message
        String warning = probability > 0.15 ? "Don't live here!" : "Safe to live here!";

        // Print formatted output
        System.out.println("Total aggregated data: " + allData.size());
        System.out.println("All Break and Enter in the past year within the radius:");
        int index = 1;
        for (BreakAndEnterResult.Incident incident : pastYearIncidents) {
            System.out.printf("#%d, occur date: %s, distance from you: %.2f meters%n", index++, incident.occurDate, incident.distance);
        }
        System.out.println("ALL known Break and Enter within the radius:");
        index = 1;
        for (BreakAndEnterResult.Incident incident : allKnownIncidents) {
            System.out.printf("#%d, occur date: %s, distance from you: %.2f meters%n", index++, incident.occurDate, incident.distance);
        }
        System.out.printf("Based on past data, within %dm of radius, there's a %.2f%% chance that break and enters happen more than %d time(s) within a year.%n", radius, probability * 100, threshold);
        System.out.println(warning);

        // Create result object for JSON output
        BreakAndEnterResult breakAndEnterResult = new BreakAndEnterResult(allData.size(), pastYearIncidents, allKnownIncidents, probability, warning);

        // Convert result to JSON
        Gson gson = new Gson();
        String jsonResult = gson.toJson(breakAndEnterResult);

        // Print JSON result
        System.out.println(jsonResult);
    }
}
