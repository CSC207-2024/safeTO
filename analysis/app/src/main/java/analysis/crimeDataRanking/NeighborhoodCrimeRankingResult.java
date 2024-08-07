package analysis.crimeDataRanking;

import com.google.gson.annotations.SerializedName;

public class NeighborhoodCrimeRankingResult {
    @SerializedName("neighborhood")
    private String neighborhood;

    @SerializedName("ranking")
    private int ranking;

    @SerializedName("safetyLevel")
    private String safetyLevel;

    @SerializedName("crimeType")
    private String crimeType;

    public NeighborhoodCrimeRankingResult(String neighborhood, int ranking, String safetyLevel, String crimeType) {
        this.neighborhood = neighborhood;
        this.ranking = ranking;
        this.safetyLevel = safetyLevel;
        this.crimeType = crimeType;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public int getRanking() {
        return ranking;
    }

    public String getSafetyLevel() {
        return safetyLevel;
    }

    public String getCrimeType() {
        return crimeType;
    }

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
