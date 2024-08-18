package analysis.carTheft;

import tech.tablesaw.api.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import analysis.utils.GeoUtils;

/**
 * A class to manage safe parking locations.
 */
public class SafeParkingLocationManager {
    private static Table safeLocations;

    static {
        Path path = Paths.get("safe_parking_locations.csv");
        if (Files.exists(path)) {
            safeLocations = Table.read().csv("safe_parking_locations.csv");
        } else {
            safeLocations = Table.create("Safe Parking Locations",
                    DoubleColumn.create("Latitude"),
                    DoubleColumn.create("Longitude"),
                    DateTimeColumn.create("Added Time"),
                    DoubleColumn.create("Probability"),
                    IntColumn.create("Radius"),
                    IntColumn.create("Threshold"));
        }
    }

    private static SafeParkingLocationManager instance = null;

    private SafeParkingLocationManager() {
    }

    /**
     * Returns the single instance of SafeParkingLocationManager.
     *
     * @return The instance of SafeParkingLocationManager.
     */
    public static SafeParkingLocationManager getInstance() {
        if (instance == null) {
            instance = new SafeParkingLocationManager();
        }
        return instance;
    }

    /**
     * Adds or updates a safe parking location based on given criteria.
     *
     * @param latitude    The latitude of the location.
     * @param longitude   The longitude of the location.
     * @param probability The calculated probability.
     * @param radius      The radius used for calculation.
     * @param threshold   The threshold used for calculation.
     */
    public void addOrUpdateSafeLocation(double latitude, double longitude, double probability, int radius, int threshold) {
        boolean updated = false;
        for (int i = 0; i < safeLocations.rowCount(); i++) {
            if (safeLocations.doubleColumn("Latitude").get(i) == latitude &&
                    safeLocations.doubleColumn("Longitude").get(i) == longitude &&
                    safeLocations.intColumn("Radius").get(i) <= radius &&
                    safeLocations.intColumn("Threshold").get(i) >= threshold) {

                safeLocations.doubleColumn("Probability").set(i, probability);
                safeLocations.intColumn("Radius").set(i, radius);
                safeLocations.intColumn("Threshold").set(i, threshold);
                safeLocations.dateTimeColumn("Added Time").set(i, LocalDateTime.now());
                updated = true;
                break;
            }
        }
        if (!updated) {
            safeLocations.doubleColumn("Latitude").append(latitude);
            safeLocations.doubleColumn("Longitude").append(longitude);
            safeLocations.dateTimeColumn("Added Time").append(LocalDateTime.now());
            safeLocations.doubleColumn("Probability").append(probability);
            safeLocations.intColumn("Radius").append(radius);
            safeLocations.intColumn("Threshold").append(threshold);
        }
        saveToFile();
    }

    /**
     * Saves the table of safe locations to a CSV file.
     */
    private void saveToFile() {
        safeLocations.write().csv("safe_parking_locations.csv");
    }

    /**
     * Finds nearby safe parking locations within a given radius.
     *
     * @param latitude      The latitude of the center point.
     * @param longitude     The longitude of the center point.
     * @param radius        The radius in meters.
     * @param numSuggestions The number of suggestions to return.
     * @param threshold     The threshold to filter.
     * @return A list of rows representing safe parking locations within the radius.
     */
    public List<Row> getNearbySafeLocations(double latitude, double longitude, double radius, int numSuggestions, int threshold) {
        List<Row> nearbySafeLocations = new ArrayList<>();

        for (int i = 0; i < safeLocations.rowCount(); i++) {
            double lat = safeLocations.doubleColumn("Latitude").get(i);
            double lon = safeLocations.doubleColumn("Longitude").get(i);
            int spotThreshold = safeLocations.intColumn("Threshold").get(i);
            double distance = GeoUtils.calculateDistance(latitude, longitude, lat, lon);
            if (distance <= radius && spotThreshold <= threshold) {
                nearbySafeLocations.add(safeLocations.row(i));
            }
        }

        if (nearbySafeLocations.size() < numSuggestions) {
            for (int i = 0; i < safeLocations.rowCount(); i++) {
                double lat = safeLocations.doubleColumn("Latitude").get(i);
                double lon = safeLocations.doubleColumn("Longitude").get(i);
                double distance = GeoUtils.calculateDistance(latitude, longitude, lat, lon);
                if (distance <= radius && !nearbySafeLocations.contains(safeLocations.row(i))) {
                    nearbySafeLocations.add(safeLocations.row(i));
                }
            }
        }

        nearbySafeLocations.sort(Comparator.comparingDouble(row -> row.getDouble("Probability")));
        return nearbySafeLocations.stream().limit(numSuggestions).collect(Collectors.toList());
    }

    /**
     * Retrieves the table of safe parking locations.
     *
     * @return The table of safe parking locations.
     */
    public Table getSafeLocationsTable() {
        return safeLocations;
    }
}
