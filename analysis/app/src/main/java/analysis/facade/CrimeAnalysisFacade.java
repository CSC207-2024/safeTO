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

    public BreakAndEnterResult analyzeBreakAndEnter(double latitude, double longitude, int radius, int threshold) {
        return breakAndEnterFacade.analyze(latitude, longitude, radius, threshold);
    }

    public AutoTheftResult analyzeAutoTheft(double latitude, double longitude, int radius, int threshold,
                                            int earliestYear) {
        return autoTheftFacade.analyze(latitude, longitude, radius, threshold, earliestYear);
    }

    public NeighborhoodCrimeRankingResult getNeighborhoodRanking(String neighborhood, String specificCrime) {
        return crimeDataRankingFacade.getNeighborhoodRanking(neighborhood, specificCrime);
    }
}
