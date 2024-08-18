package analysis.interfaces;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An interface for calculating crime-related data.
 *
 * @param <T> The type of data being processed.
 */
public interface CrimeCalculatorInterface<T> {

    /**
     * Retrieves a list of crime data within a specified radius from a given point.
     *
     * @param lat The latitude of the center point.
     * @param lon The longitude of the center point.
     * @param radius The radius in meters within which to search for crime data.
     * @return A list of crime data within the specified radius.
     */
    List<T> getCrimeDataWithinRadius(double lat, double lon, double radius);

    /**
     * Retrieves a list of crime data within a specified radius from a given point
     * that occurred within the past year.
     *
     * @param lat The latitude of the center point.
     * @param lon The longitude of the center point.
     * @param radius The radius in meters within which to search for crime data.
     * @return A list of crime data within the specified radius that occurred in the past year.
     */
    List<T> getCrimeDataWithinRadiusPastYear(double lat, double lon, double radius);

    /**
     * Retrieves a list of crime data within a specified radius from a given point
     * that occurred from a specified year onwards.
     *
     * @param lat The latitude of the center point.
     * @param lon The longitude of the center point.
     * @param radius The radius in meters within which to search for crime data.
     * @param earliestYear The earliest year to consider for filtering the data.
     * @return A list of crime data within the specified radius that occurred from the specified year onwards.
     */
    default List<T> getCrimeDataWithinRadius(double lat, double lon, double radius, int earliestYear) {
        throw new UnsupportedOperationException("This method needs to be overridden by the specific calculator.");
    }

    /**
     * Calculates the average annual number of incidents.
     *
     * @param data The list of crime data.
     * @return The average annual number of incidents.
     */
    default double calculateAnnualAverageIncidents(List<T> data) {
        // Provide a default implementation for calculating average annual incidents
        if (data.isEmpty()) {
            return 0.0;
        }

        Map<Integer, Integer> yearlyCounts = new HashMap<>();
        for (T item : data) {
            int year = getYear(item);
            yearlyCounts.put(year, yearlyCounts.getOrDefault(year, 0) + 1);
        }

        int totalYears = yearlyCounts.size();
        int totalIncidents = yearlyCounts.values().stream().mapToInt(Integer::intValue).sum();

        return totalYears == 0 ? 0.0 : totalIncidents / (double) totalYears;
    }

    /**
     * Extracts the year from a given data item.
     *
     * @param item The data item from which to extract the year.
     * @return The year of the incident.
     */
    default int getYear(T item) {
        throw new UnsupportedOperationException("This method should be implemented to extract the year from the data item.");
    }

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
