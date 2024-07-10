package backend.app.src.main.java.dataAccess;

import java.util.List;

public interface CrimeDataFetcherInterface<T> {
    List<T> fetchCrimeData();
}