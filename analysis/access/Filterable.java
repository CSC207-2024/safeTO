package access;

import tech.tablesaw.api.Table;

/**
 * An interface for filtering table based on various criteria.
 */
public interface Filterable {
    Table filterBy(String columnName, String value);

    Table filterByRange(String columnName, int start, int end);
}
