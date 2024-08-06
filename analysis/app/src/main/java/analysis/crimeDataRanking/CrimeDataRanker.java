package analysis.crimeDataRanking;

import tech.tablesaw.api.Table;
import access.manipulate.CrimeDataProcessor;

/**
 * A class for ranking neighborhoods based on crime data.
 */
public class CrimeDataRanker {
    private final CrimeDataProcessor processor;

    /**
     * Constructs a CrimeDataRanker with the specified data processor.
     *
     * @param processor The data processor for processing crime data.
     */
    public CrimeDataRanker(CrimeDataProcessor processor) {
        this.processor = processor;
    }

    /**
     * Ranks neighborhoods by the total number of crimes.
     *
     * @return A table with neighborhoods ranked by total crimes.
     */
    public Table rankNeighborhoodsByTotalCrime() {
        return processor.aggregate("MCI_CATEGORY", "NEIGHBOURHOOD_140")
                .sortDescendingOn("Count [MCI_CATEGORY]");
    }

    /**
     * Ranks neighborhoods by a specific type of crime.
     *
     * @param crimeType The type of crime to filter by.
     * @return A table with neighborhoods ranked by the specified crime type.
     */
    public Table rankNeighborhoodsBySpecificCrime(String crimeType) {
        Table filteredTable = processor.filterBy("MCI_CATEGORY", crimeType);
        CrimeDataProcessor filteredProcessor = new CrimeDataProcessor();
        filteredProcessor.setTable(filteredTable);
        return filteredProcessor.aggregate("MCI_CATEGORY", "NEIGHBOURHOOD_140")
                .sortDescendingOn("Count [MCI_CATEGORY]");
    }

    /**
     * Gets the ranking of a specific neighborhood by total crime.
     *
     * @param neighborhood The name of the neighborhood.
     * @return The ranking of the neighborhood, or -1 if not found.
     */
    public int getNeighborhoodRanking(String neighborhood) {
        Table rankingTable = rankNeighborhoodsByTotalCrime();
        return getRanking(rankingTable, neighborhood);
    }

    /**
     * Gets the ranking of a specific neighborhood by a specific type of crime.
     *
     * @param crimeType   The type of crime to filter by.
     * @param neighborhood The name of the neighborhood.
     * @return The ranking of the neighborhood, or -1 if not found.
     */
    public int getSpecificCrimeNeighborhoodRanking(String crimeType, String neighborhood) {
        Table rankingTable = rankNeighborhoodsBySpecificCrime(crimeType);
        return getRanking(rankingTable, neighborhood);
    }

    /**
     * Gets the ranking of a neighborhood from a ranking table.
     *
     * @param rankingTable The table with neighborhood rankings.
     * @param neighborhood The name of the neighborhood.
     * @return The ranking of the neighborhood, or -1 if not found.
     */
    private int getRanking(Table rankingTable, String neighborhood) {
        for (int i = 0; i < rankingTable.rowCount(); i++) {
            if (rankingTable.stringColumn("NEIGHBOURHOOD_140").get(i).equalsIgnoreCase(neighborhood)) {
                return i + 1; // Ranking starts from 1
            }
        }
        return -1; // Not found
    }

    /**
     * Determines the safety level based on the ranking.
     *
     * @param ranking The ranking of the neighborhood.
     * @param totalNeighborhoods The total number of neighborhoods.
     * @return The safety level as a string.
     */
    public String getSafetyLevel(int ranking, int totalNeighborhoods) {
        double percentile = (double) ranking / totalNeighborhoods;
        if (percentile <= 0.25) {
            return "Very Dangerous";
        } else if (percentile <= 0.50) {
            return "Moderately Dangerous";
        } else if (percentile <= 0.75) {
            return "Moderately Safe";
        } else {
            return "Very Safe";
        }
    }
}
