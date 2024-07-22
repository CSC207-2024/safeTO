package types;

import java.util.Map;

import com.google.gson.annotations.SerializedName;

public class CachedResponse {
    @SerializedName("headers")
    private Map<String, String> headers;

    @SerializedName("body")
    private String body;

    // Getters and setters
    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
