package analysis.carTheft;

import access.convert.CrimeDataConverter;
import access.data.CrimeDataFetcher;
import access.manipulate.CrimeDataProcessor;
import analysis.utils.GeoUtils;

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

        System.out.println("All Auto Theft in the past year within the radius:");
        int index = 1;
        for (AutoTheftData data : pastYearData) {
            double distance = GeoUtils.calculateDistance(latitude, longitude, data.getLatitude(), data.getLongitude());
            System.out.printf("#%d, occur date: %d-%s-%d, distance from you: %.2f meters%n", index++, data.getOccYear(), data.getOccMonth(), data.getOccDay(), distance);
        }

        // Get all auto theft data within the radius from the specified earliest year
        List<AutoTheftData> allData = autoTheftCalculator.getCrimeDataWithinRadius(latitude, longitude, radius, earliestYear);
        allData.sort(Comparator.comparing(AutoTheftData::getOccYear)
                .thenComparing(AutoTheftData::getOccMonth)
                .thenComparing(AutoTheftData::getOccDay));

        System.out.println("ALL known Auto Theft within the radius:");
        index = 1;
        for (AutoTheftData data : allData) {
            double distance = GeoUtils.calculateDistance(latitude, longitude, data.getLatitude(), data.getLongitude());
            System.out.printf("#%d, occur date: %d-%s-%d, distance from you: %.2f meters%n", index++, data.getOccYear(), data.getOccMonth(), data.getOccDay(), distance);
        }

        // Calculate the average annual rate of incidents
        double lambda = autoTheftCalculator.calculateAnnualAverageIncidents(allData); // Use the average annual rate of incidents as the λ value
        double probability = autoTheftCalculator.calculatePoissonProbability(lambda, threshold);

        // Display the probability and recommendation
        System.out.printf("Based on past data, within %dm of radius, there's a %.4f%% chance that auto thefts happen more than %d time(s) within a year.%n", radius, probability * 100, threshold);

        if (probability > 0.15) {
            System.out.println("WARNING: Don't park here!");
        } else {
            System.out.println("Safe to park here!");

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
            double randomLat = latitude + (random.nextDouble() - 0.5) / 100; // small random variation
            double randomLon = longitude + (random.nextDouble() - 0.5) / 100; // small random variation
            double randomProbability = random.nextDouble() * 0.1; // random probability between 0 and 0.1
            int randomRadius = 200;
            int randomThreshold = random.nextInt(10) + 1; // random threshold between 1 and 10
            safeParkingLocationManager.addOrUpdateSafeLocation(randomLat, randomLon, randomProbability, randomRadius, randomThreshold);
        }
    }
}
