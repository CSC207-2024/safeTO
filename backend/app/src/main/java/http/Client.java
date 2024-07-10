package http;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import java.net.http.HttpRequest;

public class Client {
    private static final HttpClient client = HttpClient.newHttpClient();

    public <T> HttpResponse<T> send(HttpRequest request, HttpResponse.BodyHandler<T> responseBodyHandler,
            int max_retries) {
        int backoff_factor = 1;
        for (int tries = 0; tries < max_retries; ++tries) {
            try {
                return client.send(request, responseBodyHandler);
            } catch (Exception e) {
                // log error
            }
            backoff_factor *= 2;
            if (backoff_factor > 16) {
                backoff_factor = 16;
            }
        }
        return null;
    }
}
