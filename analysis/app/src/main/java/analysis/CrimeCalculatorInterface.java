package analysis;

import java.util.List;

public interface CrimeCalculatorInterface<T> {
    double calculateDistance(double lat1, double lon1, double lat2, double lon2);
    List<T> getCrimeDataWithinRadius(double lat, double lon, double radius);
    List<T> getCrimeDataWithinRadiusPastYear(double lat, double lon, double radius);
    double calculatePoissonProbability(double lambda, int threshold);
}
