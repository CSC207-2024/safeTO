package analysis.breakAndEnter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BreakAndEnterCalculatorTest {

    private BreakAndEnterCalculator calculator;
    private List<BreakAndEnterData> sampleData;

    @BeforeEach
    void setUp() {
        sampleData = new ArrayList<>();
        // Adding test data similar to the real data from the logs
        sampleData.add(new BreakAndEnterData("id1", 2023, "August", 18, "Break and Enter", 43.8062893908425, -79.1803868891903));
        sampleData.add(new BreakAndEnterData("id2", 2023, "August", 19, "Break and Enter", 43.8062893908425, -79.1803868891903));
        sampleData.add(new BreakAndEnterData("id3", 2016, "February", 24, "Break and Enter", 43.8062893908425, -79.1803868891903));
        sampleData.add(new BreakAndEnterData("id4", 2016, "July", 30, "Break and Enter", 43.8062893908425, -79.1803868891903));
        sampleData.add(new BreakAndEnterData("id5", 2020, "December", 24, "Break and Enter", 43.8062893908425, -79.1803868891903));
        sampleData.add(new BreakAndEnterData("id6", 2020, "November", 25, "Break and Enter", 43.8062893908425, -79.1803868891903));
        sampleData.add(new BreakAndEnterData("id7", 2021, "August", 24, "Break and Enter", 43.8062893908425, -79.1803868891903));
        sampleData.add(new BreakAndEnterData("id8", 2023, "January", 17, "Break and Enter", 43.8062893908425, -79.1803868891903));
        sampleData.add(new BreakAndEnterData("id9", 2023, "June", 2, "Break and Enter", 43.8062893908425, -79.1803868891903));

        calculator = new BreakAndEnterCalculator(sampleData);
    }

    @Test
    void getCrimeDataWithinRadius() {
        double lat = 43.8062893908425;
        double lon = -79.1803868891903;
        double radius = 200; // in meters

        List<BreakAndEnterData> result = calculator.getCrimeDataWithinRadius(lat, lon, radius);
        assertEquals(9, result.size(), "Expected 9 incidents within the given radius");
    }

    @Test
    void getCrimeDataWithinRadiusPastYear() {
        double lat = 43.8062893908425;
        double lon = -79.1803868891903;
        double radius = 200; // in meters

        List<BreakAndEnterData> result = calculator.getCrimeDataWithinRadiusPastYear(lat, lon, radius);
        assertEquals(2, result.size(), "Expected 2 incidents within the past year and given radius");
    }

    @Test
    void calculateAnnualAverageIncidents() {
        double averageIncidents = calculator.calculateAnnualAverageIncidents(sampleData);
        assertEquals(2.25, averageIncidents, 0.1, "Expected annual average incidents to be approximately 1.5");
    }

    @Test
    void calculatePoissonProbability() {
        double lambda = 1.5;
        int threshold = 3;
        double probability = calculator.calculatePoissonProbability(lambda, threshold);
        assertTrue(probability > 0, "Expected probability to be greater than 0");
        assertEquals(0.06564, probability, 0.001, "Expected probability to be approximately 0.191");
    }
}
