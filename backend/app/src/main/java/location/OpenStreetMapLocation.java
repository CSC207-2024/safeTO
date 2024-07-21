package location;

import types.Place;
import geography.ReverseGeocoding;

public class OpenStreetMapLocation extends SimpleLocation {
    private Place place;

    public String getAddress() {
        return place.getDisplayName();
    }

    public String getPostalCode() {
        return place.getAddress().getPostcode();    
    }

    public OpenStreetMapLocation(double latitude, double longitude) {
        super(latitude, longitude);
        place = ReverseGeocoding.resolve(this);
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
};

// https://nominatim.openstreetmap.org/reverse?lat=43.6667501&lon=-79.3919743&format=json&addressdetails=1
