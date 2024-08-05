package analysis.breakAndEnter;

import java.util.List;

public class BreakAndEnterResult {
    public int totalAggregatedData;
    public List<Incident> pastYearIncidents;
    public List<Incident> allKnownIncidents;
    public double probability;
    public String warning;

    public BreakAndEnterResult(int totalAggregatedData, List<Incident> pastYearIncidents, List<Incident> allKnownIncidents, double probability, String warning) {
        this.totalAggregatedData = totalAggregatedData;
        this.pastYearIncidents = pastYearIncidents;
        this.allKnownIncidents = allKnownIncidents;
        this.probability = probability;
        this.warning = warning;
    }

    public static class Incident {
        public String occurDate;
        public double distance;

        public Incident(String occurDate, double distance) {
            this.occurDate = occurDate;
            this.distance = distance;
        }
    }
}
