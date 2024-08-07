package analysis.facade;

import analysis.breakAndEnter.BreakAndEnterResult;
import analysis.carTheft.AutoTheftResult;
import analysis.crimeDataRanking.NeighborhoodCrimeRankingResult;
import org.springframework.stereotype.Service;

@Service
public class CrimeAnalysisFacade {

    private final BreakAndEnterFacade breakAndEnterFacade;
    private final AutoTheftFacade autoTheftFacade;
    private final CrimeDataRankingFacade crimeDataRankingFacade;

    public CrimeAnalysisFacade() {
        this.breakAndEnterFacade = new BreakAndEnterFacade();
        this.autoTheftFacade = new AutoTheftFacade();
        this.crimeDataRankingFacade = new CrimeDataRankingFacade();
    }

    public BreakAndEnterResult analyzeBreakAndEnter(double latitude, double longitude, int radius, int threshold) {
        return breakAndEnterFacade.analyze(latitude, longitude, radius, threshold);
    }

    public AutoTheftResult analyzeAutoTheft(double latitude, double longitude, int radius, int threshold, int earliestYear) {
        return autoTheftFacade.analyze(latitude, longitude, radius, threshold, earliestYear);
    }

    public NeighborhoodCrimeRankingResult getNeighborhoodRanking(String neighborhood, String specificCrime) {
        return crimeDataRankingFacade.getNeighborhoodRanking(neighborhood, specificCrime);
    }
}
