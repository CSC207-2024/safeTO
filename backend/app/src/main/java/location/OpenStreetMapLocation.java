package location;

import java.util.Optional;
import types.Place;

public class OpenStreetMapLocation extends SimpleLocation {
    private Place place;

    public double getLatitude() {
        return super.getLatitude();
    }

    public double getLongitude() {
        return super.getLongitude();
    }

    public String getAddress() {
        return place.
    }

    public OpenStreetMapLocation(double latitude, double longitude) {
        super(latitude, longitude);
    }
};

// https://nominatim.openstreetmap.org/reverse?lat=43.6667501&lon=-79.3919743&format=json&addressdetails=1
