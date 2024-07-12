package analysis;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

public class BreakAndEnterCalculator implements CrimeCalculatorInterface<BreakAndEnterData> {

    private final IncidentFetcherInterface<BreakAndEnterData> breakAndEnterIncidentFetcher;

    public BreakAndEnterCalculator(IncidentFetcherInterface<BreakAndEnterData> breakAndEnterIncidentFetcher) {
        this.breakAndEnterIncidentFetcher = breakAndEnterIncidentFetcher;
    }

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

    private List<BreakAndEnterData> filterDataByRadius(double lat, double lon, double radius, List<BreakAndEnterData> data) {
        List<BreakAndEnterData> filteredData = new ArrayList<>();
        for (BreakAndEnterData item : data) {
            double distance = calculateDistance(lat, lon, item.getLatitude(), item.getLongitude());
            if (distance <= radius) {
                filteredData.add(item);
            }
        }
        return filteredData;
    }

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

    @Override
    public List<BreakAndEnterData> getCrimeDataWithinRadius(double lat, double lon, double radius) {
        List<BreakAndEnterData> breakAndEnterDataList = breakAndEnterIncidentFetcher.fetchCrimeData();
        return filterDataByRadius(lat, lon, radius, breakAndEnterDataList);
    }

    @Override
    public List<BreakAndEnterData> getCrimeDataWithinRadiusPastYear(double lat, double lon, double radius) {
        List<BreakAndEnterData> breakAndEnterDataList = breakAndEnterIncidentFetcher.fetchCrimeData();
        List<BreakAndEnterData> pastYearData = filterDataByYear(breakAndEnterDataList);
        return filterDataByRadius(lat, lon, radius, pastYearData);
    }

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

    @Override
    public double calculatePoissonProbability(double lambda, int threshold) {
        double cumulativeProbability = 0.0;
        for (int k = 0; k <= threshold; k++) {
            cumulativeProbability += (Math.pow(lambda, k) * Math.exp(-lambda)) / factorial(k);
        }
        return 1 - cumulativeProbability;
    }

    private int factorial(int n) {
        if (n == 0) {
            return 1;
        }
        return n * factorial(n - 1);
    }
}
