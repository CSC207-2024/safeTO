package backend.app.src.test.java.dataAccess;

import backend.app.src.main.java.access.CrimeDataFetcher;
import backend.app.src.main.java.access.CrimeDataConverter;
import backend.app.src.main.java.dataAccess.StolenCarDataFetcher;
import backend.app.src.main.java.dataAccess.AutoTheftCalculator;
import backend.app.src.main.java.dataAccess.StolenCarData;

import org.json.JSONArray;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class AutoTheftCalculatorTest {

    @Test
    public void testFetchData() {
        CrimeDataFetcher dataFetcher = new CrimeDataFetcher();
        CrimeDataConverter dataConverter = new CrimeDataConverter();
        StolenCarDataFetcher stolenCarDataFetcher = new StolenCarDataFetcher(dataFetcher, dataConverter);
        AutoTheftCalculator calculator = new AutoTheftCalculator(stolenCarDataFetcher);

        List<StolenCarData> dataWithinRadius = calculator.getStolenCarDataWithinRadius(43.65107, -79.347015, 5000);
        assertNotNull(dataWithinRadius, "Data within radius should not be null");
        assertTrue(dataWithinRadius.size() > 0, "Data within radius should contain at least one entry");

        List<StolenCarData> dataWithinRadiusPastYear = calculator.getStolenCarDataWithinRadiusPastYear(43.65107, -79.347015, 5000);
        assertNotNull(dataWithinRadiusPastYear, "Data within radius and past year should not be null");
        assertTrue(dataWithinRadiusPastYear.size() > 0, "Data within radius and past year should contain at least one entry");

        double lambda = 2.0;
        int threshold = 3;
        double probability = calculator.calculatePoissonProbability(lambda, threshold);
        assertTrue(probability > 0, "Poisson probability should be greater than 0");

        System.out.println("Test passed!");
    }
}
