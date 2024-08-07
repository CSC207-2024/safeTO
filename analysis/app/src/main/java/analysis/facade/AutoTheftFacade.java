package analysis.facade;

import analysis.carTheft.*;
import access.convert.CrimeDataConverter;
import access.data.CrimeDataFetcher;
import access.manipulate.CrimeDataProcessor;
import analysis.interfaces.IncidentFetcherInterface;
import analysis.utils.GeoUtils;
import tech.tablesaw.api.Row;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A facade class that provides a simplified interface to the auto theft
 * analysis system.
 */
public class AutoTheftFacade {

    private final AutoTheftCalculator autoTheftCalculator;
    private final SafeParkingLocationManager safeParkingLocationManager;

    public AutoTheftFacade() {
        CrimeDataFetcher fetcher = new CrimeDataFetcher();
        CrimeDataConverter converter = new CrimeDataConverter();
        CrimeDataProcessor processor = new CrimeDataProcessor();
        IncidentFetcherInterface<AutoTheftData> incidentFetcher = new AutoTheftIncidentFetcher(fetcher, converter,
                processor);
        this.autoTheftCalculator = new AutoTheftCalculator(incidentFetcher);
        this.safeParkingLocationManager = SafeParkingLocationManager.getInstance();
    }

    public AutoTheftResult analyze(double latitude, double longitude, int radius, int threshold, int earliestYear) {
        long start = System.currentTimeMillis();
        List<AutoTheftData> pastYearData = autoTheftCalculator.getCrimeDataWithinRadiusPastYear(latitude, longitude,
                radius);
        long curr = System.currentTimeMillis();
        System.err.println("getCrimeDataWithinRadiusPastYear: " + (curr - start) / 1000.0);

        start = curr;
        List<AutoTheftData> allKnownData = autoTheftCalculator.getCrimeDataWithinRadius(latitude, longitude, radius,
                earliestYear);
        curr = System.currentTimeMillis();
        System.err.println("getCrimeDataWithinRadius: " + (curr - start) / 1000.0);

        start = curr;
        pastYearData.sort(Comparator.comparing(AutoTheftData::getOccYear)
                .thenComparing(AutoTheftData::getOccMonth)
                .thenComparing(AutoTheftData::getOccDay));
        curr = System.currentTimeMillis();
        System.err.println("pastYearData.sort: " + (curr - start) / 1000.0);

        start = curr;
        allKnownData.sort(Comparator.comparing(AutoTheftData::getOccYear)
                .thenComparing(AutoTheftData::getOccMonth)
                .thenComparing(AutoTheftData::getOccDay));
        curr = System.currentTimeMillis();
        System.err.println("allKnownData.sort: " + (curr - start) / 1000.0);

        start = curr;
        List<AutoTheftResult.Incident> pastYearIncidents = new ArrayList<>();
        for (AutoTheftData data : pastYearData) {
            double distance = GeoUtils.calculateDistance(latitude, longitude, data.getLatitude(), data.getLongitude());
            String occurDate = data.getOccYear() + "-" + data.getOccMonth() + "-" + data.getOccDay();
            pastYearIncidents.add(new AutoTheftResult.Incident(occurDate, distance));
        }
        curr = System.currentTimeMillis();
        System.err.println("pastYearIncidents: " + (curr - start) / 1000.0);

        start = curr;
        List<AutoTheftResult.Incident> allKnownIncidents = new ArrayList<>();
        for (AutoTheftData data : allKnownData) {
            double distance = GeoUtils.calculateDistance(latitude, longitude, data.getLatitude(), data.getLongitude());
            String occurDate = data.getOccYear() + "-" + data.getOccMonth() + "-" + data.getOccDay();
            allKnownIncidents.add(new AutoTheftResult.Incident(occurDate, distance));
        }
        curr = System.currentTimeMillis();
        System.err.println("allKnownIncidents: " + (curr - start) / 1000.0);

        start = curr;
        double lambda = autoTheftCalculator.calculateAnnualAverageIncidents(allKnownData);
        curr = System.currentTimeMillis();
        System.err.println("calculateAnnualAverageIncidents: " + (curr - start) / 1000.0);

        start = curr;
        double probability = autoTheftCalculator.calculatePoissonProbability(lambda, threshold);
        curr = System.currentTimeMillis();
        System.err.println("calculatePoissonProbability: " + (curr - start) / 1000.0);

        String probabilityMessage = String.format(
                "Based on past data, within %dm of radius, there's a %.1f%% chance that auto thefts happen more than %d time(s) within a year.",
                radius, probability * 100, threshold);
        String warning = probability > 0.15 ? "WARNING: Don't park here!" : "Safe to park here!";

        List<SafeParkingSpot> safeSpotsList = new ArrayList<>();
        if (probability > 0.15) {
            List<Row> safeSpots = safeParkingLocationManager.getNearbySafeLocations(latitude, longitude, radius, 3,
                    threshold);
            for (Row spot : safeSpots) {
                double spotLat = spot.getDouble("Latitude");
                double spotLon = spot.getDouble("Longitude");
                String date = spot.getDateTime("Added Time").toLocalDate().toString();
                double spotProbability = spot.getDouble("Probability");
                int spotRadius = spot.getInt("Radius");
                int spotThreshold = spot.getInt("Threshold");

                double spotDistance = GeoUtils.calculateDistance(latitude, longitude, spotLat, spotLon);
                safeSpotsList.add(new SafeParkingSpot(spotLat, spotLon, spotDistance, date, spotProbability, spotRadius,
                        spotThreshold));
            }
        }

        return new AutoTheftResult(pastYearIncidents, allKnownIncidents, probability, probabilityMessage, warning,
                safeSpotsList);
    }
}
