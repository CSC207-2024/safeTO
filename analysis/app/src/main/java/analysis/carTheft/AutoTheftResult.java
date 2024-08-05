package analysis.carTheft;

import java.util.List;

public class AutoTheftResult {
    public int totalAggregatedData;
    public List<Incident> pastYearIncidents;
    public List<Incident> allKnownIncidents;
    public double probability;
    public String probabilityMessage;
    public String warning;
    public List<SafeParkingSpot> safeParkingSpots;

    public AutoTheftResult(int totalAggregatedData, List<Incident> pastYearIncidents, List<Incident> allKnownIncidents, double probability, String probabilityMessage, String warning, List<SafeParkingSpot> safeParkingSpots) {
        this.totalAggregatedData = totalAggregatedData;
        this.pastYearIncidents = pastYearIncidents;
        this.allKnownIncidents = allKnownIncidents;
        this.probability = probability;
        this.probabilityMessage = probabilityMessage;
        this.warning = warning;
        this.safeParkingSpots = safeParkingSpots;
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
