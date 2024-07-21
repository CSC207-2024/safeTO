package location;

/**
 * Represents a geographical simple location.
 */
public class SimpleLocation implements Location {
    private double latitude;
    private double longitude;

    /**
     * Constructs a new Location.
     *
     * @param latitude   the latitude of the location
     * @param longitude  the longitude of the location
     */
    public SimpleLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Gets the latitude of the location.
     *
     * @return the latitude of the location
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude of the location.
     *
     * @param latitude the latitude of the location
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * Gets the longitude of the location.
     *
     * @return the longitude of the location
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude of the location.
     *
     * @param longitude the longitude of the location
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    

    @Override
    public String toString() {
        return "Location{" +
                "latitude=" + super.getLatitude() +
                ", longitude=" + super.getLongitude()
                '}';
    }

}
