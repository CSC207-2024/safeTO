package backend;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import types.Place;
import geography.Geocoding;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import static org.junit.jupiter.api.Assertions.*;

class SearchResourceTest {

    @Test
    void search_success() {
        SearchResource searchResource = new SearchResource();

        // Simulate a successful search query
        String query = "123 Main St";
        String limit = "Toronto, ON, Canada";

        // Call the search method
        Response response = searchResource.search(query, limit);

        // Assert that the status is 200 OK
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        // Parse the response body as JSON
        String responseBody = (String) response.getEntity();
        JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();

        // Validate that the response contains the expected structure
        assertTrue(jsonResponse.has("ok"));
        assertTrue(jsonResponse.get("ok").getAsBoolean());

        assertTrue(jsonResponse.has("data"));
        JsonObject data = jsonResponse.getAsJsonObject("data");

        assertTrue(data.has("_places"), "Expected '_places' field in 'data'");

        // Handle the _places field as a JsonArray
        JsonArray placesArray = data.getAsJsonArray("_places");
        assertNotNull(placesArray, "Expected non-null '_places' array");

        // Further checks for the structure of the placesArray
        assertFalse(placesArray.isEmpty(), "Expected non-empty '_places' array");
    }


}
