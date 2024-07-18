package access.manipulate;

import tech.tablesaw.api.Table;

/**
 * An interface for aggregating information.
 */
public interface Aggregator {
    Table aggregate(String colName, String byFirst);

    Table aggregate(String colName, String... byColumns);
}
