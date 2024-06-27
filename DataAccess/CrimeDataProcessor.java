package DataAccess;

import org.json.JSONArray;
import tech.tablesaw.api.*;
import tech.tablesaw.io.*;

import java.util.ArrayList;



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
     * @return A Table containing only rows from the specified year range.
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

    /**
     * private helper method to fetch unique crime types from MCI_CATEGORY.
     * @return An array of String containing unique crime types.
     */

    private String[] getUniqueCrimeTypes() {
        CategoricalColumn<?> categoryColumn = df.categoricalColumn("MCI_CATEGORY");
        return categoryColumn.unique().asList().stream()
                .map(Object::toString)
                .toArray(String[]::new);
    }

    /**
     * private helper method to fetch unique neighbourhood names.
     * @return An array of String containing unique neighbourhood names.
     */

    private String[] getUniqueNeighbourhoods() {
        CategoricalColumn<?> categoryColumn = df.categoricalColumn("NEIGHBOURHOOD_158");
        return categoryColumn.unique().asList().stream()
                .map(Object::toString)
                .toArray(String[]::new);
    }


    /**
     * Filter the table by a specific crime type.
     * @param crimeType A string represent the crime type from MCI_CATEGORY
     * @return A table containing only the rows that match the specified crime type.
     */
    public Table filterByType(String crimeType){
        String[] types = getUniqueCrimeTypes();
        boolean isValidType = false;
        for (String type : types) {
            if (crimeType.equalsIgnoreCase(type)){
                isValidType = true;
                break;
            }
        }
        if (!isValidType) {
            throw new IllegalArgumentException("Invalid crime type provided: " + crimeType);
        }
        Table filteredData = df.where(df.stringColumn("MCI_CATEGORY").equalsIgnoreCase(crimeType));
        return filteredData;


    }


    /**
     * Filter the table by a specific neighbourhood.
     * @param neighbourhood A string represent the crime type from MCI_CATEGORY
     * @return A table containing only the rows that match the specified crime type.
     */
    public Table filterByNeighbourhood(String neighbourhood){
        String[] neighbourhoods = getUniqueNeighbourhoods();
        boolean isValidHood = false;
        for (String n : neighbourhoods) {
            if (neighbourhood.equalsIgnoreCase(n)){
                isValidHood = true;
                break;
            }
        }
        if (!isValidHood) {
            throw new IllegalArgumentException("Invalid neighbourhood provided: " + neighbourhood);
        }
        Table filteredData = df.where(df.stringColumn("NEIGHBOURHOOD_158").equalsIgnoreCase(neighbourhood));
        return filteredData;


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

//        Table filtered = processor.filterByYear(2024, 2024);
//        System.out.println(filtered.first(10));


        String[] types = processor.getUniqueCrimeTypes();
        for (String type : types) {
            System.out.println(type);
        }
        String[] neighbourhood = processor.getUniqueNeighbourhoods();
        for (String n : neighbourhood) {
            System.out.println(n);
        }
        Table filteredData = processor.filterByType(types[0]);
        System.out.println(filteredData.first(3));

    }









}
