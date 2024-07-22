package analysis.carTheft;

import analysis.interfaces.IncidentFetcherInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AutoTheftCalculatorTest {

    private AutoTheftCalculator calculator;

    @BeforeEach
    void setUp() {
        List<AutoTheftData> mockData = Arrays.asList(
                new AutoTheftData("1", 2023, "JULY", 15, "Auto Theft", 43.7319, -79.2718),
                new AutoTheftData("2", 2023, "AUGUST", 20, "Auto Theft", 43.7320, -79.2720),
                new AutoTheftData("3", 2022, "SEPTEMBER", 10, "Auto Theft", 43.90, -79.9730)
        );
        IncidentFetcherInterface<AutoTheftData> mockFetcher = () -> mockData;
        calculator = new AutoTheftCalculator(mockFetcher);
    }

    @Test
    void getCrimeDataWithinRadius() {
        double lat = 43.7319;
        double lon = -79.2718;
        double radius = 2000; // 2000 meters

        List<AutoTheftData> result = calculator.getCrimeDataWithinRadius(lat, lon, radius);
        assertEquals(2, result.size());
    }

    @Test
    void getCrimeDataWithinRadiusPastYear() {
        double lat = 43.7319;
        double lon = -79.2718;
        double radius = 2000; // 2000 meters

        List<AutoTheftData> result = calculator.getCrimeDataWithinRadiusPastYear(lat, lon, radius);
        assertEquals(1, result.size());
    }

    @Test
    void getCrimeDataWithinRadiusWithEarliestYear() {
        double lat = 43.7319;
        double lon = -79.2718;
        double radius = 200; // 200 meters
        int earliestYear = 2022;

        List<AutoTheftData> result = calculator.getCrimeDataWithinRadius(lat, lon, radius, earliestYear);
        assertEquals(2, result.size());
    }

    @Test
    void calculateAnnualAverageIncidents() {
        List<AutoTheftData> data = calculator.getCrimeDataWithinRadius(43.7319, -79.2718, 1000);
        double average = calculator.calculateAnnualAverageIncidents(data);
        assertEquals(2, average); // 3 incidents over 2 years
    }

    @Test
    void calculatePoissonProbability() {
        double lambda = 1.5;
        int threshold = 2;
        double probability = calculator.calculatePoissonProbability(lambda, threshold);
        assertTrue(probability > 0 && probability < 1);
    }
}
