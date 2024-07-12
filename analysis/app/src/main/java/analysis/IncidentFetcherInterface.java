package analysis;

import java.util.List;

public interface IncidentFetcherInterface<T> {
    List<T> fetchCrimeData();
}