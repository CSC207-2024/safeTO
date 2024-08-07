package analysis.com.example.crimeanalysis.controller;

import analysis.facade.CrimeAnalysisFacade;
import analysis.breakAndEnter.BreakAndEnterResult;
import analysis.carTheft.AutoTheftResult;
import analysis.crimeDataRanking.NeighborhoodCrimeRankingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/crime")
public class CrimeAnalysisController {

    @Autowired
    private CrimeAnalysisFacade crimeAnalysisFacade;

    @GetMapping("/analyze")
    public Object analyzeCrime(
            @RequestParam String type,
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam int radius,
            @RequestParam int threshold,
            @RequestParam(required = false) Integer earliestYear) {

        if ("type1".equals(type)) {
            return crimeAnalysisFacade.analyzeBreakAndEnter(latitude, longitude, radius, threshold);
        } else if ("type2".equals(type) && earliestYear != null) {
            return crimeAnalysisFacade.analyzeAutoTheft(latitude, longitude, radius, threshold, earliestYear);
        } else {
            throw new IllegalArgumentException("Invalid type or missing parameters");
        }
    }

    @GetMapping("/ranking")
    public NeighborhoodCrimeRankingResult getNeighborhoodRanking(
            @RequestParam String neighborhood,
            @RequestParam(required = false) String specificCrime) {
        return crimeAnalysisFacade.getNeighborhoodRanking(neighborhood, specificCrime);
    }

    @GetMapping("/analyzeRanking")
    public NeighborhoodCrimeRankingResult analyzeCrimeRanking(
            @RequestParam String type,
            @RequestParam String neighborhood,
            @RequestParam(required = false) String specificCrime) {
        if ("type3".equals(type)) {
            return crimeAnalysisFacade.getNeighborhoodRanking(neighborhood, specificCrime);
        } else {
            throw new IllegalArgumentException("Invalid type or missing parameters");
        }
    }
}
