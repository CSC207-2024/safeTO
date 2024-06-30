package access;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * A public class implements src.InterfaceDataFetcher. It is responsible for
 * fetching
 * JSON format data from a specified API.
 */
public class CrimeDataFetcher implements InterfaceDataFetcher {

    private final String API_URL = "https://services.arcgis.com/S9th0jAJ7bqgIRjw/arcgis/rest/services/Major_Crime_Indicators_Open_Data/FeatureServer/0/query?where=OCC_YEAR%20BETWEEN%202019%20AND%202020&outFields=EVENT_UNIQUE_ID,OCC_DATE,OCC_YEAR,OCC_MONTH,OCC_DAY,OCC_DOY,OCC_DOW,OCC_HOUR,DIVISION,LOCATION_TYPE,PREMISES_TYPE,UCR_CODE,UCR_EXT,OFFENCE,MCI_CATEGORY,HOOD_158,NEIGHBOURHOOD_158,LONG_WGS84,LAT_WGS84,REPORT_DATE&outSR=4326&f=json";

    /**
     * Fetches crime data from pre-specified API and return it as a JSONArray
     * 
     * @return JSONArray containing the crime data or {@code null} if error occurs
     * @throws RuntimeException wrapping a JSONException if parsing of the received
     *                          JSON data fails.
     */
    @Override
    public JSONArray fetchData() {
        try {
            URI uri = new URI(API_URL);
            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            connection.disconnect();

            JSONObject jsonResponse = new JSONObject(response.toString());
            return jsonResponse.getJSONArray("features");
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

}
