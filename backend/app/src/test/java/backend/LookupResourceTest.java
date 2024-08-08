package backend;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import types.Place;
import geography.ReverseGeocoding;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import static org.junit.jupiter.api.Assertions.*;

class LookupResourceTest {

    @Test
    void lookup_success() {
        LookupResource lookupResource = new LookupResource();

        // Simulate a successful lookup
        Response response = lookupResource.lookup(43.7182639f, -79.7077207f);

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

        assertTrue(data.has("_place"), "Expected '_place' field in 'data'");
        JsonObject placeJson = data.getAsJsonObject("_place");

        // Further checks for the structure of the placeJson object
        assertNotNull(placeJson, "Expected non-null '_place' object");
    }



}
