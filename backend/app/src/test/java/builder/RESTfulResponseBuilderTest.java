package builder;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RESTfulResponseBuilderTest {

    @Test
    public void testDefaultValues() {
        String jsonResponse = RESTfulResponseBuilder.create().build();

        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();

        assertEquals(true, jsonObject.get("ok").getAsBoolean());
        assertEquals("", jsonObject.get("message").getAsString());
        assertTrue(jsonObject.get("data") == null);
    }

    @Test
    public void testWithOkFalse() {
        String jsonResponse = RESTfulResponseBuilder.create()
                .withOk(false)
                .build();

        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();

        assertEquals(false, jsonObject.get("ok").getAsBoolean());
    }

    @Test
    public void testWithMessage() {
        String testMessage = "An error occurred";
        String jsonResponse = RESTfulResponseBuilder.create()
                .withMessage(testMessage)
                .build();

        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();

        assertEquals(testMessage, jsonObject.get("message").getAsString());
    }

    @Test
    public void testWithDataJsonElement() {
        JsonObject testData = new JsonObject();
        testData.addProperty("name", "John Doe");

        String jsonResponse = RESTfulResponseBuilder.create()
                .withData(testData)
                .build();

        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();

        assertEquals(testData, jsonObject.get("data").getAsJsonObject());
    }

    @Test
    public void testWithDataObject() {
        String testDataMessage = "Some data";

        String jsonResponse = RESTfulResponseBuilder.create()
                .withData(testDataMessage)
                .build();

        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();

        assertEquals(testDataMessage, jsonObject.get("data").getAsString());
    }

    private class DummyData {
        private String id;
        private String content;

        public DummyData(String id, String content) {
            this.id = id;
            this.content = content;
        }
    }

    @Test
    public void testCompleteResponse() {
        String jsonResponse = RESTfulResponseBuilder.create()
                .withOk(false)
                .withMessage("Not found")
                .withData(new DummyData("123", "Sample Content"))
                .build();

        JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();

        assertEquals(false, jsonObject.get("ok").getAsBoolean());
        assertEquals("Not found", jsonObject.get("message").getAsString());
        assertFalse(jsonObject.get("data") == null);
        assertEquals("123", jsonObject.get("data").getAsJsonObject().get("id").getAsString());
        assertEquals("Sample Content", jsonObject.get("data").getAsJsonObject().get("content").getAsString());
    }
}
