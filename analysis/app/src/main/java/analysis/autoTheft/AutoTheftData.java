package analysis.autoTheft;

/**
 * A class representing data related to auto theft incidents.
 */
public class AutoTheftData {
    private String eventUniqueId;
    private int occYear;
    private String occMonth;
    private int occDay;
    private String mciCategory;
    private double latitude;
    private double longitude;

    /**
     * Constructs an AutoTheftData object with the specified details.
     *
     * @param eventUniqueId The unique identifier for the event.
     * @param occYear       The year the incident occurred.
     * @param occMonth      The month the incident occurred.
     * @param occDay        The day the incident occurred.
     * @param mciCategory   The category of the incident.
     * @param latitude      The latitude where the incident occurred.
     * @param longitude     The longitude where the incident occurred.
     */
    public AutoTheftData(String eventUniqueId, int occYear, String occMonth, int occDay,
                         String mciCategory, double latitude, double longitude) {
        this.eventUniqueId = eventUniqueId;
        this.occYear = occYear;
        this.occMonth = occMonth;
        this.occDay = occDay;
        this.mciCategory = mciCategory;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Returns the unique identifier for the event.
     *
     * @return The unique identifier for the event.
     */
    public String getEventUniqueId() {
        return eventUniqueId;
    }

    /**
     * Returns the year the incident occurred.
     *
     * @return The year the incident occurred.
     */
    public int getOccYear() {
        return occYear;
    }

    /**
     * Returns the month the incident occurred.
     *
     * @return The month the incident occurred.
     */
    public String getOccMonth() {
        return occMonth;
    }

    /**
     * Returns the day the incident occurred.
     *
     * @return The day the incident occurred.
     */
    public int getOccDay() {
        return occDay;
    }

    /**
     * Returns the category of the incident.
     *
     * @return The category of the incident.
     */
    public String getMciCategory() {
        return mciCategory;
    }

    /**
     * Returns the latitude where the incident occurred.
     *
     * @return The latitude where the incident occurred.
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Returns the longitude where the incident occurred.
     *
     * @return The longitude where the incident occurred.
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Returns a string representation of the AutoTheftData object.
     *
     * @return A string representation of the AutoTheftData object.
     */
    @Override
    public String toString() {
        return "AutoTheftData{" +
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
