package backend;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import static org.junit.jupiter.api.Assertions.*;

class WelcomeResourceTest {

    private WelcomeResource welcomeResource;

    @BeforeEach
    void setUp() {
        welcomeResource = new WelcomeResource();
    }

    @Test
    void welcome_success() {
        // When
        Response response = welcomeResource.welcome();

        // Then
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        // Parse the response body as JSON
        String responseBody = (String) response.getEntity();
        JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();

        // Validate the JSON structure and content
        assertTrue(jsonResponse.has("ok"));
        assertTrue(jsonResponse.get("ok").getAsBoolean());

        assertTrue(jsonResponse.has("message"));
        String expectedMessage = "Welcome to the API. This endpoint is not intended for human interaction. Check out our repository at https://github.com/CSC207-2024/safeTO";
        assertEquals(expectedMessage, jsonResponse.get("message").getAsString());
    }
}
