package analysis;

import access.CrimeDataConverter;
import access.CrimeDataFetcher;
import access.CrimeDataProcessor;

import java.util.Comparator;
import java.util.List;

public class BreakAndEnterModelDemo {
    public static void main(String[] args) {
        CrimeDataFetcher fetcher = new CrimeDataFetcher();
        CrimeDataConverter converter = new CrimeDataConverter();
        CrimeDataProcessor processor = new CrimeDataProcessor();

        BreakAndEnterIncidentFetcher breakAndEnterIncidentFetcher = new BreakAndEnterIncidentFetcher(fetcher, converter, processor);
        BreakAndEnterCalculator breakAndEnterCalculator = new BreakAndEnterCalculator(breakAndEnterIncidentFetcher);

        double latitude = 43.8062893908425;
        double longitude = -79.1803868891903;
        int radius = 200;
        int threshold = 3; // Set threshold

        // Get break and enter data for the past year
        List<BreakAndEnterData> pastYearData = breakAndEnterCalculator.getCrimeDataWithinRadiusPastYear(latitude, longitude, radius);
        pastYearData.sort(Comparator.comparing(BreakAndEnterData::getOccYear)
                .thenComparing(BreakAndEnterData::getOccMonth)
                .thenComparing(BreakAndEnterData::getOccDay)); // Sort by date

        System.out.println("All Break and Enter in the past year within the radius:");
        int index = 1;
        for (BreakAndEnterData data : pastYearData) {
            double distance = breakAndEnterCalculator.calculateDistance(latitude, longitude, data.getLatitude(), data.getLongitude());
            System.out.printf("#%d, occur date: %d-%s-%d, distance from you: %.2f meters%n", index++, data.getOccYear(), data.getOccMonth(), data.getOccDay(), distance);
        }

        // Get all break and enter data within the radius
        List<BreakAndEnterData> allData = breakAndEnterCalculator.getCrimeDataWithinRadius(latitude, longitude, radius);
        allData.sort(Comparator.comparing(BreakAndEnterData::getOccYear)
                .thenComparing(BreakAndEnterData::getOccMonth)
                .thenComparing(BreakAndEnterData::getOccDay));

        System.out.println("ALL known Break and Enter within the radius:");
        index = 1;
        for (BreakAndEnterData data : allData) {
            double distance = breakAndEnterCalculator.calculateDistance(latitude, longitude, data.getLatitude(), data.getLongitude());
            System.out.printf("#%d, occur date: %d-%s-%d, distance from you: %.2f meters%n", index++, data.getOccYear(), data.getOccMonth(), data.getOccDay(), distance);
        }

        // Calculate the average annual rate of incidents
        double lambda = breakAndEnterCalculator.calculateAnnualAverageIncidents(allData); // Use the average annual rate of incidents as the λ value
        double probability = breakAndEnterCalculator.calculatePoissonProbability(lambda, threshold);

        // Display the probability and recommendation
        System.out.printf("Based on past data, within %dm of radius, there's a %.2f%% chance that break and enters happen more than %d time(s) within a year.%n", radius, probability * 100, threshold);

        if (probability > 0.15) {
            System.out.println("WARNING: Don't live here!");
        } else {
            System.out.println("Safe to live here!");
        }
    }
}
