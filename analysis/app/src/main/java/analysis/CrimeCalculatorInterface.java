package analysis;

import java.util.List;

/**
 * An interface for calculating crime-related data.
 *
 * @param <T> The type of crime data to process.
 */
public interface CrimeCalculatorInterface<T> {

    /**
     * Calculates the distance between two geographical points specified by their
     * latitude and longitude.
     *
     * @param lat1 The latitude of the first point.
     * @param lon1 The longitude of the first point.
     * @param lat2 The latitude of the second point.
     * @param lon2 The longitude of the second point.
     * @return The distance between the two points in meters.
     */
    double calculateDistance(double lat1, double lon1, double lat2, double lon2);

    /**
     * Retrieves a list of crime data within a specified radius from a given point.
     *
     * @param lat The latitude of the center point.
     * @param lon The longitude of the center point.
     * @param radius The radius in meters within which to search for crime data.
     * @return A list of crime data of type T within the specified radius.
     */
    List<T> getCrimeDataWithinRadius(double lat, double lon, double radius);

    /**
     * Retrieves a list of crime data within a specified radius from a given point
     * that occurred within the past year.
     *
     * @param lat The latitude of the center point.
     * @param lon The longitude of the center point.
     * @param radius The radius in meters within which to search for crime data.
     * @return A list of crime data of type T within the specified radius that occurred in the past year.
     */
    List<T> getCrimeDataWithinRadiusPastYear(double lat, double lon, double radius);

    /**
     * Calculates the Poisson probability of observing a specified number of events
     * or fewer given an average event rate.
     *
     * @param lambda The average number of events (rate parameter).
     * @param threshold The number of events for which to calculate the probability.
     * @return The Poisson probability of observing the threshold number of events or fewer.
     */
    double calculatePoissonProbability(double lambda, int threshold);
}
