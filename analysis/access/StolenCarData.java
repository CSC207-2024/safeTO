package analysis.access;

import java.sql.Timestamp;
import java.util.List;

public class StolenCarData {
    private int objectId;
    private String eventUniqueId;
    private Timestamp reportDate;
    private Timestamp occDate;
    private String offence;
    private String mciCategory;
    private double latitude;
    private double longitude;

    public StolenCarData(int objectId, String eventUniqueId, Timestamp reportDate, Timestamp occDate,
                         String offence, String mciCategory, double latitude, double longitude) {
        this.objectId = objectId;
        this.eventUniqueId = eventUniqueId;
        this.reportDate = reportDate;
        this.occDate = occDate;
        this.offence = offence;
        this.mciCategory = mciCategory;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getObjectId() {
        return objectId;
    }

    public String getEventUniqueId() {
        return eventUniqueId;
    }

    public Timestamp getReportDate() {
        return reportDate;
    }

    public Timestamp getOccDate() {
        return occDate;
    }

    public String getOffence() {
        return offence;
    }

    public String getMciCategory() {
        return mciCategory;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return "StolenCarData{" +
                "objectId=" + objectId +
                ", eventUniqueId='" + eventUniqueId + '\'' +
                ", reportDate=" + reportDate +
                ", occDate=" + occDate +
                ", offence='" + offence + '\'' +
                ", mciCategory='" + mciCategory + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    public static void main(String[] args) {
        CrimeDataFetcher fetcher = new CrimeDataFetcher();
        StolenCarDataFetcher stolenCarDataFetcher = new StolenCarDataFetcher(fetcher);

        // 获取所有 Auto Theft 数据
        List<StolenCarData> stolenCarDataList = stolenCarDataFetcher.getAllStolenCarData();

        if (!stolenCarDataList.isEmpty()) {
            System.out.println("Data fetched successfully!");
            for (StolenCarData data : stolenCarDataList) {
                System.out.println(data);
            }
        } else {
            System.out.println("No Auto Theft data found.");
        }
    }
}
