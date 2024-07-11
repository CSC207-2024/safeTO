package access;

import org.jfree.chart.JFreeChart;
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

        Table agg2 = processor.aggregate("MCI_CATEGORY", "OCC_YEAR","NEIGHBOURHOOD_158");
        System.out.println(agg2.first(5));

        Table agg3 = processor.aggregate("MCI_CATEGORY", "OCC_YEAR", "MCI_CATEGORY");
        System.out.println(agg3.first(5));

        Table agg4 = processor.aggregate("MCI_CATEGORY",
                "OCC_YEAR", "MCI_CATEGORY","NEIGHBOURHOOD_158");
        System.out.println(agg4.first(15));


        Table agg5 = processor.aggregate("MCI_CATEGORY", "MCI_CATEGORY");
        System.out.println(agg5.first(15));

        Table agg6 = processor.aggregate("MCI_CATEGORY", "NEIGHBOURHOOD_158");
        System.out.println(agg6.first(15));

//        Export the data to frontend/aggregates
        CrimeDataExporter exporter = new CrimeDataExporter();
//        String path1 = "/Users/admin/Desktop/Github-Projects/safeTO/frontend/aggregates/total_by_year.json";
//        String path2 = "/Users/admin/Desktop/Github-Projects/safeTO/frontend/aggregates/by_year_neighbourhood.json";
//        String path3 = "/Users/admin/Desktop/Github-Projects/safeTO/frontend/aggregates/by_year_category.json";
//        String path4 = "/Users/admin/Desktop/Github-Projects/safeTO/frontend/aggregates/by_year_category_neighbourhood.json";
//        String path5 = "/Users/admin/Desktop/Github-Projects/safeTO/frontend/aggregates/total_by_category.json";
//        String path6 = "/Users/admin/Desktop/Github-Projects/safeTO/frontend/aggregates/total_by_neighbourhood.json";
//        exporter.writeToJson(agg1, path1);
//        exporter.writeToJson(agg2, path2);
//        exporter.writeToJson(agg3, path3);
//        exporter.writeToJson(agg4, path4);
//        exporter.writeToJson(agg5, path5);
//        exporter.writeToJson(agg6, path6);

        CrimeDataPlotter plotter = new CrimeDataPlotter();
//        JFreeChart barplot1 = plotter.barPlot(agg1, "OCC_YEAR", "Count [MCI_CATEGORY]", "Total Crime by Year", "Year", "Count");
//        exporter.exportToSVG(barplot1, "/Users/admin/Desktop/Github-Projects/safeTO/frontend/aggregates/total_by_year.svg");
//        JFreeChart lineplot1 = plotter.linePlot(agg3, "OCC_YEAR", "Count [MCI_CATEGORY]","MCI_CATEGORY",
//                "Total Crime by Year and Category", "Year", "Count");
//        exporter.exportToSVG(lineplot1, "/Users/admin/Desktop/Github-Projects/safeTO/frontend/aggregates/by_year_category.svg");





    }

}
