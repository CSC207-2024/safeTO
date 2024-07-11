package analysis.access;

import org.json.*;
import java.io.*;
import java.net.*;
import java.net.http.*;
import java.nio.charset.StandardCharsets;

/**
 * A public class implements InterfaceDataFetcher It is responsible for
 * fetching JSON format data from a specified API.
 */
public class CrimeDataFetcher implements InterfaceDataFetcher {
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final String BASE_API_URL = "https://services.arcgis.com/S9th0jAJ7bqgIRjw/arcgis/rest/services/Major_Crime_Indicators_Open_Data/FeatureServer/0/query?outFields=EVENT_UNIQUE_ID,OCC_DATE,OCC_YEAR,OCC_MONTH,OCC_DAY,OCC_DOY,OCC_DOW,OCC_HOUR,DIVISION,LOCATION_TYPE,PREMISES_TYPE,UCR_CODE,UCR_EXT,OFFENCE,MCI_CATEGORY,HOOD_158,NEIGHBOURHOOD_158,LONG_WGS84,LAT_WGS84,REPORT_DATE&outSR=4326&f=json";

    /**
     * Fetches crime data from pre-specified API and return it as a JSONArray,
     * it includes data from 2019 to 2024.
     *
     * @return JSONArray containing the crime data or {@code null} if error occurs
     * @throws RuntimeException wrapping a JSONException if parsing of the received
     *                          JSON data fails.
     */
    @Override
    public JSONArray fetchData() {
        JSONArray aggregatedData = new JSONArray();

        try {
            // iterate over 2019 to 2024
            for (int year = 2019; year <= 2024; year++) {
                int offset = 0;
                boolean hasMoreData = true;

                while (hasMoreData) {
                    String whereClause = "OCC_YEAR=" + year;
                    // ensure it is properly formatted for URL
                    String apiUrl = BASE_API_URL + "&where=" + URLEncoder.encode(whereClause, StandardCharsets.UTF_8)
                            + "&resultOffset=" + offset + "&resultRecordCount=2000";
                    HttpResponse<String> response = httpClient.send(HttpRequest.newBuilder()
                            .GET()
                            .uri(URI.create(apiUrl))
                            .setHeader(
                                    "user-agent",
                                    "safeTO <analysis@csc207.joefang.org>")
                            .setHeader("accept",
                                    "application/json")
                            .build(),
                            HttpResponse.BodyHandlers.ofString());

                    JSONObject jsonResponse = new JSONObject(response.body());

                    if (jsonResponse.has("features")) {
                        JSONArray features = jsonResponse.getJSONArray("features");

                        for (int i = 0; i < features.length(); i++) {
                            JSONObject feature = features.getJSONObject(i);
                            if (feature.has("attributes")) {
                                aggregatedData.put(feature.getJSONObject("attributes"));
                            }
                        }

                        if (features.length() < 2000) {
                            hasMoreData = false;
                        } else {
                            offset += 2000;
                        }
                    } else {
                        hasMoreData = false;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Total aggregated data: " + aggregatedData.length());
        return aggregatedData;
    }
}
