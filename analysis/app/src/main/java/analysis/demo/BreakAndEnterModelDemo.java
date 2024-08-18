package analysis.demo;

import analysis.breakAndEnter.BreakAndEnterResult;
import analysis.facade.CrimeAnalysisFacade;
import com.google.gson.Gson;

/**
 * A demo class for analyzing and displaying break and enter data within a
 * specified radius.
 */
public class BreakAndEnterModelDemo {

    /**
     * The main method that runs the demo for analyzing and displaying break and
     * enter data.
     *
     * @param args Command-line arguments (latitude, longitude, radius, threshold).
     */
    public static void main(String[] args) {
        double latitude = 43.8062893908425;
        double longitude = -79.1803868891903;
        int radius = 200;
        int threshold = 3; // Set threshold

        if (args.length == 4) {
            latitude = Double.parseDouble(args[0]);
            longitude = Double.parseDouble(args[1]);
            radius = Integer.parseInt(args[2]);
            threshold = Integer.parseInt(args[3]);
        } else if (args.length != 0) {
            System.err.println("Usage: java BreakAndEnterModelDemo <latitude> <longitude> <radius> <threshold>");
            System.exit(1);
        }

        // Create an instance of CrimeAnalysisFacade
        CrimeAnalysisFacade facade = new CrimeAnalysisFacade();

        // Analyze break and enter data using the facade
        BreakAndEnterResult result = facade.analyzeBreakAndEnter(latitude, longitude, radius, threshold);

        // Print the results
        System.err.println("All Break and Enter in the past year within the radius:");
        int index = 1;
        for (BreakAndEnterResult.Incident incident : result.getPastYearIncidents()) {
            System.out.printf("#%d, occur date: %s, distance from you: %.2f meters%n", index++, incident.occurDate, incident.distance);
        }

        System.err.println("ALL known Break and Enter within the radius:");
        index = 1;
        for (BreakAndEnterResult.Incident incident : result.getAllKnownIncidents()) {
            System.out.printf("#%d, occur date: %s, distance from you: %.2f meters%n", index++, incident.occurDate, incident.distance);
        }

        // Print probability message and warning
        System.err.println(result.getProbabilityMessage());
        System.err.println(result.getWarning());

        // Convert result to JSON
        Gson gson = new Gson();
        String jsonResult = gson.toJson(result);

        // Print JSON result
        System.out.println(jsonResult);
    }
}
