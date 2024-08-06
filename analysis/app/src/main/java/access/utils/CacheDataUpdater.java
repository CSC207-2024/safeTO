package access.utils;

import com.google.gson.*;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

/**
 * A utility class for updating the cache with the latest data from the API.
 */
public class CacheDataUpdater {
    private final String cacheDir;
    private final HttpClient httpClient;
    private final String BASE_API_URL = "https://services.arcgis.com/S9th0jAJ7bqgIRjw/arcgis/rest/services/Major_Crime_Indicators_Open_Data/FeatureServer/0/query?outFields=EVENT_UNIQUE_ID,OCC_DATE,OCC_YEAR,OCC_MONTH,OCC_DAY,OCC_DOY,OCC_DOW,OCC_HOUR,DIVISION,LOCATION_TYPE,PREMISES_TYPE,UCR_CODE,UCR_EXT,OFFENCE,MCI_CATEGORY,HOOD_140,NEIGHBOURHOOD_140,LONG_WGS84,LAT_WGS84,REPORT_DATE&outSR=4326&f=json";

    /**
     * Constructs a CacheDataUpdater with the specified cache directory.
     *
     * @param cacheDir The path to the cache directory.
     */
    public CacheDataUpdater(String cacheDir) {
        this.cacheDir = cacheDir;
        this.httpClient = HttpClient.newHttpClient();
    }

    /**
     * Updates the cache directory with the latest data from the API if there are any differences.
     * Returns the new data that was added.
     *
     * @return A JsonArray containing the new data added to the cache.
     */
    public JsonArray updateCache() {
        JsonArray newData = new JsonArray();

        for (int year = 2014; year <= 2024; year++) {
            int offset = 0;
            boolean hasMoreData = true;

            while (hasMoreData) {
                String whereClause = "OCC_YEAR=" + year;
                String apiUrl = BASE_API_URL + "&where=" + URLEncoder.encode(whereClause, StandardCharsets.UTF_8)
                        + "&resultOffset=" + offset + "&resultRecordCount=2000";
                String cacheFileName = cacheDir + year + "_" + offset + ".json";

                try {
                    HttpResponse<String> response = httpClient.send(HttpRequest.newBuilder()
                                    .GET()
                                    .uri(URI.create(apiUrl))
                                    .setHeader("user-agent", "safeTO <analysis@csc207.joefang.org>")
                                    .setHeader("accept", "application/json")
                                    .build(),
                            HttpResponse.BodyHandlers.ofString());

                    JsonObject latestData = JsonParser.parseString(response.body()).getAsJsonObject();

                    if (new File(cacheFileName).exists()) {
                        JsonObject cachedData = JsonParser.parseReader(new FileReader(cacheFileName)).getAsJsonObject();

                        if (!latestData.equals(cachedData)) {
                            Set<String> cachedIds = getIdsFromData(cachedData);
                            JsonArray latestFeatures = latestData.getAsJsonArray("features");

                            for (JsonElement element : latestFeatures) {
                                JsonObject feature = element.getAsJsonObject();
                                String eventId = feature.getAsJsonObject("attributes").get("EVENT_UNIQUE_ID").getAsString();
                                if (!cachedIds.contains(eventId)) {
                                    newData.add(feature);
                                }
                            }

                            try (FileWriter writer = new FileWriter(cacheFileName)) {
                                Gson gson = new Gson();
                                gson.toJson(latestData, writer);
                            }
                        }
                    } else {
                        try (FileWriter writer = new FileWriter(cacheFileName)) {
                            Gson gson = new Gson();
                            gson.toJson(latestData, writer);
                        }
                    }

                    if (latestData.has("features")) {
                        JsonArray features = latestData.getAsJsonArray("features");
                        if (features.size() < 2000) {
                            hasMoreData = false;
                        } else {
                            offset += 2000;
                        }
                    } else {
                        hasMoreData = false;
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        return newData;
    }

    /**
     * Extracts a set of event IDs from the given data.
     *
     * @param data The data from which to extract event IDs.
     * @return A set of event IDs.
     */
    private Set<String> getIdsFromData(JsonObject data) {
        Set<String> ids = new HashSet<>();
        if (data.has("features")) {
            JsonArray features = data.getAsJsonArray("features");
            for (JsonElement element : features) {
                JsonObject feature = element.getAsJsonObject();
                String eventId = feature.getAsJsonObject("attributes").get("EVENT_UNIQUE_ID").getAsString();
                ids.add(eventId);
            }
        }
        return ids;
    }
}
