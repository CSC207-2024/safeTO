package analysis;


import access.CrimeDataConverter;
import access.CrimeDataFetcher;
import access.CrimeDataFetcherInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import tech.tablesaw.api.Table;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class StolenCarDataFetcher implements CrimeDataFetcherInterface<StolenCarData> {
    private final CrimeDataFetcher dataFetcher;
    private final CrimeDataConverter dataConverter;

    public StolenCarDataFetcher(CrimeDataFetcher dataFetcher, CrimeDataConverter dataConverter) {
        this.dataFetcher = dataFetcher;
        this.dataConverter = dataConverter;
    }

    @Override
    public List<StolenCarData> fetchCrimeData() {
        List<StolenCarData> stolenCarDataList = new ArrayList<>();
        JSONArray data = dataFetcher.fetchData();
        if (data == null) {
            return stolenCarDataList;
        }

        Table table = dataConverter.jsonToTable(data);
        if (table == null) {
            return stolenCarDataList;
        }

        for (int i = 0; i < data.length(); i++) {
            try {
                JSONObject attributes = data.getJSONObject(i);

                String mciCategory = attributes.getString("MCI_CATEGORY");
                if (!mciCategory.equalsIgnoreCase("Auto Theft")) {
                    continue; // Skip non Auto Theft records
                }

                String eventUniqueId = attributes.getString("EVENT_UNIQUE_ID");
                Timestamp reportDate = new Timestamp(attributes.getLong("REPORT_DATE"));
                Timestamp occDate = new Timestamp(attributes.getLong("OCC_DATE"));
                double latitude = attributes.getDouble("LAT_WGS84");
                double longitude = attributes.getDouble("LONG_WGS84");

                StolenCarData stolenCarData = new StolenCarData(
                        eventUniqueId, reportDate, occDate, mciCategory, latitude, longitude);
                stolenCarDataList.add(stolenCarData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return stolenCarDataList;
    }
}