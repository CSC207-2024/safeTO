package access.manipulate;

import org.jfree.chart.axis.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.category.*;
import tech.tablesaw.api.*;
import tech.tablesaw.columns.Column;
import org.jfree.chart.*;
import org.jfree.data.category.*;
import java.awt.*;


/**
 * A class for data visualization.
 */
public class CrimeDataPlotter {

    /**
     * Validates that the given column names exist in the table.
     *
     * @param table The Table to check.
     * @param colname The column name to check.
     */
    private void validateColumns(Table table, String colname) {
        if (!table.columnNames().contains(colname)) {
            throw new IllegalArgumentException("Column " + colname + " does not exist in the table.");
    }
    }

    /**
     * Creates a bar chart from the given Table of data.
     *
     * @param table The Table containing the data.
     * @param xCol The column name to be used for categories.
     * @param yCol The column name to be used for values.
     * @param title The title of the chart.
     * @param xLabel The label for the X axis.
     * @param yLabel The label for the Y axis.
     * @return A JFreeChart object representing the bar chart.
     */
    public JFreeChart barPlot(Table table, String xCol, String yCol, String title, String xLabel, String yLabel) {
        validateColumns(table, xCol);
        validateColumns(table, yCol);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Column<?> xColumn = table.column(xCol);

        for (Row row : table) {
            ColumnType xType = xColumn.type();
            if (xType.equals(ColumnType.STRING)) {
                String category = row.getString(xCol);
                double value = row.getDouble(yCol);
                dataset.addValue(value, yCol, category);
            } else if (xType.equals(ColumnType.INTEGER)) {
                int category = row.getInt(xCol);
                double value = row.getDouble(yCol);
                dataset.addValue(value, yCol, Integer.toString(category));
            } else if (xType.equals(ColumnType.DOUBLE)) {
                double category = row.getDouble(xCol);
                double value = row.getDouble(yCol);
                dataset.addValue(value, yCol, Double.toString(category));
            } else {
                throw new IllegalArgumentException("Unsupported column type for x-axis: " + xColumn.type().name());
            }
        }

        JFreeChart chart = ChartFactory.createBarChart(
                title == null ? "NA" : title, // Chart title
                xLabel == null ? xCol : xLabel, // Domain axis label
                yLabel == null ? yCol : yLabel, // Range axis label
                dataset, // Data
                PlotOrientation.VERTICAL, // Orientation
                true, // Include legend
                true, // Tooltips
                false // URLs
        );

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setDomainAxis(new CategoryAxis(xLabel == null ? xCol : xLabel));
        plot.setRangeAxis(new NumberAxis(yLabel == null ? yCol : yLabel));
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinesVisible(true);

        // Customize the renderer
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);

        // Customize the domain axis
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

        // Customize the range axis
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        return chart;
    }


    /**
     * Creates a line chart from the given Table of data.
     *
     * @param table The Table containing the data.
     * @param xCol The column name to be used for X values.
     * @param yCol The column name to be used for Y values.
     * @param type The column name to group by, or null if no grouping is needed.
     * @param xLab The label for the X axis.
     * @param yLab The label for the Y axis.
     * @param title The title of the chart.
     * @return A JFreeChart object representing the line chart.
     */
    public JFreeChart linePlot(Table table, String xCol, String yCol, String type, String xLab, String yLab, String title) {

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        Column<?> xColumn = table.column(xCol);
        Column<?> yColumn = table.column(yCol);
        Column<?> typeColumn = table.column(type);

        for (int i = 0; i < table.rowCount(); i++) {
            String xValue;
            if (xColumn.type() == ColumnType.STRING) {
                xValue = xColumn.getString(i);
            } else if (xColumn.type() == ColumnType.INTEGER) {
                xValue = Integer.toString((int) xColumn.get(i));
            } else if (xColumn.type() == ColumnType.DOUBLE) {
                xValue = Double.toString((double) xColumn.get(i));
            } else {
                throw new IllegalArgumentException("Unsupported xCol type: " + xColumn.type());
            }

            Number yValue = (Number) yColumn.get(i);
            String typeValue = typeColumn.getString(i);
            dataset.addValue(yValue, typeValue, xValue);
        }

        JFreeChart lineChart = ChartFactory.createLineChart(
                title,
                xLab,
                yLab,
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        CategoryPlot plot = (CategoryPlot) lineChart.getPlot();
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();

        // Set line width for each series
        for (int i = 0; i < dataset.getRowCount(); i++) {
            renderer.setSeriesStroke(i, new BasicStroke(2.0f));
        }
        return lineChart;
    }



}
