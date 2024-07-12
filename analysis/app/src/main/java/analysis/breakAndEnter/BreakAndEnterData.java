package analysis;

public class BreakAndEnterData {
    private String eventUniqueId;
    private int occYear;
    private String occMonth;
    private int occDay;
    private String mciCategory;
    private double latitude;
    private double longitude;

    public BreakAndEnterData(String eventUniqueId, int occYear, String occMonth, int occDay,
                             String mciCategory, double latitude, double longitude) {
        this.eventUniqueId = eventUniqueId;
        this.occYear = occYear;
        this.occMonth = occMonth;
        this.occDay = occDay;
        this.mciCategory = mciCategory;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getEventUniqueId() {
        return eventUniqueId;
    }

    public int getOccYear() {
        return occYear;
    }

    public String getOccMonth() {
        return occMonth;
    }

    public int getOccDay() {
        return occDay;
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
        return "BreakAndEnterData{" +
                "eventUniqueId='" + eventUniqueId + '\'' +
                ", occYear=" + occYear +
                ", occMonth='" + occMonth + '\'' +
                ", occDay=" + occDay +
                ", mciCategory='" + mciCategory + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
