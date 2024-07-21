package location;

public class SimpleLocation implements Location {
    private double latitude;
    private double longitude;

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public SimpleLocation(double latitude, double longitude) {
        this.latitude = latitude;
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
