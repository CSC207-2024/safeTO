package access.data;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * A public class implements InterfaceDataFetcher It is responsible for
 * fetching JSON format data from a specified API.
 */
public class CrimeDataFetcher implements InterfaceDataFetcher {
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final String BASE_API_URL = "https://services.arcgis.com/S9th0jAJ7bqgIRjw/arcgis/rest/services/Major_Crime_Indicators_Open_Data/FeatureServer/0/query?outFields=EVENT_UNIQUE_ID,OCC_DATE,OCC_YEAR,OCC_MONTH,OCC_DAY,OCC_DOY,OCC_DOW,OCC_HOUR,DIVISION,LOCATION_TYPE,PREMISES_TYPE,UCR_CODE,UCR_EXT,OFFENCE,MCI_CATEGORY,HOOD_140,NEIGHBOURHOOD_140,LONG_WGS84,LAT_WGS84,REPORT_DATE&outSR=4326&f=json";
    private static final String CACHE_DIR = CacheDirectory.getCacheDir();
    private static final String AGGREGATED_DATA_FILENAME = "aggregated_data.json";
    private static final JsonArray cachedAggregatedData;

    static {
        createCacheDir();
        cachedAggregatedData = initializeAggregatedData();
    }

    /**
     * Constructs a CrimeDataFetcher.
     */
    private static void createCacheDir() {
        File cacheDir = new File(CACHE_DIR);
        if (!cacheDir.exists()) {
            cacheDir.mkdir();
        }
    }

    /**
     * Fetches crime data from pre-specified API and return it as a JSONArray,
     * it includes data from 2019 to 2024.
     *
     * @return JSONArray containing the crime data or {@code null} if error occurs
     * @throws RuntimeException wrapping a JSONException if parsing of the received
     *                          JSON data fails.
     */
    public JsonArray fetchData() {
        return cachedAggregatedData;
    }

    private static JsonArray initializeAggregatedData() {
        File cachedAggregateData = new File(CACHE_DIR, AGGREGATED_DATA_FILENAME);
        Gson gson = new Gson();
        if (cachedAggregateData.exists()) {
            System.err.println("Hit from Cached Aggregate Data: " + cachedAggregateData.getName());
            try {
                long start = System.currentTimeMillis();
                JsonArray content = gson.fromJson(new FileReader(cachedAggregateData), JsonArray.class);
                long curr = System.currentTimeMillis();
                System.err.println("Total aggregated data: " + content.size());
                System.err.println("initializeAggregatedData: " + (curr - start) / 1000.0);
                return content;
            } catch (Throwable e) {
            }
        }
        JsonArray aggregatedData = new JsonArray();

        long start = System.currentTimeMillis();
        try {
            // iterate over 2014 to 2024
            for (int year = 2014; year <= 2024; year++) {
                int offset = 0;
                boolean hasMoreData = true;

                while (hasMoreData) {
                    String whereClause = "OCC_YEAR=" + year;
                    // ensure it is properly formatted for URL
                    String apiUrl = BASE_API_URL + "&where=" + URLEncoder.encode(whereClause, StandardCharsets.UTF_8)
                            + "&resultOffset=" + offset + "&resultRecordCount=2000";
                    File cacheFile = new File(CACHE_DIR, String.format("%s_%s.json", year, offset));
                    JsonObject jsonResponse;

                    if (cacheFile.exists()) {
                        System.err.println("Hit from Cache: " + cacheFile.getName());
                        jsonResponse = gson.fromJson(new FileReader(cacheFile), JsonObject.class);
                    } else {
                        HttpResponse<String> response = httpClient.send(HttpRequest.newBuilder()
                                .GET()
                                .uri(URI.create(apiUrl))
                                .setHeader("user-agent", "safeTO <analysis@csc207.joefang.org>")
                                .setHeader("accept", "application/json")
                                .build(),
                                HttpResponse.BodyHandlers.ofString());

                        jsonResponse = gson.fromJson(response.body(), JsonObject.class);

                        try (FileWriter writer = new FileWriter(cacheFile)) {
                            gson.toJson(jsonResponse, writer);
                        }
                    }

                    if (jsonResponse.has("features")) {
                        JsonArray features = jsonResponse.getAsJsonArray("features");

                        for (JsonElement element : features) {
                            JsonObject feature = element.getAsJsonObject();
                            if (feature.has("attributes")) {
                                aggregatedData.add(feature.getAsJsonObject("attributes"));
                            }
                        }

                        if (features.size() < 2000) {
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
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        long curr = System.currentTimeMillis();
        System.err.println("Total aggregated data: " + aggregatedData.size());
        System.err.println("initializeAggregatedData: " + (curr - start) / 1000.0);

        try (FileWriter writer = new FileWriter(cachedAggregateData)) {
            gson.toJson(aggregatedData, writer);
        } catch (Throwable e) {
        }
        return aggregatedData;
    }
}
