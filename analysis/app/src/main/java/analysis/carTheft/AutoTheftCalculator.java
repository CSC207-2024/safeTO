package analysis.carTheft;

import analysis.interfaces.CrimeCalculatorInterface;
import analysis.utils.GeoUtils;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

/**
 * A class for calculating auto theft-related data.
 */
public class AutoTheftCalculator implements CrimeCalculatorInterface<AutoTheftData> {

    private final List<AutoTheftData> autoTheftDataList;

    /**
     * Constructs an AutoTheftCalculator with the specified incident data list.
     *
     * @param autoTheftDataList The list of auto theft incidents.
     */
    public AutoTheftCalculator(List<AutoTheftData> autoTheftDataList) {
        this.autoTheftDataList = autoTheftDataList;
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

    @Override
    public int getYear(AutoTheftData item) {
        return item.getOccYear();
    }
    private List<AutoTheftData> filterDataByRadius(double lat, double lon, double radius, List<AutoTheftData> data) {
        List<AutoTheftData> filteredData = new ArrayList<>();
        for (AutoTheftData item : data) {
            double distance = GeoUtils.calculateDistance(lat, lon, item.getLatitude(), item.getLongitude());
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
     * Filters a list of auto theft data to include only incidents that occurred from a specified year onwards.
     *
     * @param data The list of auto theft data to filter.
     * @param earliestYear The earliest year to consider.
     * @return A filtered list of auto theft data that occurred from the specified year onwards.
     */
    private List<AutoTheftData> filterDataByEarliestYear(List<AutoTheftData> data, int earliestYear) {
        List<AutoTheftData> filteredData = new ArrayList<>();

        for (AutoTheftData item : data) {
            if (item.getOccYear() >= earliestYear) {
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
        List<AutoTheftData> pastYearData = filterDataByYear(autoTheftDataList);
        return filterDataByRadius(lat, lon, radius, pastYearData);
    }

    /**
     * Retrieves a list of auto theft data within a specified radius from a given point
     * that occurred from a specified year onwards.
     *
     * @param lat The latitude of the center point.
     * @param lon The longitude of the center point.
     * @param radius The radius in meters within which to search for auto theft data.
     * @param earliestYear The earliest year to consider.
     * @return A list of auto theft data within the specified radius that occurred from the specified year onwards.
     */
    public List<AutoTheftData> getCrimeDataWithinRadius(double lat, double lon, double radius, int earliestYear) {
        List<AutoTheftData> filteredData = filterDataByEarliestYear(autoTheftDataList, earliestYear);
        return filterDataByRadius(lat, lon, radius, filteredData);
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
