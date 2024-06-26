package src.DataAccess;

import org.json.JSONArray;
import tech.tablesaw.api.*;
import tech.tablesaw.io.*;

import java.util.ArrayList;
import java.util.List;


/**
 * A class that is responsible for data wrangling, manipulation, and visualization.
 */
public class CrimeDataProcessor {

    private Table df;
    private final String yearColumnName = "OCC_YEAR";

    /**
     * A setter method to set data table for further processing.
     * @param df input data table
     */
    public void setTable(Table df) {
        this.df = df;
    }

    /**
     * A method for selecting subset of columns.
     * @param colNames List of column names to select from the table.
     * @return A subset of the table containing only the specified columns.
     */
    public Table selectColumn(ArrayList<String> colNames) {
        ArrayList<String> existingColumns = new ArrayList<>();
        for (String column : colNames) {
            if (df.columnNames().contains(column)) {
                existingColumns.add(column);
            } else {
                System.out.println("Column not found: " + column);
            }
        }
        return df.select(existingColumns.toArray(new String[0]));
    }

    /**
     * Filters the table by a specified year range.
     * @param startYear the starting year of the filter range.
     * @param endYear the ending year of the filter range.
     * @return A Table containing only the rows from the specified year range.
     */
    public Table filterByYear(int startYear, int endYear){
        if (startYear > endYear) {
            throw new IllegalArgumentException("Start year must be less than or equal to end year.");
        }
        int minYearInData = (int) df.numberColumn(yearColumnName).min();
        int maxYearInData = (int) df.numberColumn(yearColumnName).max();
        if (startYear < minYearInData || endYear > maxYearInData) {
            throw new IllegalArgumentException("Year range [" + startYear + ", " + endYear + "] is out of bounds. Available range is [" + minYearInData + ", " + maxYearInData + "].");
        }
        return df.where(
                df.numberColumn(yearColumnName).isGreaterThanOrEqualTo(startYear)
                        .and(df.numberColumn(yearColumnName).isLessThanOrEqualTo(endYear))
        );


    }






    public static void main(String[] args) {
        CrimeDataFetcher fetcher = new CrimeDataFetcher();
        JSONArray data = fetcher.fetchData();
        CrimeDataConverter converter = new CrimeDataConverter();
        Table t = converter.jsonToTable(data);
        System.out.println(t.shape());
        CrimeDataProcessor processor = new CrimeDataProcessor();
        processor.setTable(t);

        ArrayList<String> columns = new ArrayList<>();
        columns.add("MCI_CATEGORY");
        columns.add("OCC_YEAR");
        System.out.println(processor.selectColumn(columns));

        Table filtered = processor.filterByYear(2013, 2014 );
        System.out.println(filtered.first(10));

    }









}
