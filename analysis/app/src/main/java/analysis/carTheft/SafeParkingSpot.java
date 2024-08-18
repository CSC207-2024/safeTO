package analysis.carTheft;

/**
 * A class representing a safe parking spot.
 */
public class SafeParkingSpot {
    private double latitude;
    private double longitude;
    private double distance;
    private String addedTime;
    private double probability;
    private int radius;
    private int threshold;

    /**
     * Constructs a SafeParkingSpot with the specified details.
     *
     * @param latitude   The latitude of the parking spot.
     * @param longitude  The longitude of the parking spot.
     * @param distance   The distance from a reference point.
     * @param addedTime  The time when the parking spot was added.
     * @param probability The probability associated with the parking spot's safety.
     * @param radius     The radius within which the probability was calculated.
     * @param threshold  The threshold used in the probability calculation.
     */
    public SafeParkingSpot(double latitude, double longitude, double distance, String addedTime,
                           double probability, int radius, int threshold) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
        this.addedTime = addedTime;
        this.probability = probability;
        this.radius = radius;
        this.threshold = threshold;
    }

    // Getter methods can be added here if needed
}
