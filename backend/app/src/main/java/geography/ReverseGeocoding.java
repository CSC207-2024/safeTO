package geography;

import location.Location;
import logging.Logger;
import types.Place;
import http.Client;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import com.google.gson.Gson;

import gson.GsonSingleton;

/**
 * A class for performing reverse geocoding to resolve geographical coordinates
 * (latitude and longitude)
 * into a corresponding Place object that contains address information.
 * <p>
 * This class interacts with the Nominatim OpenStreetMap API to obtain address
 * details based
 * on given geographic coordinates. It provides methods to resolve coordinates
 * and retrieve
 * information about the neighborhood.
 * </p>
 */
public class ReverseGeocoding {
    private static final Gson gson = GsonSingleton.getInstance();

    /**
     * Resolves the given latitude and longitude to a Place object containing
     * address details.
     *
     * @param latitude  the latitude of the location to resolve
     * @param longitude the longitude of the location to resolve
     * @return a Place object populated with address details if the request is
     *         successful,
     *         or null if an error occurs or the response status is not OK
     */
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
     * Resolves a Location object into a Place object by extracting the latitude and
     * longitude
     * from the Location instance.
     *
     * @param location an object that implements the Location interface containing
     *                 latitude
     *                 and longitude information
     * @return a Place object populated with address details if the request is
     *         successful,
     *         or null if an error occurs
     */
    public static Place resolve(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        return resolve(latitude, longitude);
    }

    /**
     * Retrieves the neighborhood name associated with the provided Location object.
     *
     * @param location an object that implements the Location interface containing
     *                 latitude and longitude information
     * @return the name of the neighborhood, or null if the neighborhood is not
     *         found
     */
    public static String getNeighborhoodByCoordinates(Location location) {
        Place place = resolve(location);
        return place.getAddress().getNeighbourhood();
    }

    /**
     * Main method that demonstrates the reverse geocoding functionality.
     * It resolves a hardcoded latitude and longitude to obtain the neighborhood
     * name
     * and prints it to the console.
     *
     * @param args command-line arguments (not used in this implementation)
     */
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
