package analysis;

import access.CrimeDataConverter;
import access.CrimeDataFetcher;
import org.json.JSONArray;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import tech.tablesaw.api.Table;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CrimeDataConverterTest {

    @Test
    public void testJsonToTableConversion() throws JSONException {
        // Arrange
        CrimeDataFetcher fetcher = new CrimeDataFetcher();
        CrimeDataConverter converter = new CrimeDataConverter();

        // Act
        JSONArray data = fetcher.fetchData();
        assertNotNull(data, "Fetched data should not be null");

        Table table = converter.jsonToTable(data);

        // Assert
        assertNotNull(table, "Converted table should not be null");
        assertTrue(table.rowCount() > 0, "Table should contain at least one row");
        assertTrue(table.columnCount() > 0, "Table should contain at least one column");

        // Print some data for manual inspection
        System.out.println("Table structure:");
        System.out.println(table.structure().print());

        System.out.println("First few rows of data:");
        System.out.println(table.first(5).print());
    }
}
