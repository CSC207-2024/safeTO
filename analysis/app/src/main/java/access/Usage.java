package access;

import access.convert.CrimeDataConverter;
import access.data.CrimeDataFetcher;
import access.export.CrimeDataExporter;
import access.manipulate.CrimeDataProcessor;
import com.google.gson.JsonArray;
import tech.tablesaw.api.Table;




/**
 * A class for DataAccess Usage Demonstration.
 */

public class Usage {

    public static void main(String[] args)  {

        CrimeDataFetcher dataFetcher = new CrimeDataFetcher();

        JsonArray data = dataFetcher.fetchData();
        CrimeDataConverter converter = new CrimeDataConverter();
        Table t = converter.jsonToTable(data);
        System.out.println(t.shape());
        CrimeDataProcessor processor = new CrimeDataProcessor();
        processor.setTable(t);
        Table autoTheft = processor.filterBy("MCI_CATEGORY", "Auto Theft");
        System.out.println(autoTheft.first(10));
        String[] names = processor.getColumnNames();
        for (String n : names){
            System.out.println(n);
        }


        // CrimeDataProcessor
        Table byYear = processor.filterByRange("OCC_YEAR", 2024, 2024);
        Table byNeighbourhood = processor.filterBy("NEIGHBOURHOOD_158", "Guildwood");
        System.out.println(byNeighbourhood.first(5));

        Table byType = processor.filterBy("MCI_CATEGORY", "Assault");
        System.out.println(byType.first(5));

        Table byPremises = processor.filterBy("PREMISES_TYPE", "Apartment");
        System.out.println(byPremises.first(5));

        Table agg1 = processor.aggregate("MCI_CATEGORY", "OCC_YEAR");
        String total_by_year = converter.tableToJson(agg1);
        String year_total = converter.changeJsonKeys(total_by_year);


        Table agg2 = processor.aggregate("MCI_CATEGORY", "OCC_YEAR","NEIGHBOURHOOD_158");
        String by_y_n = converter.tableToJson(agg2);
        String year_neighbourhood = converter.changeJsonKeys(by_y_n);


        Table agg3 = processor.aggregate("MCI_CATEGORY", "OCC_YEAR", "MCI_CATEGORY");
        String by_y_c = converter.tableToJson(agg3);
        String year_category = converter.changeJsonKeys(by_y_c);

        Table agg4 = processor.aggregate("MCI_CATEGORY",
                "OCC_YEAR", "MCI_CATEGORY","NEIGHBOURHOOD_158");
        String by_y_c_n = converter.tableToJson(agg4);
        String year_category_neighbourhood = converter.changeJsonKeys(by_y_c_n);


        Table agg5 = processor.aggregate("MCI_CATEGORY", "MCI_CATEGORY");
        String total_c = converter.tableToJson(agg5);
        String total_category = converter.changeJsonKeys(total_c);

        Table agg6 = processor.aggregate("MCI_CATEGORY", "NEIGHBOURHOOD_158");
        String total_by_n = converter.tableToJson(agg6);
        String total_neighbourhood = converter.changeJsonKeys(total_by_n);

//        Export the data to frontend/aggregates
        CrimeDataExporter exporter = new CrimeDataExporter();
//        String path1 = "/Users/admin/Desktop/Github-Projects/safeTO/frontend/aggregates/total_by_year.json";
//        String path2 = "/Users/admin/Desktop/Github-Projects/safeTO/frontend/aggregates/by_year_neighbourhood.json";
//        String path3 = "/Users/admin/Desktop/Github-Projects/safeTO/frontend/aggregates/by_year_category.json";
//        String path4 = "/Users/admin/Desktop/Github-Projects/safeTO/frontend/aggregates/by_year_category_neighbourhood.json";
//        String path5 = "/Users/admin/Desktop/Github-Projects/safeTO/frontend/aggregates/total_by_category.json";
//        String path6 = "/Users/admin/Desktop/Github-Projects/safeTO/frontend/aggregates/total_by_neighbourhood.json";
//        exporter.writeToJson(year_total, path1);
//        exporter.writeToJson(year_neighbourhood, path2);
//        exporter.writeToJson(year_category, path3);
//        exporter.writeToJson(year_category_neighbourhood, path4);
//        exporter.writeToJson(total_category, path5);
//        exporter.writeToJson(total_neighbourhood, path6);

//        access.manipulate.CrimeDataPlotter plotter = new access.manipulate.CrimeDataPlotter();
//        JFreeChart barplot1 = plotter.barPlot(agg1, "OCC_YEAR", "Count [MCI_CATEGORY]", "Total Crime by Year", "Year", "Count");
//        exporter.exportToSVG(barplot1, "/Users/admin/Desktop/Github-Projects/safeTO/frontend/aggregates/total_by_year.svg");
//        JFreeChart lineplot1 = plotter.linePlot(agg3, "OCC_YEAR", "Count [MCI_CATEGORY]","MCI_CATEGORY",
//                "Total Crime by Year and Category", "Year", "Count");
//        exporter.exportToSVG(lineplot1, "/Users/admin/Desktop/Github-Projects/safeTO/frontend/aggregates/by_year_category.svg");





    }

}
