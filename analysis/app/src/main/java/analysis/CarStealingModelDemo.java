package analysis;

import access.CrimeDataConverter;
import access.CrimeDataFetcher;
import access.CrimeDataFetcherInterface;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;

public class CarStealingModelDemo {
    public static void main(String[] args) {
        CrimeDataFetcher fetcher = new CrimeDataFetcher();
        CrimeDataConverter converter = new CrimeDataConverter();

        CrimeDataFetcherInterface<StolenCarData> stolenCarDataFetcher = new StolenCarDataFetcher(fetcher, converter);
        AutoTheftCalculator autoTheftCalculator = new AutoTheftCalculator(stolenCarDataFetcher);

        double latitude = 43.64444078400003;
        double longitude = -79.40006913299997;
        int radius = 200;
        int threshold = 10; // Set threshold

        // Get stolen car data for the past year
        List<StolenCarData> pastYearData = autoTheftCalculator.getCrimeDataWithinRadiusPastYear(latitude, longitude, radius);
        pastYearData.sort(Comparator.comparing(StolenCarData::getOccDate)); // Sort by date

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        System.out.println("All Auto Theft in the past year within the radius:");
        int index = 1;
        for (StolenCarData data : pastYearData) {
            double distance = autoTheftCalculator.calculateDistance(latitude, longitude, data.getLatitude(), data.getLongitude());
            String formattedDate = dateFormat.format(data.getOccDate());
            System.out.printf("#%d, occur date: %s, distance from you: %.2f meters%n", index++, formattedDate, distance);
        }

        // Get all stolen car data within the radius
        List<StolenCarData> allData = autoTheftCalculator.getCrimeDataWithinRadius(latitude, longitude, radius);
        allData.sort(Comparator.comparing(StolenCarData::getOccDate));

        System.out.println("ALL known Auto Theft within the radius:");
        index = 1;
        for (StolenCarData data : allData) {
            double distance = autoTheftCalculator.calculateDistance(latitude, longitude, data.getLatitude(), data.getLongitude());
            String formattedDate = dateFormat.format(data.getOccDate());
            System.out.printf("#%d, occur date: %s, distance from you: %.2f meters%n", index++, formattedDate, distance);
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
