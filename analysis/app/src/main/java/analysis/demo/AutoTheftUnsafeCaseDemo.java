package analysis.demo;

import analysis.carTheft.AutoTheftResult;
import analysis.carTheft.SafeParkingLocationManager;
import analysis.carTheft.SafeParkingSpot;
import analysis.facade.CrimeAnalysisFacade;
import analysis.utils.GeoUtils;
import com.google.gson.Gson;
import tech.tablesaw.api.Row;

import java.util.ArrayList;
import java.util.List;

/**
 * A demo class for analyzing and displaying auto theft data within a specified
 * radius and suggesting alternative safe parking spots if the current location is
 * unsafe.
 */
public class AutoTheftUnsafeCaseDemo {

    /**
     * The main method that runs the demo for analyzing and displaying auto theft
     * data within a specified radius and suggesting alternative safe parking spots if the current location is
     * unsafe.
     *
     * @param args Command-line arguments (latitude, longitude, radius, threshold,
     *             earliestYear).
     */
    public static void main(String[] args) {
        double latitude = 43.731937;
        double longitude = -79.266401;
        int radius = 200;
        int threshold = 5; // Set threshold
        int earliestYear = 2018; // Set the earliest year to consider for crime data

        if (args.length == 5) {
            latitude = Double.parseDouble(args[0]);
            longitude = Double.parseDouble(args[1]);
            radius = Integer.parseInt(args[2]);
            threshold = Integer.parseInt(args[3]);
            earliestYear = Integer.parseInt(args[4]);
        } else if (args.length != 0) {
            System.err.println(
                    "Usage: java AutoTheftUnsafeCaseDemo <latitude> <longitude> <radius> <threshold> <earliestYear>");
            System.exit(1);
        }

        // Use CrimeAnalysisFacade to analyze auto theft data
        CrimeAnalysisFacade facade = new CrimeAnalysisFacade();
        AutoTheftResult result = facade.analyzeAutoTheft(latitude, longitude, radius, threshold, earliestYear);

        // Print the results
        System.err.println("All Auto Theft in the past year within the radius:");
        int index = 1;
        for (AutoTheftResult.Incident incident : result.getPastYearIncidents()) {
            System.out.printf("#%d, occur date: %s, distance from you: %.2f meters%n", index++, incident.getOccurDate(), incident.getDistance());
        }

        System.err.println("ALL known Auto Theft within the radius:");
        index = 1;
        for (AutoTheftResult.Incident incident : result.getAllKnownIncidents()) {
            System.out.printf("#%d, occur date: %s, distance from you: %.2f meters%n", index++, incident.getOccurDate(), incident.getDistance());
        }

        System.err.println(result.getProbabilityMessage());
        System.err.println(result.getWarning());

        List<SafeParkingSpot> safeSpotsList = new ArrayList<>();

        if (result.getProbability() > 0.15) {
            System.err.println("Nearby safe parking spots:");
            // Find nearby safe parking spots
            List<Row> safeSpots = SafeParkingLocationManager.getInstance().getNearbySafeLocations(latitude, longitude, radius, 3, threshold);
            for (Row spot : safeSpots) {
                double spotLat = spot.getDouble("Latitude");
                double spotLon = spot.getDouble("Longitude");
                String date = spot.getDateTime("Added Time").toLocalDate().toString();
                double spotProbability = spot.getDouble("Probability");
                int spotRadius = spot.getInt("Radius");
                int spotThreshold = spot.getInt("Threshold");

                double spotDistance = GeoUtils.calculateDistance(latitude, longitude, spotLat, spotLon);
                System.out.printf(
                        "Safe Spot - Latitude: %.6f, Longitude: %.6f, Distance from you: %.2f meters, Added Time: %s, Probability: %.4f%%, Radius: %d, Threshold: %d%n",
                        spotLat, spotLon, spotDistance, date, spotProbability * 100, spotRadius, spotThreshold);

                safeSpotsList.add(new SafeParkingSpot(spotLat, spotLon, spotDistance, date, spotProbability, spotRadius, spotThreshold));
            }
        } else {
            SafeParkingLocationManager.getInstance().addOrUpdateSafeLocation(latitude, longitude, result.getProbability(), radius, threshold);
            System.err.println("Location added to safe parking locations.");
        }

        // Create and print JSON result
        Gson gson = new Gson();
        String jsonResult = gson.toJson(result);
        System.err.println(jsonResult);
    }
}
