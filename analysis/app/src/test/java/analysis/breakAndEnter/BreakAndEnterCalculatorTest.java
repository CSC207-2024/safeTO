package analysis.breakAndEnter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BreakAndEnterCalculatorTest {

    private BreakAndEnterCalculator calculator;
    private BreakAndEnterIncidentFetcher mockFetcher;
    private List<BreakAndEnterData> sampleData;

    @BeforeEach
    void setUp() {
        mockFetcher = Mockito.mock(BreakAndEnterIncidentFetcher.class);
        calculator = new BreakAndEnterCalculator(mockFetcher);

        sampleData = new ArrayList<>();
        sampleData.add(new BreakAndEnterData("id1", LocalDate.now().getYear() - 1, "November", 10, "Break and Enter", 43.71, -79.39));
        sampleData.add(new BreakAndEnterData("id2", LocalDate.now().getYear(), "February", 20, "Break and Enter", 43.70, -79.41));
        sampleData.add(new BreakAndEnterData("id3", LocalDate.now().getYear() - 2, "March", 15, "Break and Enter", 43.71, -79.41));

        Mockito.when(mockFetcher.fetchCrimeData()).thenReturn(sampleData);
    }

    @Test
    void getCrimeDataWithinRadius() {
        double lat = 43.70;
        double lon = -79.40;
        double radius = 5000; // in meters

        List<BreakAndEnterData> result = calculator.getCrimeDataWithinRadius(lat, lon, radius);
        assertEquals(3, result.size());
    }

    @Test
    void getCrimeDataWithinRadiusPastYear() {
        double lat = 43.70;
        double lon = -79.40;
        double radius = 5000; // in meters

        List<BreakAndEnterData> result = calculator.getCrimeDataWithinRadiusPastYear(lat, lon, radius);
        assertEquals(2, result.size());
    }

    @Test
    void calculateAnnualAverageIncidents() {
        double averageIncidents = calculator.calculateAnnualAverageIncidents(sampleData);
        assertEquals(1.0, averageIncidents, 0.1);
    }

    @Test
    void calculatePoissonProbability() {
        double lambda = 2.0;
        int threshold = 3;
        double probability = calculator.calculatePoissonProbability(lambda, threshold);
        assertTrue(probability > 0);
    }
}
