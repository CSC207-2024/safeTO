package access;

import java.util.List;

public interface CrimeDataFetcherInterface<T> {
    List<T> fetchCrimeData();
}