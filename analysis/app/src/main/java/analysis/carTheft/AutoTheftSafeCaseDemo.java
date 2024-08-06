package analysis.carTheft;

import access.convert.CrimeDataConverter;
import access.data.CrimeDataFetcher;
import access.manipulate.CrimeDataProcessor;
import analysis.utils.GeoUtils;
import com.google.gson.Gson;
import tech.tablesaw.api.Row;
import tech.tablesaw.api.Table;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * A demo class for analyzing and displaying auto theft data within a specified radius.
 */
public class AutoTheftSafeCaseDemo {

    /**
     * The main method that runs the demo for analyzing and displaying auto theft data within a specified radius.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        CrimeDataFetcher fetcher = new CrimeDataFetcher();
        CrimeDataConverter converter = new CrimeDataConverter();
        CrimeDataProcessor processor = new CrimeDataProcessor();

        AutoTheftIncidentFetcher autoTheftIncidentFetcher = new AutoTheftIncidentFetcher(fetcher, converter, processor);
        AutoTheftCalculator autoTheftCalculator = new AutoTheftCalculator(autoTheftIncidentFetcher);
        SafeParkingLocationManager safeParkingLocationManager = SafeParkingLocationManager.getInstance();

        double latitude = 43.731901;
        double longitude = -79.271765;
        int radius = 200;
        int threshold = 5; // Set threshold
        int earliestYear = 2016; // Set the earliest year to consider for crime data

        // Get auto theft data for the past year
        List<AutoTheftData> pastYearData = autoTheftCalculator.getCrimeDataWithinRadiusPastYear(latitude, longitude, radius);
        pastYearData.sort(Comparator.comparing(AutoTheftData::getOccYear)
                .thenComparing(AutoTheftData::getOccMonth)
                .thenComparing(AutoTheftData::getOccDay)); // Sort by date

        List<AutoTheftResult.Incident> pastYearIncidents = new ArrayList<>();
        System.out.println("All Auto Theft in the past year within the radius:");
        int index = 1;
        for (AutoTheftData data : pastYearData) {
            double distance = GeoUtils.calculateDistance(latitude, longitude, data.getLatitude(), data.getLongitude());
            String occurDate = data.getOccYear() + "-" + data.getOccMonth() + "-" + data.getOccDay();
            pastYearIncidents.add(new AutoTheftResult.Incident(occurDate, distance));
            System.out.printf("#%d, occur date: %d-%s-%d, distance from you: %.2f meters%n", index++, data.getOccYear(), data.getOccMonth(), data.getOccDay(), distance);
        }

        // Get all auto theft data within the radius from the specified earliest year
        List<AutoTheftData> allData = autoTheftCalculator.getCrimeDataWithinRadius(latitude, longitude, radius, earliestYear);
        allData.sort(Comparator.comparing(AutoTheftData::getOccYear)
                .thenComparing(AutoTheftData::getOccMonth)
                .thenComparing(AutoTheftData::getOccDay));

        List<AutoTheftResult.Incident> allKnownIncidents = new ArrayList<>();
        System.out.println("ALL known Auto Theft within the radius:");
        index = 1;
        for (AutoTheftData data : allData) {
            double distance = GeoUtils.calculateDistance(latitude, longitude, data.getLatitude(), data.getLongitude());
            String occurDate = data.getOccYear() + "-" + data.getOccMonth() + "-" + data.getOccDay();
            allKnownIncidents.add(new AutoTheftResult.Incident(occurDate, distance));
            System.out.printf("#%d, occur date: %d-%s-%d, distance from you: %.2f meters%n", index++, data.getOccYear(), data.getOccMonth(), data.getOccDay(), distance);
        }

        // Calculate the average annual rate of incidents
        double lambda = autoTheftCalculator.calculateAnnualAverageIncidents(allData); // Use the average annual rate of incidents as the λ value
        double probability = autoTheftCalculator.calculatePoissonProbability(lambda, threshold);

        // Create probability message
        String probabilityMessage = String.format("Based on past data, within %dm of radius, there's a %.4f%% chance that auto thefts happen more than %d time(s) within a year.", radius, probability * 100, threshold);

        // Create warning message
        String warning = probability > 0.15 ? "WARNING: Don't park here!" : "Safe to park here!";

        List<SafeParkingSpot> safeSpotsList = new ArrayList<>();

        if (probability > 0.15) {
            System.out.println("WARNING: Don't park here!");

            // Find nearby safe parking spots
            List<Row> safeSpots = safeParkingLocationManager.getNearbySafeLocations(latitude, longitude, radius, 3, threshold);
            System.out.println("Nearby safe parking spots:");
            for (Row spot : safeSpots) {
                double spotLat = spot.getDouble("Latitude");
                double spotLon = spot.getDouble("Longitude");
                String date = spot.getDateTime("Added Time").toLocalDate().toString();
                double spotProbability = spot.getDouble("Probability");
                int spotRadius = spot.getInt("Radius");
                int spotThreshold = spot.getInt("Threshold");

                double spotDistance = GeoUtils.calculateDistance(latitude, longitude, spotLat, spotLon);
                System.out.printf("Safe Spot - Latitude: %.6f, Longitude: %.6f, Distance from you: %.2f meters, Added Time: %s, Probability: %.4f%%, Radius: %d, Threshold: %d%n",
                        spotLat, spotLon, spotDistance, date, spotProbability * 100, spotRadius, spotThreshold);

                safeSpotsList.add(new SafeParkingSpot(spotLat, spotLon, spotDistance, date, spotProbability, spotRadius, spotThreshold));
            }
        } else {
            System.out.println("Safe to park here!");
        }

        // Create result object for JSON output
        AutoTheftResult autoTheftResult = new AutoTheftResult(allData.size(), pastYearIncidents, allKnownIncidents, probability, probabilityMessage, warning, safeSpotsList);

        // Convert result to JSON
        Gson gson = new Gson();
        String jsonResult = gson.toJson(autoTheftResult);

        // Print JSON result
        System.out.println(jsonResult);

        if (probability <= 0.15) {
            // Allow user to add current location to safe parking locations if it's safe
            Scanner scanner = new Scanner(System.in);
            System.out.print("Do you want to add this location to safe parking locations? (yes/no): ");
            String response = scanner.nextLine();
            if (response.equalsIgnoreCase("yes")) {
                safeParkingLocationManager.addOrUpdateSafeLocation(latitude, longitude, probability, radius, threshold);
                System.out.println("Location added to safe parking locations.");
            }
        }

        // Simulate adding 20 safe locations
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            double randomLat = latitude + (random.nextDouble() - 1.0) / 100; // small random variation
            double randomLon = longitude + (random.nextDouble() - 1.0) / 100; // small random variation
            double randomProbability = random.nextDouble() * 0.1; // random probability between 0 and 0.1
            int randomRadius = 200;
            int randomThreshold = random.nextInt(10) + 1; // random threshold between 1 and 10
            safeParkingLocationManager.addOrUpdateSafeLocation(randomLat, randomLon, randomProbability, randomRadius, randomThreshold);
        }

        Table table = safeParkingLocationManager.getSafeLocationsTable();
        System.out.println("Safe Parking Locations:");
        for (int i = 0; i < table.rowCount(); i++) {
            double lat = table.doubleColumn("Latitude").get(i);
            double lon = table.doubleColumn("Longitude").get(i);
            String date = table.dateTimeColumn("Added Time").get(i).toLocalDate().toString();
            double prob = table.doubleColumn("Probability").get(i);
            int rad = table.intColumn("Radius").get(i);
            int thresh = table.intColumn("Threshold").get(i);
            System.out.printf("Latitude: %.6f, Longitude: %.6f, Added Time: %s, Probability: %.4f%%, Radius: %d, Threshold: %d%n",
                    lat, lon, date, prob * 100, rad, thresh);
        }
    }
}
