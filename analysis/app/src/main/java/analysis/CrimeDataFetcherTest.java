package analysis;

import access.CrimeDataFetcher;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * A test class for the CrimeDataFetcher class.
 */
public class CrimeDataFetcherTest {

    /**
     * Tests the fetchData method of the CrimeDataFetcher class to ensure
     * that data is correctly fetched from the data source.
     *
     * @throws JSONException if there is an error parsing the JSON data.
     */
    @Test
    public void testFetchData() throws JSONException {
        CrimeDataFetcher dataFetcher = new CrimeDataFetcher();
        JSONArray data = dataFetcher.fetchData();

        // Assert that data is not null
        assertNotNull(data, "Data should not be null");

        // Assert that data has at least one entry
        assertTrue(data.length() > 0, "Data should contain at least one entry");

        // Print the first entry for manual inspection
        if (data.length() > 0) {
            System.out.println("First entry: " + data.getJSONObject(0).toString(2));
        }
    }
}
