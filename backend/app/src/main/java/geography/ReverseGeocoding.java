package geography;

import com.google.gson.Gson;
import location.Location;
import types.Place;
import http.Client;
import java.net.URI;
import logging.Logger;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public class ReverseGeocoding {
    private static final Gson gson = new Gson();

    public static Place resolve(double latitude, double longitude) {
        String apiUrl = String.format(
                "https://nominatim.openstreetmap.org/reverse?lat=%f&lon=%f&format=json&addressdetails=1",
                latitude,
                longitude);

        try {
            HttpResponse<String> response = Client.send(HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(apiUrl))
                    .setHeader(
                            "user-agent",
                            "safeTO <backend@csc207.joefang.org>")
                    .build(),
                    BodyHandlers.ofString(),
                    5);

            int statusCode = response.statusCode();
            if (statusCode == 200) { // HTTP OK
                // Parse JSON response
                return gson.fromJson(response.body(), Place.class);
            } else {
                Logger.warn("GET request not worked, Response Code: " + statusCode,
                        "/backend/geography/ReverseGeocoding");
            }
        } catch (Exception e) {
            Logger.error(e.getMessage(), "/backend/geography/ReverseGeocoding");
        }
        return null;
    }

    /**
     * @param location An object that implements the location interface
     */
    public static Place resolve(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        return resolve(latitude, longitude);
    }

    public static String getNeighborhoodByCoordinates(Location location) {
        Place place = resolve(location);
        return place.getAddress().getNeighbourhood();
    }

    public static void main(String[] args) {
        double latitude = 43.651070;
        double longitude = -79.347015;
        Place place = resolve(latitude, longitude);
        String neighborhood = place.getAddress().getNeighbourhood();
        if (neighborhood != null) {
            System.out.println("The neighborhood is: " + neighborhood);
        } else {
            System.out.println("Neighborhood not found.");
            System.out.println(gson.toJson(place.getAddress()));
        }
    }
}
