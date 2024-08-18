package analysis.breakAndEnter;

import java.util.List;

/**
 * A class representing the result of a break and enter analysis.
 * It includes data about incidents within the past year, all known incidents,
 * the probability of incidents exceeding a threshold, and corresponding messages.
 */
public class BreakAndEnterResult {
    private List<Incident> pastYearIncidents;
    private List<Incident> allKnownIncidents;
    private double probability;
    private String probabilityMessage;
    private String warning;

    /**
     * Constructs a BreakAndEnterResult with the specified details.
     *
     * @param pastYearIncidents   The list of incidents in the past year within the specified radius.
     * @param allKnownIncidents   The list of all known incidents within the specified radius.
     * @param probability         The probability of incidents exceeding the threshold.
     * @param probabilityMessage  A message summarizing the probability result.
     * @param warning             A warning message based on the probability result.
     */
    public BreakAndEnterResult(List<Incident> pastYearIncidents, List<Incident> allKnownIncidents,
                               double probability, String probabilityMessage, String warning) {
        this.pastYearIncidents = pastYearIncidents;
        this.allKnownIncidents = allKnownIncidents;
        this.probability = probability;
        this.probabilityMessage = probabilityMessage;
        this.warning = warning;
    }

    /**
     * Returns the list of incidents in the past year within the specified radius.
     *
     * @return The list of incidents in the past year.
     */
    public List<Incident> getPastYearIncidents() {
        return pastYearIncidents;
    }

    /**
     * Returns the list of all known incidents within the specified radius.
     *
     * @return The list of all known incidents.
     */
    public List<Incident> getAllKnownIncidents() {
        return allKnownIncidents;
    }

    /**
     * Returns the probability of incidents exceeding the threshold.
     *
     * @return The probability value.
     */
    public double getProbability() {
        return probability;
    }

    /**
     * Returns the probability message summarizing the result.
     *
     * @return The probability message.
     */
    public String getProbabilityMessage() {
        return probabilityMessage;
    }

    /**
     * Returns the warning message based on the probability result.
     *
     * @return The warning message.
     */
    public String getWarning() {
        return warning;
    }

    /**
     * A class representing a specific break and enter incident with its occurrence date and distance.
     */
    public static class Incident {
        private final String occurDate;
        private final double distance;

        /**
         * Constructs an Incident with the specified details.
         *
         * @param occurDate The date of the incident occurrence.
         * @param distance  The distance from the specified point to the incident location.
         */
        public Incident(String occurDate, double distance) {
            this.occurDate = occurDate;
            this.distance = distance;
        }

        /**
         * Returns the date of the incident occurrence.
         *
         * @return The date of the incident.
         */
        public String getOccurDate() {
            return occurDate;
        }

        /**
         * Returns the distance from the specified point to the incident location.
         *
         * @return The distance of the incident from the specified point.
         */
        public double getDistance() {
            return distance;
        }
    }
}
