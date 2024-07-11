package access;

import tech.tablesaw.api.Table;
import tech.tablesaw.aggregate.AggregateFunctions;
import access.CrimeDataProcessor;

public class CrimeDataRanker {
    private final CrimeDataProcessor processor;

    public CrimeDataRanker(CrimeDataProcessor processor) {
        this.processor = processor;
    }

    public Table rankNeighborhoodsByTotalCrime() {
        return processor.aggregate("MCI_CATEGORY", "NEIGHBOURHOOD_158")
                .sortDescendingOn("Count [MCI_CATEGORY]");
    }

    public Table rankNeighborhoodsBySpecificCrime(String crimeType) {
        Table filteredTable = processor.filterBy("MCI_CATEGORY", crimeType);
        CrimeDataProcessor filteredProcessor = new CrimeDataProcessor();
        filteredProcessor.setTable(filteredTable);
        return filteredProcessor.aggregate("MCI_CATEGORY", "NEIGHBOURHOOD_158")
                .sortDescendingOn("Count [MCI_CATEGORY]");
    }

    public int getNeighborhoodRanking(String neighborhood) {
        Table rankingTable = rankNeighborhoodsByTotalCrime();
        return getRanking(rankingTable, neighborhood);
    }

    public int getSpecificCrimeNeighborhoodRanking(String crimeType, String neighborhood) {
        Table rankingTable = rankNeighborhoodsBySpecificCrime(crimeType);
        return getRanking(rankingTable, neighborhood);
    }

    private int getRanking(Table rankingTable, String neighborhood) {
        for (int i = 0; i < rankingTable.rowCount(); i++) {
            if (rankingTable.stringColumn("NEIGHBOURHOOD_158").get(i).equalsIgnoreCase(neighborhood)) {
                return i + 1; // Ranking starts from 1
            }
        }
        return -1; // Not found
    }
}
