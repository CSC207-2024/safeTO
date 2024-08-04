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
import java.net.URLEncoder;
import java.util.Arrays;
import types.Place;

import http.Client;
import java.util.logging.Logger;
import gson.GsonSingleton;

public class Geocoding {
    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search?q=%s&format=json&addressdetails=1";
    private static final Gson gson = GsonSingleton.getInstance();
    private static final Logger logger = Logger.getLogger(Geocoding.class.getName());

    public Place[] resolve(String query) {
        try {
            // Prepare the URL-encoded query string
            String url = String.format(NOMINATIM_URL, URLEncoder.encode(query, "UTF-8"));
            logger.info("Request URL: " + url);

            // Create an HttpRequest
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url))
                    .header("User-Agent", "safeTO <backend@csc207.joefang.org>") // Optional: Include user-agent
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = Client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check if response status is OK (200)
            if (response.statusCode() == 200) {
                return gson.fromJson(response.body(), Place[].class);
            } else {
                logger.severe("Error: Received HTTP status code " + response.statusCode());
            }
        } catch (JsonSyntaxException e) {
            logger.severe("Error parsing JSON response: " + e.getMessage());
        } catch (Throwable e) {
            logger.severe("Unhandled error: " + e.getMessage());
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
