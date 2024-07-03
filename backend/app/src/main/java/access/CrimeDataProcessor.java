package access;

import tech.tablesaw.api.*;
import java.util.ArrayList;

import static tech.tablesaw.aggregate.AggregateFunctions.count;

/**
 * A class that is responsible for data wrangling, manipulation, and
 * visualization.
 */
public class CrimeDataProcessor implements access.Filterable, access.Aggregator {

    private Table df;

    /**
     * A setter method to set data table for further processing.
     * 
     * @param df input data table
     */
    public void setTable(Table df) {
        this.df = df;
    }

    /**
     * A getter method to retrieve column names.
     * 
     * @return an array of String containing column names in this dataset
     */
    public String[] getColumnNames() {
        return df.columnNames().toArray(new String[0]);
    }

    /**
     * A method for selecting subset of columns.
     * 
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
     * @param columnName the colum we want to filter on.
     * @param startYear  the starting year of the filter range.
     * @param endYear    the ending year of the filter range.
     * @return A Table containing only rows from the specified year range.
     * @throws IllegalArgumentException if the columnName or the value is not valid.
     */
    @Override
    public Table filterByRange(String columnName, int startYear, int endYear) {
        checkValidColumn(columnName);
        if (startYear > endYear) {
            throw new IllegalArgumentException("Start year must be less than or equal to end year.");
        }

        int minYearInData = (int) df.numberColumn(columnName).min();
        int maxYearInData = (int) df.numberColumn(columnName).max();
        if (startYear < minYearInData || endYear > maxYearInData) {
            throw new IllegalArgumentException("Year range [" + startYear + ", " + endYear
                    + "] is out of bounds. Available range is [" + minYearInData + ", " + maxYearInData + "].");
        }
        return df.where(
                df.numberColumn(columnName).isGreaterThanOrEqualTo(startYear)
                        .and(df.numberColumn(columnName).isLessThanOrEqualTo(endYear)));
    }

    /**
     * Filters the data table based on a specified value within a specified column.
     * 
     * @param columnName columnName the name of the column to filter by.
     *                   This column should exist in the data table and should be of
     *                   type String.
     * @param value      the value to filter by.
     * @return A table containing only the rows where the specified column matches
     *         the provided value.
     * @throws IllegalArgumentException if the columnName or the value is not valid.
     */
    @Override
    public Table filterBy(String columnName, String value) {
        checkValidColumn(columnName);
        String[] uniqueValues = getUniqueValues(columnName);
        for (String uniqueValue : uniqueValues) {
            if (value.equalsIgnoreCase(uniqueValue)) {
                return df.where(df.stringColumn(columnName).equalsIgnoreCase(value));
            }
        }
        throw new IllegalArgumentException("Invalid value provided for " + columnName + ": " + value);
    }

    /**
     * A helper method for checking the validity of input column name.
     * 
     * @param columnName the column names to be checked.
     * @throws IllegalArgumentException if columnName is not presented in the
     *                                  datatable.
     */

    private void checkValidColumn(String columnName) throws IllegalArgumentException {
        String[] names = getColumnNames();
        boolean isValidColumn = false;
        for (String name : names) {
            if (columnName.equals(name)) {
                isValidColumn = true;
                break;
            }
        }
        if (!isValidColumn)
            throw new IllegalArgumentException("Column not found: " + columnName);
    }

    /**
     * A private helper fetches unique values from a specified column
     * 
     * @param columnName the column name
     * @return an array of String containing unique value from the column.
     */

    private String[] getUniqueValues(String columnName) {
        CategoricalColumn<?> categoryColumn = df.categoricalColumn(columnName);
        return categoryColumn.unique().asList().stream()
                .map(Object::toString)
                .toArray(String[]::new);
    }

    /**
     * Aggregates the specified column of the table by summing up its values,
     * grouped by another specified column.
     * 
     * @param colName The name of the column to be aggregated.
     * @param byFirst The name of the column by which the results should be grouped.
     * @return A table containing the summarized data.
     * @throws IllegalArgumentException if columnName is not presented in the
     *                                  datatable.
     */
    @Override
    public Table aggregate(String colName, String byFirst) {
        checkValidColumn(colName);
        checkValidColumn(byFirst);

        return df.summarize(colName, count).by(byFirst);
    }

    /**
     * Aggregates the specified column of the table by summing up its values,
     * grouped by another specified column.
     * 
     * @param colName   The name of the column to be aggregated.
     * @param byColumns A list of column names by which the results should be
     *                  grouped.
     * @return A table containing the summarized data.
     * @throws IllegalArgumentException if columnName is not presented in the
     *                                  datatable.
     */
    @Override
    public Table aggregate(String colName, String... byColumns) {
        checkValidColumn(colName);
        for (String col : byColumns) {
            checkValidColumn(col);
        }

        return df.summarize(colName, count).by(byColumns);
    }

}
