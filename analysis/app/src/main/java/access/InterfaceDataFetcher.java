package analysis.access;

import org.json.JSONArray;


/**
 * An interface for fetching JSON format data from API.
 */
public interface InterfaceDataFetcher {

    JSONArray fetchData();
}
