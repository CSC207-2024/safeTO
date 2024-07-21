package location;
import java.util.regex.Pattern;

/**
 * Represents a geographical strong location.
 */
public class StrongLocation extends SimpleLocation{

    //Use Regex to validate Canadian postcode 'A1A 1A1'
    private static final Pattern POSTAL_CODE_PATTERN = Pattern.compile("^[A-Z]\\d[A-Z] \\d[A-Z]\\d$");

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
    public StrongLocation(double latitude, double longitude, String postalCode, String address) {
        super(latitude, longitude);
    
        setPostalCode(postalCode); // Using setter to validate
        this.address = address;
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
                "latitude=" + super.getLatitude() +
                ", longitude=" + super.getLongitude() +
                ", postalCode='" + getPostalCode() + '\'' +
                ", address='" + getAddress() + '\'' +
                '}';
    }

    // public static void main(String[] args) {
    // 
    //     try {
    //         Location location = new Location(43.6532, -79.3832, "M5V 3C6", "123 Main St, Toronto, ON");
    //         System.out.println(location);
    //     } catch (IllegalArgumentException e) {
    //         System.err.println(e.getMessage());
    //     }
    // }
}
