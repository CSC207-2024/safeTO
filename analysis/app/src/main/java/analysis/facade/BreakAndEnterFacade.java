package analysis.facade;

import analysis.breakAndEnter.*;
import access.convert.CrimeDataConverter;
import access.data.CrimeDataFetcher;
import access.manipulate.CrimeDataProcessor;
import analysis.interfaces.IncidentFetcherInterface;
import analysis.utils.GeoUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A facade class that provides a simplified interface to the break and enter analysis system.
 */
public class BreakAndEnterFacade {

    private final BreakAndEnterCalculator breakAndEnterCalculator;

    public BreakAndEnterFacade() {
        CrimeDataFetcher fetcher = new CrimeDataFetcher();
        CrimeDataConverter converter = new CrimeDataConverter();
        CrimeDataProcessor processor = new CrimeDataProcessor();
        IncidentFetcherInterface<BreakAndEnterData> incidentFetcher = new BreakAndEnterIncidentFetcher(fetcher, converter, processor);
        this.breakAndEnterCalculator = new BreakAndEnterCalculator(incidentFetcher);
    }

    public BreakAndEnterResult analyze(double latitude, double longitude, int radius, int threshold) {
        List<BreakAndEnterData> pastYearData = breakAndEnterCalculator.getCrimeDataWithinRadiusPastYear(latitude, longitude, radius);
        List<BreakAndEnterData> allKnownData = breakAndEnterCalculator.getCrimeDataWithinRadius(latitude, longitude, radius);

        pastYearData.sort(Comparator.comparing(BreakAndEnterData::getOccYear)
                .thenComparing(BreakAndEnterData::getOccMonth)
                .thenComparing(BreakAndEnterData::getOccDay));

        allKnownData.sort(Comparator.comparing(BreakAndEnterData::getOccYear)
                .thenComparing(BreakAndEnterData::getOccMonth)
                .thenComparing(BreakAndEnterData::getOccDay));

        List<BreakAndEnterResult.Incident> pastYearIncidents = new ArrayList<>();
        for (BreakAndEnterData data : pastYearData) {
            double distance = GeoUtils.calculateDistance(latitude, longitude, data.getLatitude(), data.getLongitude());
            String occurDate = data.getOccYear() + "-" + data.getOccMonth() + "-" + data.getOccDay();
            pastYearIncidents.add(new BreakAndEnterResult.Incident(occurDate, distance));
        }

        List<BreakAndEnterResult.Incident> allKnownIncidents = new ArrayList<>();
        for (BreakAndEnterData data : allKnownData) {
            double distance = GeoUtils.calculateDistance(latitude, longitude, data.getLatitude(), data.getLongitude());
            String occurDate = data.getOccYear() + "-" + data.getOccMonth() + "-" + data.getOccDay();
            allKnownIncidents.add(new BreakAndEnterResult.Incident(occurDate, distance));
        }

        double lambda = breakAndEnterCalculator.calculateAnnualAverageIncidents(allKnownData);
        double probability = breakAndEnterCalculator.calculatePoissonProbability(lambda, threshold);

        String warning = probability > 0.15 ? "Don't live here!" : "Safe to live here!";

        return new BreakAndEnterResult(pastYearIncidents, allKnownIncidents, probability, warning);
    }
}
