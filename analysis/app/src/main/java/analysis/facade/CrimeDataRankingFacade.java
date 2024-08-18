package analysis.facade;

import access.data.InterfaceDataFetcher;
import access.convert.CrimeDataConverter;
import access.manipulate.CrimeDataProcessor;
import analysis.crimeDataRanking.*;
import tech.tablesaw.api.Table;

/**
 * A facade class that provides a simplified interface to the crime data ranking system.
 */
public class CrimeDataRankingFacade {
    private final InterfaceDataFetcher fetcher;
    private final CrimeDataConverter converter;
    private final CrimeDataProcessor processor;

    /**
     * Constructs a CrimeDataRankingFacade with the specified data fetcher, converter,
     * and processor.
     *
     * @param dataFetcher The data fetcher for retrieving crime data.
     * @param converter The converter for transforming data formats.
     * @param processor The processor for manipulating crime data.
     */
    public CrimeDataRankingFacade(InterfaceDataFetcher dataFetcher, CrimeDataConverter converter, CrimeDataProcessor processor) {
        this.fetcher = dataFetcher;
        this.converter = converter;
        this.processor = processor;
        fetchAndConvertData();
    }

    /**
     * Fetches and converts crime data.
     */
    private void fetchAndConvertData() {
        Table table = converter.jsonToTable(fetcher.fetchData());
        processor.setTable(table);
        processor.formatNeighbourhoodColumn(processor.getTable(), "NEIGHBOURHOOD_140");
    }

    /**
     * Retrieves the ranking of a neighborhood based on total or specific crime.
     *
     * @param neighborhood The name of the neighborhood.
     * @param specificCrime The specific crime type to consider (or null for total crime).
     * @return A NeighborhoodCrimeRankingResult containing the ranking and safety level.
     */
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
