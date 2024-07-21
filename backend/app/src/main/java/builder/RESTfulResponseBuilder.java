package builder;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import singleton.GsonSingleton;

/**
 * A builder class for constructing a RESTful JSON response.
 * <p>
 * This class allows you to create a JSON response with three primary
 * components:
 * <ul>
 * <li>ok - a boolean indicating success or failure</li>
 * <li>message - a String that contains a descriptive message</li>
 * <li>data - a JsonElement that can hold any JSON-compatible data</li>
 * </ul>
 * The builder follows the fluent interface pattern, allowing for method
 * chaining.
 * </p>
 */
public class RESTfulResponseBuilder {

    private boolean ok = true; // Default value set to true
    private String message = ""; // Default value set to empty string
    private JsonElement data = JsonNull.INSTANCE; // Default value set to null

    /**
     * Private constructor to prevent instantiation of this class.
     */
    private RESTfulResponseBuilder() {
    }

    /**
     * Static method to initialize and return a new instance of
     * RESTfulResponseBuilder.
     *
     * @return a new instance of RESTfulResponseBuilder
     */
    public static RESTfulResponseBuilder create() {
        return new RESTfulResponseBuilder();
    }

    /**
     * Sets the 'ok' property to indicate the success or failure of the response.
     *
     * @param ok a boolean indicating success (true) or failure (false)
     * @return the current instance of RESTfulResponseBuilder for method chaining
     */
    public RESTfulResponseBuilder withOk(boolean ok) {
        this.ok = ok;
        return this;
    }

    /**
     * Sets the 'message' property for the response.
     *
     * @param message a String containing a descriptive message
     * @return the current instance of RESTfulResponseBuilder for method chaining
     */
    public RESTfulResponseBuilder withMessage(String message) {
        this.message = message;
        return this;
    }

    /**
     * Sets the 'data' property using a JsonElement.
     *
     * @param data a JsonElement representing the data to be included in the
     *             response
     * @return the current instance of RESTfulResponseBuilder for method chaining
     */
    public RESTfulResponseBuilder withData(JsonElement data) {
        this.data = data;
        return this;
    }

    /**
     * Sets the 'data' property using an Object, which is converted to a JSON tree.
     *
     * @param data an Object that will be converted to JsonElement using Gson
     * @return the current instance of RESTfulResponseBuilder for method chaining
     */
    public RESTfulResponseBuilder withData(Object data) {
        this.data = GsonSingleton.geInstance().toJsonTree(data);
        return this;
    }

    /**
     * Builds the JSON response and returns it as a JSON string.
     *
     * @return a String representing the JSON response
     */
    public String build() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("ok", this.ok);
        jsonObject.addProperty("message", this.message);
        jsonObject.add("data", this.data);

        return GsonSingleton.geInstance().toJson(jsonObject);
    }
}
