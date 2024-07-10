package backend.app.src.test.java.dataAccess;

import backend.app.src.main.java.access.CrimeDataFetcher;
import backend.app.src.main.java.access.CrimeDataConverter;
import backend.app.src.main.java.dataAccess.StolenCarDataFetcher;
import backend.app.src.main.java.dataAccess.AutoTheftCalculator;
import backend.app.src.main.java.dataAccess.StolenCarData;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


public class AutoTheftCalculatorTest {

    @Test
    public void testGetCrimeDataWithinRadius() {
        CrimeDataFetcher fetcher = new CrimeDataFetcher();
        CrimeDataConverter converter = new CrimeDataConverter();
        StolenCarDataFetcher stolenCarDataFetcher = new StolenCarDataFetcher(fetcher, converter);
        AutoTheftCalculator calculator = new AutoTheftCalculator(stolenCarDataFetcher);

        double latitude = 43.64444078400003;
        double longitude = -79.40006913299997;
        double radius = 5000.0;

        List<StolenCarData> data = calculator.getCrimeDataWithinRadius(latitude, longitude, radius);

        assertNotNull(data, "Data should not be null");
        assertTrue(data.size() > 0, "Data should contain at least one entry");

        // Additional assertions can be added here based on expected values
    }

    @Test
    public void testGetCrimeDataWithinRadiusPastYear() {
        CrimeDataFetcher fetcher = new CrimeDataFetcher();
        CrimeDataConverter converter = new CrimeDataConverter();
        StolenCarDataFetcher stolenCarDataFetcher = new StolenCarDataFetcher(fetcher, converter);
        AutoTheftCalculator calculator = new AutoTheftCalculator(stolenCarDataFetcher);

        double latitude = 43.64444078400003;
        double longitude = -79.40006913299997;
        double radius = 5000.0;

        List<StolenCarData> data = calculator.getCrimeDataWithinRadiusPastYear(latitude, longitude, radius);

        assertNotNull(data, "Data should not be null");
        assertTrue(data.size() > 0, "Data should contain at least one entry");

        // Additional assertions can be added here based on expected values
    }

    @Test
    public void testCalculatePoissonProbability() {
        CrimeDataFetcher fetcher = new CrimeDataFetcher();
        CrimeDataConverter converter = new CrimeDataConverter();
        StolenCarDataFetcher stolenCarDataFetcher = new StolenCarDataFetcher(fetcher, converter);
        AutoTheftCalculator calculator = new AutoTheftCalculator(stolenCarDataFetcher);

        double lambda = 5.0;
        int threshold = 1;

        double probability = calculator.calculatePoissonProbability(lambda, threshold);

        assertTrue(probability > 0, "Probability should be greater than 0");
        assertTrue(probability <= 1, "Probability should be less than or equal to 1");
    }
}