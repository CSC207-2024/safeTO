package analysis.facade;

import access.data.CrimeDataFetcher;
import access.convert.CrimeDataConverter;
import access.manipulate.CrimeDataProcessor;
import analysis.crimeDataRanking.*;
import tech.tablesaw.api.Table;

/**
 * A facade class that provides a simplified interface to the crime data ranking system.
 */
public class CrimeDataRankingFacade {
    private final CrimeDataFetcher fetcher;
    private final CrimeDataConverter converter;
    private final CrimeDataProcessor processor;

    public CrimeDataRankingFacade() {
        this.fetcher = new CrimeDataFetcher();
        this.converter = new CrimeDataConverter();
        this.processor = new CrimeDataProcessor();
        fetchAndConvertData();
    }

    private void fetchAndConvertData() {
        Table table = converter.jsonToTable(fetcher.fetchData());
        processor.setTable(table);
        processor.formatNeighbourhoodColumn(processor.getTable(), "NEIGHBOURHOOD_140");
    }


    public NeighborhoodCrimeRankingResult getNeighborhoodRanking(String neighborhood, String specificCrime) {
        CrimeDataRanker ranker = new CrimeDataRanker(processor);
        int totalNeighborhoods = processor.getTable().stringColumn("NEIGHBOURHOOD_140").unique().size();

        if (specificCrime == null || specificCrime.isEmpty()) {
            int ranking = ranker.getNeighborhoodRanking(neighborhood);
            String safetyLevel = ranker.getSafetyLevel(ranking, totalNeighborhoods);
            return new NeighborhoodCrimeRankingResult(neighborhood, ranking, safetyLevel, "Total Crime");
        } else {
            int ranking = ranker.getSpecificCrimeNeighborhoodRanking(specificCrime, neighborhood);
            String safetyLevel = ranker.getSafetyLevel(ranking, totalNeighborhoods);
            return new NeighborhoodCrimeRankingResult(neighborhood, ranking, safetyLevel, specificCrime);
        }
    }
}
