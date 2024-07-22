package analysis.carTheft;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.tablesaw.api.Row;
import tech.tablesaw.api.Table;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SafeParkingLocationManagerTest {

    private SafeParkingLocationManager manager;

    @BeforeEach
    void setUp() {
        manager = SafeParkingLocationManager.getInstance();
        // Clear any existing data for a clean test environment
        Table safeLocations = manager.getSafeLocationsTable();
        safeLocations.clear();
    }

    @Test
    void getInstance() {
        SafeParkingLocationManager instance1 = SafeParkingLocationManager.getInstance();
        SafeParkingLocationManager instance2 = SafeParkingLocationManager.getInstance();
        assertSame(instance1, instance2, "getInstance() should return the same instance.");
    }

    @Test
    void addOrUpdateSafeLocation() {
        double latitude = 43.7319;
        double longitude = -79.2718;
        double probability = 0.05;
        int radius = 200;
        int threshold = 3;

        // Add a new safe location
        manager.addOrUpdateSafeLocation(latitude, longitude, probability, radius, threshold);

        Table safeLocations = manager.getSafeLocationsTable();
        assertEquals(1, safeLocations.rowCount(), "There should be one safe location.");
        Row row = safeLocations.row(0);
        assertEquals(latitude, row.getDouble("Latitude"));
        assertEquals(longitude, row.getDouble("Longitude"));
        assertEquals(probability, row.getDouble("Probability"));
        assertEquals(radius, row.getInt("Radius"));
        assertEquals(threshold, row.getInt("Threshold"));

        // Update the same location with new data
        double newProbability = 0.03;
        int newRadius = 250;
        int newThreshold = 2;
        manager.addOrUpdateSafeLocation(latitude, longitude, newProbability, newRadius, newThreshold);

        assertEquals(1, safeLocations.rowCount(), "There should still be one safe location after update.");
        row = safeLocations.row(0);
        assertEquals(latitude, row.getDouble("Latitude"));
        assertEquals(longitude, row.getDouble("Longitude"));
        assertEquals(newProbability, row.getDouble("Probability"));
        assertEquals(newRadius, row.getInt("Radius"));
        assertEquals(newThreshold, row.getInt("Threshold"));
    }

    @Test
    void getNearbySafeLocations() {
        double latitude = 43.7319;
        double longitude = -79.2718;

        // Add multiple safe locations
        manager.addOrUpdateSafeLocation(43.7310, -79.2700, 0.02, 200, 3);
        manager.addOrUpdateSafeLocation(43.7320, -79.2720, 0.04, 200, 3);
        manager.addOrUpdateSafeLocation(43.7330, -79.2730, 0.06, 200, 3);

        List<Row> nearbyLocations = manager.getNearbySafeLocations(latitude, longitude, 500, 2, 3);
        assertEquals(2, nearbyLocations.size(), "Should return the closest 2 locations within the radius.");

        Row firstLocation = nearbyLocations.get(0);
        Row secondLocation = nearbyLocations.get(1);

        assertEquals(43.7310, firstLocation.getDouble("Latitude"));
        assertEquals(-79.2700, firstLocation.getDouble("Longitude"));
        assertEquals(43.7320, secondLocation.getDouble("Latitude"));
        assertEquals(-79.2720, secondLocation.getDouble("Longitude"));
    }

    @Test
    void getSafeLocationsTable() {
        Table safeLocations = manager.getSafeLocationsTable();
        assertNotNull(safeLocations, "Safe locations table should not be null.");
    }
}
