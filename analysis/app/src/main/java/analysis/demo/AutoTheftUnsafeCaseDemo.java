package analysis.demo;

import access.convert.CrimeDataConverter;
import access.data.CrimeDataFetcher;
import access.manipulate.CrimeDataProcessor;
import analysis.carTheft.*;
import analysis.utils.GeoUtils;
import com.google.gson.Gson;
import tech.tablesaw.api.Row;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A demo class for analyzing and displaying auto theft data within a specified radius
 * and suggesting alternative safe parking spots if the current location is unsafe.
 */
public class AutoTheftUnsafeCaseDemo {

    /**
     * The main method that runs the demo for analyzing and displaying auto theft data within a specified radius
     * and suggesting alternative safe parking spots if the current location is unsafe.
     *
     * @param args Command-line arguments (latitude, longitude, radius, threshold, earliestYear).
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
            System.err.println("Usage: java AutoTheftUnsafeCaseDemo <latitude> <longitude> <radius> <threshold> <earliestYear>");
            System.exit(1);
        }

        CrimeDataFetcher fetcher = new CrimeDataFetcher();
        CrimeDataConverter converter = new CrimeDataConverter();
        CrimeDataProcessor processor = new CrimeDataProcessor();

        AutoTheftIncidentFetcher autoTheftIncidentFetcher = new AutoTheftIncidentFetcher(fetcher, converter, processor);
        AutoTheftCalculator autoTheftCalculator = new AutoTheftCalculator(autoTheftIncidentFetcher);

        SafeParkingLocationManager safeParkingLocationManager = SafeParkingLocationManager.getInstance();

        // Get auto theft data for the past year
        List<AutoTheftData> pastYearData = autoTheftCalculator.getCrimeDataWithinRadiusPastYear(latitude, longitude, radius);
        pastYearData.sort(Comparator.comparing(AutoTheftData::getOccYear)
                .thenComparing(AutoTheftData::getOccMonth)
                .thenComparing(AutoTheftData::getOccDay)); // Sort by date

        List<AutoTheftResult.Incident> pastYearIncidents = new ArrayList<>();
        for (AutoTheftData data : pastYearData) {
            double distance = GeoUtils.calculateDistance(latitude, longitude, data.getLatitude(), data.getLongitude());
            String occurDate = data.getOccYear() + "-" + data.getOccMonth() + "-" + data.getOccDay();
            pastYearIncidents.add(new AutoTheftResult.Incident(occurDate, distance));
        }

        // Get all auto theft data within the radius from the specified earliest year
        List<AutoTheftData> allData = autoTheftCalculator.getCrimeDataWithinRadius(latitude, longitude, radius, earliestYear);
        allData.sort(Comparator.comparing(AutoTheftData::getOccYear)
                .thenComparing(AutoTheftData::getOccMonth)
                .thenComparing(AutoTheftData::getOccDay));

        List<AutoTheftResult.Incident> allKnownIncidents = new ArrayList<>();
        for (AutoTheftData data : allData) {
            double distance = GeoUtils.calculateDistance(latitude, longitude, data.getLatitude(), data.getLongitude());
            String occurDate = data.getOccYear() + "-" + data.getOccMonth() + "-" + data.getOccDay();
            allKnownIncidents.add(new AutoTheftResult.Incident(occurDate, distance));
        }

        // Calculate the average annual rate of incidents
        double lambda = autoTheftCalculator.calculateAnnualAverageIncidents(allData); // Use the average annual rate of incidents as the λ value
        double probability = autoTheftCalculator.calculatePoissonProbability(lambda, threshold);

        // Create probability message
        String probabilityMessage = String.format("Based on past data, within %dm of radius, there's a %.4f%% chance that auto thefts happen more than %d time(s) within a year.", radius, probability * 100, threshold);

        // Create warning message
        String warning = probability > 0.15 ? "WARNING: Don't park here!" : "Safe to park here!";

        // Print the results
        System.out.println("All Auto Theft in the past year within the radius:");
        int index = 1;
        for (AutoTheftResult.Incident incident : pastYearIncidents) {
            System.out.printf("#%d, occur date: %s, distance from you: %.2f meters%n", index++, incident.occurDate, incident.distance);
        }

        System.out.println("ALL known Auto Theft within the radius:");
        index = 1;
        for (AutoTheftResult.Incident incident : allKnownIncidents) {
            System.out.printf("#%d, occur date: %s, distance from you: %.2f meters%n", index++, incident.occurDate, incident.distance);
        }

        System.out.println(probabilityMessage);
        System.out.println(warning);

        List<SafeParkingSpot> safeSpotsList = new ArrayList<>();

        if (probability > 0.15) {
            System.out.println("Nearby safe parking spots:");
            // Find nearby safe parking spots
            List<Row> safeSpots = safeParkingLocationManager.getNearbySafeLocations(latitude, longitude, radius, 3, threshold);
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
            safeParkingLocationManager.addOrUpdateSafeLocation(latitude, longitude, probability, radius, threshold);
            System.out.println("Location added to safe parking locations.");
        }

        // Create and print JSON result
        AutoTheftResult result = new AutoTheftResult(pastYearIncidents, allKnownIncidents, probability, probabilityMessage, warning, safeSpotsList);
        Gson gson = new Gson();
        String jsonResult = gson.toJson(result);
        System.out.println(jsonResult);
    }
}
