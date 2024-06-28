package DataAccess;
import org.json.JSONArray;
import tech.tablesaw.api.Table;

/**
 * A class for DataAccess Usage Demonstration.
 */

public class Usage {

    public static void main(String[] args) {
        CrimeDataFetcher fetcher = new CrimeDataFetcher();
        JSONArray data = fetcher.fetchData();
        CrimeDataConverter converter = new CrimeDataConverter();
        Table t = converter.jsonToTable(data);
        System.out.println(t.shape());
        CrimeDataProcessor processor = new CrimeDataProcessor();
        processor.setTable(t);

        // CrimeDataProcessor
        Table byYear = processor.filterByRange("OCC_YEAR", 2019, 2019);
        System.out.println(byYear.first(5));

        Table byNeighbourhood = processor.filterBy("NEIGHBOURHOOD_158", "Guildwood");
        System.out.println(byNeighbourhood.first(5));

        Table byType = processor.filterBy("MCI_CATEGORY", "Assault");
        System.out.println(byType.first(5));

        Table byPremises = processor.filterBy("PREMISES_TYPE", "Apartment");
        System.out.println(byPremises.first(5));





    }

}
