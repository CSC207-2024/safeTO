package backend.app.src.main.java.dataAccess;

import java.sql.Timestamp;

public class StolenCarData {
    private String eventUniqueId;
    private Timestamp reportDate;
    private Timestamp occDate;
    private String mciCategory;
    private double latitude;
    private double longitude;

    public StolenCarData(String eventUniqueId, Timestamp reportDate, Timestamp occDate,
                          String mciCategory, double latitude, double longitude) {
        this.eventUniqueId = eventUniqueId;
        this.reportDate = reportDate;
        this.occDate = occDate;
        this.mciCategory = mciCategory;
        this.latitude = latitude;
        this.longitude = longitude;
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
                ", eventUniqueId='" + eventUniqueId + '\'' +
                ", reportDate=" + reportDate +
                ", occDate=" + occDate +
                ", mciCategory='" + mciCategory + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}