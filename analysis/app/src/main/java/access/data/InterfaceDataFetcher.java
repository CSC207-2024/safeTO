package access.data;

import com.google.gson.JsonArray;



/**
 * An interface for fetching JSON format data from API.
 */
public interface InterfaceDataFetcher {

    JsonArray fetchData();
}
