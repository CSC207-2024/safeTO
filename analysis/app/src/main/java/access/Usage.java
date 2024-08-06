package access;

import access.convert.CrimeDataConverter;
import access.data.CrimeDataFetcher;
import access.export.CrimeDataExporter;
import access.manipulate.CrimeDataProcessor;
import com.google.gson.JsonArray;
import tech.tablesaw.api.Table;

import java.util.ArrayList;


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
        processor.formatNeighbourhoodColumn(t, "NEIGHBOURHOOD_140");
        String[] names = processor.getColumnNames();
        for (String n : names){
            System.out.println(n);
        }
        ArrayList<String> nei_columns = new ArrayList<>();
        nei_columns.add("NEIGHBOURHOOD_140");
        nei_columns.add("HOOD_140");
        Table neighbourhood = processor.selectColumn(nei_columns);
        System.out.println(neighbourhood.first(10));


        // CrimeDataProcessor
        Table byYear5 = processor.filterByRange("OCC_YEAR", 2019, 2024);
        processor.setTable(byYear5);
        Table agg5year = processor.aggregate("MCI_CATEGORY", "NEIGHBOURHOOD_140","HOOD_140","MCI_CATEGORY");
        String total_5year = converter.tableToJson(agg5year);
        String year_5_category = converter.changeJsonKeys(total_5year);
        String path7 = "/Users/admin/Desktop/Github-Projects/safeTO/backend/app/src/main/resources/aggregates/five_year_total.json";


        Table agg1 = processor.aggregate("MCI_CATEGORY", "OCC_YEAR");
        String total_by_year = converter.tableToJson(agg1);
        String year_total = converter.changeJsonKeys(total_by_year);


        Table agg2 = processor.aggregate("MCI_CATEGORY", "OCC_YEAR","NEIGHBOURHOOD_140","HOOD_140");
        String by_y_n = converter.tableToJson(agg2);
        String year_neighbourhood = converter.changeJsonKeys(by_y_n);


        Table agg3 = processor.aggregate("MCI_CATEGORY", "OCC_YEAR", "MCI_CATEGORY");
        String by_y_c = converter.tableToJson(agg3);
        String year_category = converter.changeJsonKeys(by_y_c);

        Table agg4 = processor.aggregate("MCI_CATEGORY",
                "OCC_YEAR", "MCI_CATEGORY","NEIGHBOURHOOD_140", "HOOD_140");
        String by_y_c_n = converter.tableToJson(agg4);
        String year_category_neighbourhood = converter.changeJsonKeys(by_y_c_n);


        Table agg5 = processor.aggregate("MCI_CATEGORY", "MCI_CATEGORY");
        String total_c = converter.tableToJson(agg5);
        String total_category = converter.changeJsonKeys(total_c);

        Table agg6 = processor.aggregate("MCI_CATEGORY", "NEIGHBOURHOOD_140","HOOD_140");
        String total_by_n = converter.tableToJson(agg6);
        String total_neighbourhood = converter.changeJsonKeys(total_by_n);

//        Export the data to frontend/aggregates
        CrimeDataExporter exporter = new CrimeDataExporter();
        String path1 = "/Users/admin/Desktop/Github-Projects/safeTO/backend/app/src/main/resources/aggregates/total_by_year.json";
        String path2 = "/Users/admin/Desktop/Github-Projects/safeTO/backend/app/src/main/resources/aggregates/by_year_neighbourhood.json";
        String path3 = "/Users/admin/Desktop/Github-Projects/safeTO/backend/app/src/main/resources/aggregates/by_year_category.json";
        String path4 = "/Users/admin/Desktop/Github-Projects/safeTO/backend/app/src/main/resources/aggregates/by_year_category_neighbourhood.json";
        String path5 = "/Users/admin/Desktop/Github-Projects/safeTO/backend/app/src/main/resources/aggregates/total_by_category.json";
        String path6 = "/Users/admin/Desktop/Github-Projects/safeTO/backend/app/src/main/resources/aggregates/total_by_neighbourhood.json";
        exporter.writeToJson(year_total, path1);
        exporter.writeToJson(year_neighbourhood, path2);
        exporter.writeToJson(year_category, path3);
        exporter.writeToJson(year_category_neighbourhood, path4);
        exporter.writeToJson(total_category, path5);
        exporter.writeToJson(total_neighbourhood, path6);
        exporter.writeToJson(year_5_category, path7);

//        access.manipulate.CrimeDataPlotter plotter = new access.manipulate.CrimeDataPlotter();
//        JFreeChart barplot1 = plotter.barPlot(agg1, "OCC_YEAR", "Count [MCI_CATEGORY]", "Total Crime by Year", "Year", "Count");
//        exporter.exportToSVG(barplot1, "/Users/admin/Desktop/Github-Projects/safeTO/frontend/aggregates/total_by_year.svg");
//        JFreeChart lineplot1 = plotter.linePlot(agg3, "OCC_YEAR", "Count [MCI_CATEGORY]","MCI_CATEGORY",
//                "Total Crime by Year and Category", "Year", "Count");
//        exporter.exportToSVG(lineplot1, "/Users/admin/Desktop/Github-Projects/safeTO/frontend/aggregates/by_year_category.svg");





    }

}
