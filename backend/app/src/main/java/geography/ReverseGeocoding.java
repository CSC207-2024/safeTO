package geography;

import com.google.gson.Gson;
import location.Location;
import location.SimpleLocation;
import types.Place;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public class ReverseGeocoding {
    private static final Gson gson = new Gson();
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public static Place resolve(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        String apiUrl = String.format(
                "https://nominatim.openstreetmap.org/reverse?lat=%f&lon=%f&format=json&addressdetails=1",
                latitude,
                longitude);

        try {
            HttpResponse<String> response = httpClient.send(HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(apiUrl))
                    .setHeader(
                            "user-agent",
                            "safeTO <client@csc207.joefang.org>")
                    .build(),
                    BodyHandlers.ofString());

            int statusCode = response.statusCode();
            if (statusCode == 200) { // HTTP OK
                // Parse JSON response
                return gson.fromJson(response.body(), Place.class);
            } else {
                System.err.println("GET request not worked, Response Code: " + statusCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getNeighborhoodByCoordinates(Location location) {
        Place place = resolve(location);
        return place.getAddress().getNeighbourhood();
    }

    public static void main(String[] args) {
        double latitude = 43.651070;
        double longitude = -79.347015;
        String neighborhood = getNeighborhoodByCoordinates(new SimpleLocation(latitude, longitude));
        if (neighborhood != null) {
            System.out.println("The neighborhood is: " + neighborhood);
        } else {
            System.out.println("Neighborhood not found.");
        }
    }
}
