package analysis.crimeDataRanking;

import com.google.gson.annotations.SerializedName;

/**
 * A class representing the result of a neighborhood crime ranking analysis.
 */
public class NeighborhoodCrimeRankingResult {
    @SerializedName("neighborhood")
    private String neighborhood;

    @SerializedName("ranking")
    private int ranking;

    @SerializedName("safetyLevel")
    private String safetyLevel;

    @SerializedName("crimeType")
    private String crimeType;

    /**
     * Constructs a NeighborhoodCrimeRankingResult with the specified details.
     *
     * @param neighborhood The name of the neighborhood.
     * @param ranking The ranking of the neighborhood based on crime data.
     * @param safetyLevel The safety level of the neighborhood.
     * @param crimeType The type of crime used for the ranking.
     */
    public NeighborhoodCrimeRankingResult(String neighborhood, int ranking, String safetyLevel, String crimeType) {
        this.neighborhood = neighborhood;
        this.ranking = ranking;
        this.safetyLevel = safetyLevel;
        this.crimeType = crimeType;
    }

    /**
     * Returns the name of the neighborhood.
     *
     * @return The neighborhood name.
     */
    public String getNeighborhood() {
        return neighborhood;
    }

    /**
     * Returns the ranking of the neighborhood.
     *
     * @return The neighborhood ranking.
     */
    public int getRanking() {
        return ranking;
    }

    /**
     * Returns the safety level of the neighborhood.
     *
     * @return The safety level.
     */
    public String getSafetyLevel() {
        return safetyLevel;
    }

    /**
     * Returns the type of crime used for the ranking.
     *
     * @return The crime type.
     */
    public String getCrimeType() {
        return crimeType;
    }

    /**
     * Returns a string representation of the NeighborhoodCrimeRankingResult.
     *
     * @return A string representation of the NeighborhoodCrimeRankingResult.
     */
    @Override
    public String toString() {
        return "NeighborhoodCrimeRankingResult{" +
                "neighborhood='" + neighborhood + '\'' +
                ", ranking=" + ranking +
                ", safetyLevel='" + safetyLevel + '\'' +
                ", crimeType='" + crimeType + '\'' +
                '}';
    }
}
