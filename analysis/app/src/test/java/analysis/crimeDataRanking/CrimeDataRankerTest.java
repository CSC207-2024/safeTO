package analysis.crimeDataRanking;

import access.manipulate.CrimeDataProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.tablesaw.api.StringColumn;
import tech.tablesaw.api.Table;

import static org.junit.jupiter.api.Assertions.*;

class CrimeDataRankerTest {

    private CrimeDataProcessor processor;
    private CrimeDataRanker ranker;

    @BeforeEach
    void setUp() {
        processor = new CrimeDataProcessor();


        Table table = Table.create("Crime Data")
                .addColumns(
                        StringColumn.create("MCI_CATEGORY", new String[]{"Assault", "Theft", "Assault", "Break and Enter", "Theft"}),
                        StringColumn.create("NEIGHBOURHOOD_158", new String[]{"Downtown", "Midtown", "Downtown", "Uptown", "Midtown"})
                );
        processor.setTable(table);

        ranker = new CrimeDataRanker(processor);
    }

    @Test
    void rankNeighborhoodsByTotalCrime() {
        Table result = ranker.rankNeighborhoodsByTotalCrime();
        assertNotNull(result, "Resulting table should not be null.");
        assertEquals(3, result.rowCount(), "There should be 3 neighborhoods ranked.");

        assertEquals("Downtown", result.stringColumn("NEIGHBOURHOOD_158").get(0), "Downtown should be ranked first.");
        assertEquals("Midtown", result.stringColumn("NEIGHBOURHOOD_158").get(1), "Midtown should be ranked second.");
        assertEquals("Uptown", result.stringColumn("NEIGHBOURHOOD_158").get(2), "Uptown should be ranked third.");
    }

    @Test
    void rankNeighborhoodsBySpecificCrime() {
        Table result = ranker.rankNeighborhoodsBySpecificCrime("Assault");
        assertNotNull(result, "Resulting table should not be null.");
        assertEquals(1, result.rowCount(), "There should be 1 neighborhood with Assault crimes.");

        assertEquals("Downtown", result.stringColumn("NEIGHBOURHOOD_158").get(0), "Downtown should be ranked first for Assault.");
    }

    @Test
    void getNeighborhoodRanking() {
        int ranking = ranker.getNeighborhoodRanking("Downtown");
        assertEquals(1, ranking, "Downtown should be ranked first for total crimes.");

        ranking = ranker.getNeighborhoodRanking("Midtown");
        assertEquals(2, ranking, "Midtown should be ranked second for total crimes.");

        ranking = ranker.getNeighborhoodRanking("Uptown");
        assertEquals(3, ranking, "Uptown should be ranked third for total crimes.");

        ranking = ranker.getNeighborhoodRanking("Unknown");
        assertEquals(-1, ranking, "Unknown neighborhood should not be found.");
    }

    @Test
    void getSpecificCrimeNeighborhoodRanking() {
        int ranking = ranker.getSpecificCrimeNeighborhoodRanking("Assault", "Downtown");
        assertEquals(1, ranking, "Downtown should be ranked first for Assault.");

        ranking = ranker.getSpecificCrimeNeighborhoodRanking("Theft", "Midtown");
        assertEquals(1, ranking, "Midtown should be ranked first for Theft.");

        ranking = ranker.getSpecificCrimeNeighborhoodRanking("Break and Enter", "Uptown");
        assertEquals(1, ranking, "Uptown should be ranked first for Break and Enter.");

        ranking = ranker.getSpecificCrimeNeighborhoodRanking("Assault", "Midtown");
        assertEquals(-1, ranking, "Midtown should not be found for Assault.");
    }
}
