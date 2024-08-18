package analysis.facade;

import analysis.breakAndEnter.BreakAndEnterResult;
import analysis.carTheft.AutoTheftResult;
import analysis.crimeDataRanking.NeighborhoodCrimeRankingResult;
import access.data.CrimeDataFetcher;
import access.data.InterfaceDataFetcher;
import access.convert.CrimeDataConverter;
import access.manipulate.CrimeDataProcessor;

/**
 * A facade class that provides a simplified interface to the crime analysis system.
 */
public class CrimeAnalysisFacade {

    private final BreakAndEnterFacade breakAndEnterFacade;
    private final AutoTheftFacade autoTheftFacade;
    private final CrimeDataRankingFacade crimeDataRankingFacade;

    /**
     * Constructs a CrimeAnalysisFacade with default data fetcher, converter, and processor.
     */
    public CrimeAnalysisFacade() {
        // Create instances of the necessary components
        InterfaceDataFetcher dataFetcher = new CrimeDataFetcher();  // Or any other implementation of InterfaceDataFetcher
        CrimeDataConverter converter = new CrimeDataConverter();
        CrimeDataProcessor processor = new CrimeDataProcessor();

        // Pass these instances to the BreakAndEnterFacade
        this.breakAndEnterFacade = new BreakAndEnterFacade(dataFetcher, converter, processor);

        // Assuming AutoTheftFacade and CrimeDataRankingFacade are structured similarly
        this.autoTheftFacade = new AutoTheftFacade(dataFetcher, converter, processor);
        this.crimeDataRankingFacade = new CrimeDataRankingFacade(dataFetcher, converter, processor);
    }

    /**
     * Analyzes break and enter crime data within a specified radius.
     *
     * @param latitude The latitude of the location.
     * @param longitude The longitude of the location.
     * @param radius The radius in meters to search for crime data.
     * @param threshold The threshold for determining the safety level.
     * @return A BreakAndEnterResult containing the analysis results.
     */
    public BreakAndEnterResult analyzeBreakAndEnter(double latitude, double longitude, int radius, int threshold) {
        return breakAndEnterFacade.analyze(latitude, longitude, radius, threshold);
    }

    /**
     * Analyzes auto theft crime data within a specified radius and time frame.
     *
     * @param latitude The latitude of the location.
     * @param longitude The longitude of the location.
     * @param radius The radius in meters to search for crime data.
     * @param threshold The threshold for determining the safety level.
     * @param earliestYear The earliest year of crime data to consider.
     * @return An AutoTheftResult containing the analysis results.
     */
    public AutoTheftResult analyzeAutoTheft(double latitude, double longitude, int radius, int threshold,
                                            int earliestYear) {
        return autoTheftFacade.analyze(latitude, longitude, radius, threshold, earliestYear);
    }

    /**
     * Retrieves the crime ranking of a neighborhood.
     *
     * @param neighborhood The name of the neighborhood to rank.
     * @param specificCrime The specific crime type to consider (or null for total crime).
     * @return A NeighborhoodCrimeRankingResult containing the ranking results.
     */
    public NeighborhoodCrimeRankingResult getNeighborhoodRanking(String neighborhood, String specificCrime) {
        return crimeDataRankingFacade.getNeighborhoodRanking(neighborhood, specificCrime);
    }
}
