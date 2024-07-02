package geography;

import com.google.gson.Gson;
import location.Location;
import location.SimpleLocation;
import types.Place;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ReverseGeocoding {
    public static Place resolve(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        String apiUrl = String.format(
                "https://nominatim.openstreetmap.org/reverse?lat=%f&lon=%f&format=json&addressdetails=1",
                latitude,
                longitude);

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) { // HTTP OK
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse JSON response
                System.out.println(response.toString());
                Gson gson = new Gson();
                Place place = gson.fromJson(response.toString(), Place.class);
                return place;
            } else {
                System.out.println("GET request not worked, Response Code: " + responseCode);
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
