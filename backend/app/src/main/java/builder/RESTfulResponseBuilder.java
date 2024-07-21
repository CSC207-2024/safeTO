package builder;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import singleton.GsonSingleton;

public class RESTfulResponseBuilder {
    private boolean ok = true; // Default value set to true
    private String message = ""; // Default value set to empty string
    private JsonElement data = JsonNull.INSTANCE; // Default value set to null

    // Private constructor to prevent instantiation
    private RESTfulResponseBuilder() {
    }

    // Static method to initialize the builder
    public static RESTfulResponseBuilder create() {
        return new RESTfulResponseBuilder();
    }

    // Methods to set properties
    public RESTfulResponseBuilder withOk(boolean ok) {
        this.ok = ok;
        return this;
    }

    public RESTfulResponseBuilder withMessage(String message) {
        this.message = message;
        return this;
    }

    public RESTfulResponseBuilder withData(JsonElement data) {
        this.data = data;
        return this;
    }

    public RESTfulResponseBuilder withData(Object data) {
        this.data = GsonSingleton.geInstance().toJsonTree(data);
        return this;
    }

    // Method to build the response and return as a JSON string
    public String build() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("ok", this.ok);
        jsonObject.addProperty("message", this.message);
        jsonObject.add("data", this.data);

        return GsonSingleton.geInstance().toJson(jsonObject);
    }
}
