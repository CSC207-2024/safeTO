package email;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;

/**
 * This class is responsible for storing and retrieving email addresses from the
 * SafeTo database.
 */
public class EmailStorage {
    private static final String API_URL = "http://localhost:8787";
    private static final String API_TOKEN = System.getenv("SAFETO_DB_TOKEN");

    private HttpClient client;
    private Gson gson;

    /**
     * Constructor for the EmailStorage class.
     */
    public EmailStorage() {
        this.client = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    /**
     * Stores the email address of a user in the SafeTo database.
     *
     * @param collection The name of the collection to store the email in.
     * @param userId     The ID of the user.
     * @param email      The email address to store.
     * @return True if the email was stored successfully, false otherwise.
     */
    public boolean storeEmail(String collection, String userId, String email) {
        try {
            URI uri = new URI(API_URL + "/put/" + collection + "/" + userId);
            Map<String, String> data = new HashMap<>();
            data.put("email", email);

            String json = gson.toJson(data);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + API_TOKEN)
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            // request was successful if status code is 201
            return response.statusCode() == 201;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves the email address of a user from the SafeTo database.
     *
     * @param collection The name of the collection to retrieve the email from.
     * @param userId     The ID of the user (used as key).
     * @return The email address of the user, or null if the email was not found.
     */
    public String getEmail(String collection, String userId) {
        try {
            URI uri = new URI(API_URL + "/get/" + collection + "/" + userId);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Authorization", "Bearer " + API_TOKEN)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Map<String, String> data = gson.fromJson(response.body(), Map.class);
                return data.get("email");
            } else if (response.statusCode() == 404) {
                System.out.println("Email not found for user: " + userId);
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Deletes the email address of a user from the SafeTo database.
     *
     * @param collection The name of the collection to delete the email from.
     * @param userId     The ID of the user.
     * @return True if the email was deleted successfully, false otherwise.
     */
    public boolean deleteEmail(String collection, String userId) {
        try {
            URI uri = new URI(API_URL + "/delete/" + collection + "/" + userId);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Authorization", "Bearer " + API_TOKEN)
                    .DELETE()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.statusCode() == 204;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
