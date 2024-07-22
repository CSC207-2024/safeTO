package analysis.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeoUtilsTest {

    @Test
    void calculateDistance() {
        double lat1 = 43.701;
        double lon1 = -79.401;
        double lat2 = 43.70;
        double lon2 = -79.40;

        double distance = GeoUtils.calculateDistance(lat1, lon1, lat2, lon2);
        assertTrue(distance > 0);
        assertEquals(137.21, distance, 0.01);
    }
}
