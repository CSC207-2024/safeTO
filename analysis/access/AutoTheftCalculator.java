package analysis.access;

import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;

public class AutoTheftCalculator {

    private final StolenCarDataFetcher stolenCarDataFetcher;

    public AutoTheftCalculator(StolenCarDataFetcher stolenCarDataFetcher) {
        this.stolenCarDataFetcher = stolenCarDataFetcher;
    }

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


    private List<StolenCarData> filterDataByRadius(double lat, double lon, double radius, List<StolenCarData> data) {
        List<StolenCarData> filteredData = new ArrayList<>();
        for (StolenCarData item : data) {
            double distance = calculateDistance(lat, lon, item.getLatitude(), item.getLongitude());
            if (distance <= radius) {
                filteredData.add(item);
            }
        }
        return filteredData;
    }

    private List<StolenCarData> filterDataByYear(List<StolenCarData> data) {
        List<StolenCarData> filteredData = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1); // get date one year ago
        Timestamp threeYearAgo = new Timestamp(calendar.getTimeInMillis());

        for (StolenCarData item : data) {
            if (item.getOccDate().after(threeYearAgo)) {
                filteredData.add(item);
            }
        }
        return filteredData;
    }

    public List<StolenCarData> getStolenCarDataWithinRadius(double lat, double lon, double radius) {
        List<StolenCarData> stolenCarDataList = stolenCarDataFetcher.getAllStolenCarData(); // get all Auto Theft data
        return filterDataByRadius(lat, lon, radius, stolenCarDataList);
    }

    public List<StolenCarData> getStolenCarDataWithinRadiusPastYear(double lat, double lon, double radius) {
        List<StolenCarData> stolenCarDataList = stolenCarDataFetcher.getAllStolenCarData(); // get all Auto Theft data
        List<StolenCarData> pastYearData = filterDataByYear(stolenCarDataList);
        return filterDataByRadius(lat, lon, radius, pastYearData);
    }

    public double calculatePoissonProbability(double lambda, int threshold) {
        // calculate P(X <= threshold)
        double cumulativeProbability = 0.0;
        for (int k = 0; k <= threshold; k++) {
            cumulativeProbability += (Math.pow(lambda, k) * Math.exp(-lambda)) / factorial(k);
        }

        // calculate P(X > threshold)
        return 1 - cumulativeProbability;
    }

    private int factorial(int n) {
        if (n == 0) {
            return 1;
        }
        return n * factorial(n - 1);
    }

}