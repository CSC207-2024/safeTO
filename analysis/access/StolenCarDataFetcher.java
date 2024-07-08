package analysis.access;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class StolenCarDataFetcher {

    private final CrimeDataFetcher dataFetcher;

    public StolenCarDataFetcher(CrimeDataFetcher dataFetcher) {
        this.dataFetcher = dataFetcher;
    }

    public List<src.DataAccess.StolenCarData> getAllStolenCarData() {
        List<src.DataAccess.StolenCarData> stolenCarDataList = new ArrayList<>();
        JSONArray data = dataFetcher.fetchData();
        if (data == null) {
            return stolenCarDataList;
        }

        for (int i = 0; i < data.length(); i++) {
            try {
                JSONObject feature = data.getJSONObject(i);
                JSONObject attributes = feature.getJSONObject("attributes");
                JSONObject geometry = feature.getJSONObject("geometry");
                String mciCategory = attributes.getString("MCI_CATEGORY");
                if (!mciCategory.equalsIgnoreCase("Auto Theft")) {
                    continue; // Skip non Auto Theft records
                }

                int objectId = attributes.getInt("OBJECTID");
                String eventUniqueId = attributes.getString("EVENT_UNIQUE_ID");
                Timestamp reportDate = new Timestamp(attributes.getLong("REPORT_DATE"));
                Timestamp occDate = new Timestamp(attributes.getLong("OCC_DATE"));
                String offence = attributes.getString("OFFENCE");
                double latitude = geometry.getDouble("y");
                double longitude = geometry.getDouble("x");

                src.DataAccess.StolenCarData stolenCarData = new src.DataAccess.StolenCarData(objectId, eventUniqueId, reportDate, occDate, offence, mciCategory, latitude, longitude);
                stolenCarDataList.add(stolenCarData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return stolenCarDataList;
    }

    public static void main(String[] args) {
        CrimeDataFetcher fetcher = new CrimeDataFetcher();
        StolenCarDataFetcher stolenCarDataFetcher = new StolenCarDataFetcher(fetcher);
        List<src.DataAccess.StolenCarData> stolenCarDataList = stolenCarDataFetcher.getAllStolenCarData();

        if (!stolenCarDataList.isEmpty()) {
            System.out.println("Data fetched successfully!");
            for (src.DataAccess.StolenCarData data : stolenCarDataList) {
                System.out.println(data);
            }
        } else {
            System.out.println("No Auto Theft data found.");
        }
    }
}
