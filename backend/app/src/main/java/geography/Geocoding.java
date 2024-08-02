package geography;

// <https://nominatim.org/release-docs/develop/api/Search/>

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import types.Place;

public class Geocoding {

    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search?q=%s&format=json&addressdetails=1";

    public Place[] queryNominatim(String query) {
        // Prepare the URL-encoded query string
        String url = String.format(NOMINATIM_URL, query.replace(" ", "+"));

        // Create an HttpClient instance
        HttpClient client = HttpClient.newHttpClient();

        // Create an HttpRequest
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "safeTO <backend@csc207.joefang.org>") // Optional: Include user-agent
                .build();

        try {
            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check if response status is OK (200)
            if (response.statusCode() == 200) {
                Gson gson = new GsonBuilder().create();
                // Parse the JSON response into Place array
                return gson.fromJson(response.body(), Place[].class);
            } else {
                System.err.println("Error: Received HTTP status code " + response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error occurred while sending request: " + e.getMessage());
        } catch (JsonSyntaxException e) {
            System.err.println("Error parsing JSON response: " + e.getMessage());
        }

        // Return null if there was an error
        return null;
    }

    public static void main(String[] args) {
        Geocoding geocoding = new Geocoding();
        // Example usage: Replace "Bakery in Berlin" with any query of your choice
        Place[] places = geocoding.queryNominatim("Bakery in Berlin");

        // Output the results
        if (places != null) {
            Arrays.stream(places).forEach(place -> {
                System.out.println("Place ID: " + place.getPlaceId());
                System.out.println("Display Name: " + place.getDisplayName());
                System.out.println("Latitude: " + place.getLat());
                System.out.println("Longitude: " + place.getLon());
                System.out.println("Address: " + place.getAddress().getRoad() + ", " + place.getAddress().getCity());
                System.out.println("-------------------------------");
            });
        } else {
            System.out.println("No places found for the given query.");
        }
    }
}
