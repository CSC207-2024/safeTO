package analysis.carTheft;

public class SafeParkingSpot {
    public double latitude;
    public double longitude;
    public double distance;
    public String addedTime;
    public double probability;
    public int radius;
    public int threshold;

    public SafeParkingSpot(double latitude, double longitude, double distance, String addedTime, double probability, int radius, int threshold) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
        this.addedTime = addedTime;
        this.probability = probability;
        this.radius = radius;
        this.threshold = threshold;
    }
}
