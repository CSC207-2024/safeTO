package http;

import types.CachedResponse;

public interface CacheInterface {
    public void put(String url, CachedResponse response, long ttl);

    public CachedResponse get(String url);
}
