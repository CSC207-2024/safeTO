package email;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Map;

/**
 * A public class that builds email alerts parameters.
 */
public class EmailBuilder {

    private Map<String, Object> parameters = new HashMap<>();

    /**
     * A public method that sets the email body paramters.
     * @param jsonObject the json object that containing the email body parameters.
     * @return an instance of EmailBuilder.
     */
    public EmailBuilder fromJsonObject(JsonObject jsonObject) {
        Gson gson = new Gson();
        this.parameters = gson.fromJson(jsonObject, Map.class);
        return this;
    }

    /**
     * A public method that sets the email body paramters.
     * @param jsonString the json string that containing the email body parameters.
     * @return an instance of EmailBuilder.
     */
    public EmailBuilder fromString(String jsonString) {
        Gson gson = new Gson();
        this.parameters = gson.fromJson(jsonString, Map.class);
        return this;
    }

    /**
     * A public method that sets the email body paramters.
     * @param map  the map that containing the email body parameters.
     * @return an instance of EmailBuilder.
     */
    public EmailBuilder fromMap(Map<String, Object> map) {
        this.parameters = map;
        return this;
    }


    /**
     * A public method that sets the email body paramters.
     * @param key the key of the parameter.
     * @param value the value of the parameter.
     * @return an instance of EmailBuilder.
     */
    public EmailBuilder setParameter(String key, Object value) {
        this.parameters.put(key, value);
        return this;
    }

    /**
     * A public method that removes a parameter from the email body.
     * @param key the key of the parameter.
     * @return an instance of EmailBuilder.
     */
    public EmailBuilder removeParameter(String key) {
        this.parameters.remove(key);
        return this;
    }

    /**
     * A public method that returns the email body parameters.
     * @return a map of email body parameters.
     */
    public Map<String, Object> getParameters() {
        return this.parameters;
    }


    /**
     * A public method that builds the email body parameters.
     * @return a map of email body parameters.
     */
    public Map<String, Object> build() {
        return this.parameters;
    }



}
