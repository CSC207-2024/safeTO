package user;
import java.util.regex.Pattern;

/**
 * Represents a geographical location.
 */
public class Location {

    private static final Pattern POSTAL_CODE_PATTERN = Pattern.compile("^[A-Z]\\d[A-Z] \\d[A-Z]\\d$");

    private double latitude;
    private double longitude;
    private String postalCode;
    private String address;

    /**
     * Constructs a new Location.
     *
     * @param latitude   the latitude of the location
     * @param longitude  the longitude of the location
     * @param postalCode the postal code of the location
     * @param address    the address of the location
     * @throws IllegalArgumentException if the postal code is not in the correct format
     */
    public Location(double latitude, double longitude, String postalCode, String address) {
        this.latitude = latitude;
        this.longitude = longitude;
        setPostalCode(postalCode); // Using setter to validate
        this.address = address;
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

    /**
     * Gets the postal code of the location.
     *
     * @return the postal code of the location
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Sets the postal code of the location.
     *
     * @param postalCode the postal code of the location
     * @throws IllegalArgumentException if the postal code is not in the correct format
     */
    public void setPostalCode(String postalCode) {
        if (!POSTAL_CODE_PATTERN.matcher(postalCode).matches()) {
            throw new IllegalArgumentException("Invalid Canadian postal code format");
        }
        this.postalCode = postalCode;
    }

    /**
     * Gets the address of the location.
     *
     * @return the address of the location
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the address of the location.
     *
     * @param address the address of the location
     */
    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Location{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", postalCode='" + postalCode + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public static void main(String[] args) {
        // Example usage
        try {
            Location location = new Location(43.6532, -79.3832, "M5V 3C6", "123 Main St, Toronto, ON");
            System.out.println(location);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }
}
