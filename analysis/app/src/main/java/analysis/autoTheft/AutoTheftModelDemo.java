package analysis.autoTheft;

import access.convert.CrimeDataConverter;
import access.data.CrimeDataFetcher;
import access.manipulate.CrimeDataProcessor;

import java.util.Comparator;
import java.util.List;

/**
 * A demo class for analyzing and displaying auto theft data within a specified radius.
 */
public class AutoTheftModelDemo {

    /**
     * The main method that runs the demo for analyzing and displaying auto theft data.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        CrimeDataFetcher fetcher = new CrimeDataFetcher();
        CrimeDataConverter converter = new CrimeDataConverter();
        CrimeDataProcessor processor = new CrimeDataProcessor();

        AutoTheftIncidentFetcher autoTheftIncidentFetcher = new AutoTheftIncidentFetcher(fetcher, converter, processor);
        AutoTheftCalculator autoTheftCalculator = new AutoTheftCalculator(autoTheftIncidentFetcher);

        double latitude = 43.64444078400003;
        double longitude = -79.40006913299997;
        int radius = 200;
        int threshold = 10; // Set threshold

        // Get auto theft data for the past year
        List<AutoTheftData> pastYearData = autoTheftCalculator.getCrimeDataWithinRadiusPastYear(latitude, longitude, radius);
        pastYearData.sort(Comparator.comparing(AutoTheftData::getOccYear)
                .thenComparing(AutoTheftData::getOccMonth)
                .thenComparing(AutoTheftData::getOccDay)); // Sort by date

        System.out.println("All Auto Theft in the past year within the radius:");
        int index = 1;
        for (AutoTheftData data : pastYearData) {
            double distance = autoTheftCalculator.calculateDistance(latitude, longitude, data.getLatitude(), data.getLongitude());
            System.out.printf("#%d, occur date: %d-%s-%d, distance from you: %.2f meters%n", index++, data.getOccYear(), data.getOccMonth(), data.getOccDay(), distance);
        }

        // Get all auto theft data within the radius
        List<AutoTheftData> allData = autoTheftCalculator.getCrimeDataWithinRadius(latitude, longitude, radius);
        allData.sort(Comparator.comparing(AutoTheftData::getOccYear)
                .thenComparing(AutoTheftData::getOccMonth)
                .thenComparing(AutoTheftData::getOccDay));

        System.out.println("ALL known Auto Theft within the radius:");
        index = 1;
        for (AutoTheftData data : allData) {
            double distance = autoTheftCalculator.calculateDistance(latitude, longitude, data.getLatitude(), data.getLongitude());
            System.out.printf("#%d, occur date: %d-%s-%d, distance from you: %.2f meters%n", index++, data.getOccYear(), data.getOccMonth(), data.getOccDay(), distance);
        }

        // Calculate the average annual rate of incidents
        double lambda = autoTheftCalculator.calculateAnnualAverageIncidents(allData); // Use the average annual rate of incidents as the λ value
        double probability = autoTheftCalculator.calculatePoissonProbability(lambda, threshold);

        // Display the probability and recommendation
        System.out.printf("Based on past data, within %dm of radius, there's a %.2f%% chance that auto thefts happen more than %d time(s) within a year.%n", radius, probability * 100, threshold);

        if (probability > 0.15) {
            System.out.println("WARNING: Don't park here!");
        } else {
            System.out.println("Safe to park here!");
        }
    }
}
