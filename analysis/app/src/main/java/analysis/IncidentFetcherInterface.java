package analysis;

import java.util.List;

/**
 * An interface for fetching incident data.
 *
 * @param <T> The type of data to fetch.
 */
public interface IncidentFetcherInterface<T> {

    /**
     * Fetches a list of incident data.
     *
     * @return A list of fetched incident data of type T.
     */
    List<T> fetchCrimeData();
}
