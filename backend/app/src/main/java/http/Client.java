package http;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import types.CachedResponse;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;

import logging.Logger;

public class Client {
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final CacheInterface cache = new DiskCache();

    public static <T> HttpResponse<T> send(HttpRequest request, HttpResponse.BodyHandler<T> responseBodyHandler,
            int max_retries) {
        int backoff_factor = 1;
        for (int tries = 0; tries < max_retries; ++tries) {
            try {
                return client.send(request, responseBodyHandler);
            } catch (Exception e) {
                Logger.warn(e.getMessage(), "backend.http.Client");
            }
            try {
                Thread.sleep(backoff_factor * 1000);
            } catch (Exception e) {
            }
            backoff_factor *= 2;
            if (backoff_factor > 16) {
                backoff_factor = 16;
            }
        }
        Logger.error("max_retries exceeded", "/backend/http/Client");
        return null;
    }

    public static <T> HttpResponse<T> send(HttpRequest request, HttpResponse.BodyHandler<T> responseBodyHandler) {
        return send(request, responseBodyHandler, 1);
    }

    public static HttpResponse<String> send(HttpRequest request) {
        return send(request, BodyHandlers.ofString());
    }

    public static HttpResponse<String> send(HttpRequest request, int max_retries) {
        return send(request, BodyHandlers.ofString(), max_retries);
    }

    // public static HttpResponse<String> send(HttpRequest request,
    // int max_retries, int cache_ttl) {
    // if (request.method() != "GET" && request.method() != "HEAD") {
    // // non-cacheable request, fall back to normal send
    // return send(request, max_retries);
    // }

    // CachedResponse cachedResponse = cache.get(request.uri().toString());
    // if (cachedResponse != null) {
    // return new Httprespon
    // }

    // HttpResponse<String> response = send(request, max_retries);
    // if (response != null && response.statusCode() >= 200 && response.statusCode()
    // < 300) {

    // }
    // }
}
