package analysis.access;
import analysis.access.CrimeDataConverter;
import analysis.access.CrimeDataFetcher;
import analysis.access.CrimeDataProcessor;
import org.json.JSONArray;
import tech.tablesaw.api.Table;
import org.json.JSONException;



/**
 * A class for DataAccess Usage Demonstration.
 */

public class Usage {

    public static void main(String[] args) throws JSONException {

        CrimeDataFetcher dataFetcher = new CrimeDataFetcher();

        JSONArray data = dataFetcher.fetchData();
        CrimeDataConverter converter = new CrimeDataConverter();
        Table t = converter.jsonToTable(data);
        System.out.println(t.shape());
        CrimeDataProcessor processor = new CrimeDataProcessor();
        processor.setTable(t);
        String[] names = processor.getColumnNames();
        for (String n : names){
            System.out.println(n);
        }


        // CrimeDataProcessor
        Table byYear = processor.filterByRange("OCC_YEAR", 2024, 2024);
        System.out.println(byYear.first(5));

        Table byNeighbourhood = processor.filterBy("NEIGHBOURHOOD_158", "Guildwood");
        System.out.println(byNeighbourhood.first(5));

        Table byType = processor.filterBy("MCI_CATEGORY", "Assault");
        System.out.println(byType.first(5));

        Table byPremises = processor.filterBy("PREMISES_TYPE", "Apartment");
        System.out.println(byPremises.first(5));

        Table agg1 = processor.aggregate("MCI_CATEGORY", "OCC_YEAR");
        System.out.println(agg1.first(6));

//        byColumns... is a syntactic sugar for passing 0 or more arguments

        Table agg2 = processor.aggregate("MCI_CATEGORY", "OCC_YEAR","NEIGHBOURHOOD_158");
        System.out.println(agg2.first(5));

        Table agg3 = processor.aggregate("MCI_CATEGORY", "OCC_YEAR", "MCI_CATEGORY");
        System.out.println(agg3.first(5));

        Table agg4 = processor.aggregate("MCI_CATEGORY",
                "OCC_YEAR", "MCI_CATEGORY","NEIGHBOURHOOD_158");
        System.out.println(agg4.first(15));


        Table agg5 = processor.aggregate("MCI_CATEGORY", "MCI_CATEGORY");
        System.out.println(agg5.first(15));





    }

}
