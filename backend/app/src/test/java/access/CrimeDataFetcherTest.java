package backend.app.src.test.java.access;

import backend.app.src.main.java.access.CrimeDataFetcher;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CrimeDataFetcherTest {

    @Test
    public void testFetchData() throws JSONException {
        CrimeDataFetcher dataFetcher = new CrimeDataFetcher();
        JSONArray data = dataFetcher.fetchData();

        assertNotNull(data, "Data should not be null");
        assertTrue(data.length() > 0, "Data should contain at least one entry");

        // Optional: Print the first entry to see what the data looks like
        if (data.length() > 0) {
            System.out.println("First entry: " + data.getJSONObject(0).toString(2));
        }
    }
}
