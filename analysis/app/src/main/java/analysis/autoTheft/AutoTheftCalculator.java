package analysis.autoTheft;

import analysis.CrimeCalculatorInterface;
import analysis.IncidentFetcherInterface;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

/**
 * A class for calculating auto theft-related data.
 */
public class AutoTheftCalculator implements CrimeCalculatorInterface<AutoTheftData> {

    private final IncidentFetcherInterface<AutoTheftData> autoTheftIncidentFetcher;

    /**
     * Constructs an AutoTheftCalculator with the specified incident fetcher.
     *
     * @param autoTheftIncidentFetcher The fetcher for auto theft incidents.
     */
    public AutoTheftCalculator(IncidentFetcherInterface<AutoTheftData> autoTheftIncidentFetcher) {
        this.autoTheftIncidentFetcher = autoTheftIncidentFetcher;
    }

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
    @Override
    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the Earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters
        return distance;
    }

    /**
     * Filters a list of auto theft data by a specified radius from a given point.
     *
     * @param lat The latitude of the center point.
     * @param lon The longitude of the center point.
     * @param radius The radius in meters within which to filter the data.
     * @param data The list of auto theft data to filter.
     * @return A filtered list of auto theft data within the specified radius.
     */
    private List<AutoTheftData> filterDataByRadius(double lat, double lon, double radius, List<AutoTheftData> data) {
        List<AutoTheftData> filteredData = new ArrayList<>();
        for (AutoTheftData item : data) {
            double distance = calculateDistance(lat, lon, item.getLatitude(), item.getLongitude());
            if (distance <= radius) {
                filteredData.add(item);
            }
        }
        return filteredData;
    }

    /**
     * Filters a list of auto theft data to include only incidents that occurred within the past year.
     *
     * @param data The list of auto theft data to filter.
     * @return A filtered list of auto theft data that occurred within the past year.
     */
    private List<AutoTheftData> filterDataByYear(List<AutoTheftData> data) {
        List<AutoTheftData> filteredData = new ArrayList<>();
        LocalDate oneYearAgo = LocalDate.now().minusYears(1);

        for (AutoTheftData item : data) {
            LocalDate occDate = LocalDate.of(item.getOccYear(), Month.valueOf(item.getOccMonth().toUpperCase()), item.getOccDay());
            if (occDate.isAfter(oneYearAgo)) {
                filteredData.add(item);
            }
        }
        return filteredData;
    }

    /**
     * Retrieves a list of auto theft data within a specified radius from a given point.
     *
     * @param lat The latitude of the center point.
     * @param lon The longitude of the center point.
     * @param radius The radius in meters within which to search for auto theft data.
     * @return A list of auto theft data within the specified radius.
     */
    @Override
    public List<AutoTheftData> getCrimeDataWithinRadius(double lat, double lon, double radius) {
        List<AutoTheftData> autoTheftDataList = autoTheftIncidentFetcher.fetchCrimeData();
        return filterDataByRadius(lat, lon, radius, autoTheftDataList);
    }

    /**
     * Retrieves a list of auto theft data within a specified radius from a given point
     * that occurred within the past year.
     *
     * @param lat The latitude of the center point.
     * @param lon The longitude of the center point.
     * @param radius The radius in meters within which to search for auto theft data.
     * @return A list of auto theft data within the specified radius that occurred in the past year.
     */
    @Override
    public List<AutoTheftData> getCrimeDataWithinRadiusPastYear(double lat, double lon, double radius) {
        List<AutoTheftData> autoTheftDataList = autoTheftIncidentFetcher.fetchCrimeData();
        List<AutoTheftData> pastYearData = filterDataByYear(autoTheftDataList);
        return filterDataByRadius(lat, lon, radius, pastYearData);
    }

    /**
     * Calculates the average annual number of auto theft incidents.
     *
     * @param data The list of auto theft data.
     * @return The average annual number of auto theft incidents.
     */
    public double calculateAnnualAverageIncidents(List<AutoTheftData> data) {
        Map<Integer, Integer> yearlyCounts = new HashMap<>();
        for (AutoTheftData item : data) {
            int year = item.getOccYear();
            yearlyCounts.put(year, yearlyCounts.getOrDefault(year, 0) + 1);
        }

        int totalYears = yearlyCounts.size();
        int totalIncidents = yearlyCounts.values().stream().mapToInt(Integer::intValue).sum();

        return totalIncidents / (double) totalYears;
    }

    /**
     * Calculates the Poisson probability of observing a specified number of events
     * or fewer given an average event rate.
     *
     * @param lambda The average number of events (rate parameter).
     * @param threshold The number of events for which to calculate the probability.
     * @return The Poisson probability of observing the threshold number of events or fewer.
     */
    @Override
    public double calculatePoissonProbability(double lambda, int threshold) {
        double cumulativeProbability = 0.0;
        for (int k = 0; k <= threshold; k++) {
            cumulativeProbability += (Math.pow(lambda, k) * Math.exp(-lambda)) / factorial(k);
        }
        return 1 - cumulativeProbability;
    }

    /**
     * Calculates the factorial of a number.
     *
     * @param n The number for which to calculate the factorial.
     * @return The factorial of the number.
     */
    private int factorial(int n) {
        if (n == 0) {
            return 1;
        }
        return n * factorial(n - 1);
    }
}
