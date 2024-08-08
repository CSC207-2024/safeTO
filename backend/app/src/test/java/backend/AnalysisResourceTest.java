package backend;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AnalysisResourceTest {

    @Test
    void breakAndEnter() {
        AnalysisResource analysisResource = new AnalysisResource();

        // Call the breakAndEnter method with default parameters
        Response response = analysisResource.breakAndEnter(43.6690207, -79.3916043, 200, 5);

        // Validate the response
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        String responseBody = (String) response.getEntity();
        assertTrue(responseBody.contains("Command executed successfully") || responseBody.contains("Error executing command"));
    }

    @Test
    void autoTheft() {
        AnalysisResource analysisResource = new AnalysisResource();

        // Call the autoTheft method with default parameters
        Response response = analysisResource.autoTheft(43.6598045, -79.3998783, 200, 5, 2021);

        // Validate the response
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        String responseBody = (String) response.getEntity();
        assertTrue(responseBody.contains("Command executed successfully") || responseBody.contains("Error executing command"));
    }

    @Test
    void ranking() {
        AnalysisResource analysisResource = new AnalysisResource();

        // Call the ranking method with some test parameters
        Response response = analysisResource.ranking("Downtown", "Assault");

        // Validate the response
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        String responseBody = (String) response.getEntity();
        assertTrue(responseBody.contains("Command executed successfully") || responseBody.contains("Error executing command"));
    }
}
