package analysis.demo;

import analysis.carTheft.AutoTheftResult;
import analysis.facade.CrimeAnalysisFacade;
import analysis.carTheft.SafeParkingLocationManager;
import com.google.gson.Gson;

import java.util.Random;

/**
 * A demo class for analyzing and displaying auto theft data within a specified
 * radius and determining if the location is safe for parking.
 */
public class AutoTheftSafeCaseDemo {

    /**
     * The main method that runs the demo for analyzing and displaying auto theft
     * data within a specified radius and determining if the location is safe for parking.
     *
     * @param args Command-line arguments (latitude, longitude, radius, threshold,
     *             earliestYear).
     */
    public static void main(String[] args) {
        double latitude = 43.731901;
        double longitude = -79.271765;
        int radius = 200;
        int threshold = 5; // Set threshold
        int earliestYear = 2016; // Set the earliest year to consider for crime data

        if (args.length == 5) {
            latitude = Double.parseDouble(args[0]);
            longitude = Double.parseDouble(args[1]);
            radius = Integer.parseInt(args[2]);
            threshold = Integer.parseInt(args[3]);
            earliestYear = Integer.parseInt(args[4]);
        } else if (args.length != 0) {
            System.err.println(
                    "Usage: java AutoTheftSafeCaseDemo <latitude> <longitude> <radius> <threshold> <earliestYear>");
            System.exit(1);
        }

        // Use CrimeAnalysisFacade to analyze auto theft data
        CrimeAnalysisFacade facade = new CrimeAnalysisFacade();
        AutoTheftResult result = facade.analyzeAutoTheft(latitude, longitude, radius, threshold, earliestYear);

        // Print the results
        System.err.println("All Auto Theft in the past year within the radius:");
        int index = 1;
        for (AutoTheftResult.Incident incident : result.getPastYearIncidents()) {
            System.out.printf("#%d, occur date: %s, distance from you: %.2f meters%n", index++, incident.occurDate, incident.distance);
        }

        System.err.println("ALL known Auto Theft within the radius:");
        index = 1;
        for (AutoTheftResult.Incident incident : result.getAllKnownIncidents()) {
            System.out.printf("#%d, occur date: %s, distance from you: %.2f meters%n", index++, incident.occurDate, incident.distance);
        }

        System.err.println(result.getProbabilityMessage());
        System.err.println(result.getWarning());

        // Directly use SafeParkingLocationManager for safe parking location management
        if (result.getProbability() <= 0.15) {
            // Add current location to safe parking locations if it's safe
            SafeParkingLocationManager.getInstance().addOrUpdateSafeLocation(latitude, longitude, result.getProbability(), radius, threshold);
            System.err.println("Location added to safe parking locations.");
        }

        // Simulate adding 5 safe locations
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            double randomLat = latitude + (random.nextDouble() - 1.0) / 100; // small random variation
            double randomLon = longitude + (random.nextDouble() - 1.0) / 100; // small random variation
            double randomProbability = random.nextDouble() * 0.1; // random probability between 0 and 0.1
            int randomRadius = 200;
            int randomThreshold = random.nextInt(10) + 1; // random threshold between 1 and 10

            // Add the simulated safe locations
            SafeParkingLocationManager.getInstance().addOrUpdateSafeLocation(randomLat, randomLon, randomProbability, randomRadius, randomThreshold);
        }

        // Create and print JSON result
        Gson gson = new Gson();
        String jsonResult = gson.toJson(result);
        System.err.println(jsonResult);
    }
}
