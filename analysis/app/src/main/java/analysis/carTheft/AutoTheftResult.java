package analysis.carTheft;

import java.util.List;

public class AutoTheftResult {
    public List<Incident> pastYearIncidents;
    public List<Incident> allKnownIncidents;
    public double probability;
    public String probabilityMessage;
    public String warning;
    public List<SafeParkingSpot> safeParkingSpots;

    public List<Incident> getPastYearIncidents() {
        return pastYearIncidents;
    }

    public List<Incident> getAllKnownIncidents() {
        return allKnownIncidents;
    }

    public double getProbability() {
        return probability;
    }

    public String getProbabilityMessage() {
        return probabilityMessage;
    }

    public String getWarning() {
        return warning;
    }

    public List<SafeParkingSpot> getSafeParkingSpots() {
        return safeParkingSpots;
    }

    public AutoTheftResult(List<Incident> pastYearIncidents, List<Incident> allKnownIncidents, double probability, String probabilityMessage, String warning, List<SafeParkingSpot> safeParkingSpots) {
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
