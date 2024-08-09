package analysis.breakAndEnter;

import analysis.interfaces.CrimeCalculatorInterface;
import analysis.utils.GeoUtils;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

/**
 * A class for calculating break and enter-related data.
 */
public class BreakAndEnterCalculator implements CrimeCalculatorInterface<BreakAndEnterData> {

    private final List<BreakAndEnterData> breakAndEnterDataList;

    /**
     * Constructs a BreakAndEnterCalculator with the specified incident data.
     *
     * @param breakAndEnterDataList The list of break and enter incidents.
     */
    public BreakAndEnterCalculator(List<BreakAndEnterData> breakAndEnterDataList) {
        this.breakAndEnterDataList = breakAndEnterDataList;
    }

    /**
     * Filters a list of break and enter data by a specified radius from a given point.
     *
     * @param lat The latitude of the center point.
     * @param lon The longitude of the center point.
     * @param radius The radius in meters within which to filter the data.
     * @return A filtered list of break and enter data within the specified radius.
     */
    private List<BreakAndEnterData> filterDataByRadius(double lat, double lon, double radius, List<BreakAndEnterData> data) {
        List<BreakAndEnterData> filteredData = new ArrayList<>();
        for (BreakAndEnterData item : data) {
            double distance = GeoUtils.calculateDistance(lat, lon, item.getLatitude(), item.getLongitude());
            if (distance <= radius) {
                filteredData.add(item);
            }
        }
        return filteredData;
    }

    /**
     * Filters a list of break and enter data to include only incidents that occurred within the past year.
     *
     * @param data The list of break and enter data to filter.
     * @return A filtered list of break and enter data that occurred within the past year.
     */
    private List<BreakAndEnterData> filterDataByYear(List<BreakAndEnterData> data) {
        List<BreakAndEnterData> filteredData = new ArrayList<>();
        LocalDate oneYearAgo = LocalDate.now().minusYears(1);

        for (BreakAndEnterData item : data) {
            LocalDate occDate = LocalDate.of(item.getOccYear(), Month.valueOf(item.getOccMonth().toUpperCase()), item.getOccDay());
            if (occDate.isAfter(oneYearAgo)) {
                filteredData.add(item);
            }
        }
        return filteredData;
    }

    /**
     * Retrieves a list of break and enter data within a specified radius from a given point.
     *
     * @param lat The latitude of the center point.
     * @param lon The longitude of the center point.
     * @param radius The radius in meters within which to search for break and enter data.
     * @return A list of break and enter data within the specified radius.
     */
    @Override
    public List<BreakAndEnterData> getCrimeDataWithinRadius(double lat, double lon, double radius) {
        return filterDataByRadius(lat, lon, radius, breakAndEnterDataList);
    }

    /**
     * Retrieves a list of break and enter data within a specified radius from a given point
     * that occurred within the past year.
     *
     * @param lat The latitude of the center point.
     * @param lon The longitude of the center point.
     * @param radius The radius in meters within which to search for break and enter data.
     * @return A list of break and enter data within the specified radius that occurred in the past year.
     */
    @Override
    public List<BreakAndEnterData> getCrimeDataWithinRadiusPastYear(double lat, double lon, double radius) {
        List<BreakAndEnterData> pastYearData = filterDataByYear(breakAndEnterDataList);
        return filterDataByRadius(lat, lon, radius, pastYearData);
    }

    /**
     * Calculates the average annual number of break and enter incidents.
     *
     * @param data The list of break and enter data.
     * @return The average annual number of break and enter incidents.
     */
    public double calculateAnnualAverageIncidents(List<BreakAndEnterData> data) {
        Map<Integer, Integer> yearlyCounts = new HashMap<>();
        for (BreakAndEnterData item : data) {
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
