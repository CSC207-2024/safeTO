package backend.types;

import java.util.Optional;

public class Location {
    private double latitude;
    private double longitude;
    private Optional<String> postalCode;
    private Optional<String> address;

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }
}
