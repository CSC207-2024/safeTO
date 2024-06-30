import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

import location.Location;

public class ReverseGeocoding {
    public static String getNeighborhoodByCoordinates(Location location) {
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
                JSONObject jsonResponse = new JSONObject(response.toString());
                if (jsonResponse.has("address") && jsonResponse.getJSONObject("address").has("neighbourhood")) {
                    return jsonResponse.getJSONObject("address").getString("neighbourhood");
                }
            } else {
                System.out.println("GET request not worked, Response Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        double latitude = 43.651070;
        double longitude = -79.347015;
        String neighborhood = getNeighborhoodByCoordinates(latitude, longitude);
        if (neighborhood != null) {
            System.out.println("The neighborhood is: " + neighborhood);
        } else {
            System.out.println("Neighborhood not found.");
        }
    }
}
