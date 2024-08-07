package analysis;

import analysis.breakAndEnter.BreakAndEnterResult;
import analysis.carTheft.AutoTheftResult;
import analysis.crimeDataRanking.NeighborhoodCrimeRankingResult;
import analysis.facade.CrimeAnalysisFacade;
import com.google.gson.Gson;

public class App {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Insufficient arguments provided.");
            System.err.println("Usage for Break and Enter: break_enter <latitude> <longitude> <radius> <threshold>");
            System.err
                    .println(
                            "Usage for Auto Theft: auto_theft <latitude> <longitude> <radius> <threshold> <earliestYear>");
            System.err.println("Usage for Ranking: type3 <neighborhood> [<specificCrime>]");
            return;
        }

        String type = args[0];
        CrimeAnalysisFacade facade = new CrimeAnalysisFacade();
        Gson gson = new Gson();

        if ("break_enter".equals(type)) {
            if (args.length != 5) {
                System.err
                        .println("Usage for Break and Enter: break_enter <latitude> <longitude> <radius> <threshold>");
                return;
            }
            double latitude = Double.parseDouble(args[1]);
            double longitude = Double.parseDouble(args[2]);
            int radius = Integer.parseInt(args[3]);
            int threshold = Integer.parseInt(args[4]);
            BreakAndEnterResult result = facade.analyzeBreakAndEnter(latitude, longitude, radius, threshold);
            System.err.println("Break and Enter Result:");
            System.out.println(gson.toJson(result));
        } else if ("auto_theft".equals(type)) {
            if (args.length != 6) {
                System.err.println(
                        "Usage for Auto Theft: auto_theft <latitude> <longitude> <radius> <threshold> <earliestYear>");
                return;
            }
            double latitude = Double.parseDouble(args[1]);
            double longitude = Double.parseDouble(args[2]);
            int radius = Integer.parseInt(args[3]);
            int threshold = Integer.parseInt(args[4]);
            int earliestYear = Integer.parseInt(args[5]);
            AutoTheftResult result = facade.analyzeAutoTheft(latitude, longitude, radius, threshold, earliestYear);
            System.err.println("Auto Theft Result:");
            System.out.println(gson.toJson(result));
        } else if ("type3".equals(type)) {
            if (args.length < 2 || args.length > 3) {
                System.out.println("Usage for Ranking: type3 <neighborhood> [<specificCrime>]");
                return;
            }
            String neighborhood = args[1];
            String specificCrime = args.length == 3 ? args[2] : null;
            NeighborhoodCrimeRankingResult result = facade.getNeighborhoodRanking(neighborhood, specificCrime);
            System.err.println("Neighborhood Ranking Result:");
            System.out.println(gson.toJson(result));
        } else {
            System.err.println("Invalid type or missing parameters.");
            System.err.println("Usage for Break and Enter: break_enter <latitude> <longitude> <radius> <threshold>");
            System.err
                    .println(
                            "Usage for Auto Theft: auto_theft <latitude> <longitude> <radius> <threshold> <earliestYear>");
            System.err.println("Usage for Ranking: type3 <neighborhood> [<specificCrime>]");
        }
    }
}
