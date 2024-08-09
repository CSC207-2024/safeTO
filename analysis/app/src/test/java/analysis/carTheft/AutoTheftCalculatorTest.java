package analysis.carTheft;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AutoTheftCalculatorTest {

    private AutoTheftCalculator calculator;
    private List<AutoTheftData> sampleData;

    @BeforeEach
    void setUp() {
        sampleData = new ArrayList<>();
        // Adding test data similar to the real data from the logs
        sampleData.add(new AutoTheftData("id1", 2023, "December", 22, "Auto Theft", 43.731901, -79.271765));
        sampleData.add(new AutoTheftData("id2", 2019, "September", 10, "Auto Theft", 43.731902, -79.271766));
        sampleData.add(new AutoTheftData("id3", 2020, "June", 26, "Auto Theft", 43.731903, -79.271767));
        sampleData.add(new AutoTheftData("id4", 2021, "April", 12, "Auto Theft", 43.731904, -79.271768));
        sampleData.add(new AutoTheftData("id5", 2023, "April", 7, "Auto Theft", 43.731905, -79.271769));
        sampleData.add(new AutoTheftData("id6", 2023, "June", 2, "Auto Theft", 43.731906, -79.271770));
        sampleData.add(new AutoTheftData("id7", 2023, "June", 2, "Auto Theft", 43.731907, -79.271771));

        calculator = new AutoTheftCalculator(sampleData);
    }

    @Test
    void getCrimeDataWithinRadius() {
        double lat = 43.731901;
        double lon = -79.271765;
        double radius = 200; // in meters

        List<AutoTheftData> result = calculator.getCrimeDataWithinRadius(lat, lon, radius);
        assertEquals(7, result.size(), "Expected 7 incidents within the given radius");
    }

    @Test
    void getCrimeDataWithinRadiusPastYear() {
        double lat = 43.731901;
        double lon = -79.271765;
        double radius = 200; // in meters

        List<AutoTheftData> result = calculator.getCrimeDataWithinRadiusPastYear(lat, lon, radius);
        assertEquals(1, result.size(), "Expected 3 incidents within the past year and given radius");
    }

    @Test
    void getCrimeDataWithinRadiusFromYear() {
        double lat = 43.731901;
        double lon = -79.271765;
        double radius = 200; // in meters
        int earliestYear = 2020;

        List<AutoTheftData> result = calculator.getCrimeDataWithinRadius(lat, lon, radius, earliestYear);
        assertEquals(6, result.size(), "Expected 6 incidents from the earliest year within the given radius");
    }

    @Test
    void calculateAnnualAverageIncidents() {
        double averageIncidents = calculator.calculateAnnualAverageIncidents(sampleData);
        assertEquals(1.75, averageIncidents, 0.1, "Expected annual average incidents to be approximately 2.33");
    }

    @Test
    void calculatePoissonProbability() {
        double lambda = 2.33;
        int threshold = 5;
        double probability = calculator.calculatePoissonProbability(lambda, threshold);
        assertTrue(probability > 0, "Expected probability to be greater than 0");
    }
}
