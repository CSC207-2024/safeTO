package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import gson.GsonSingleton;
import logging.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import types.CachedResponse;

public class DiskCache implements CacheInterface {
    private final Path cacheDir;
    private final Map<String, CacheMetadata> urlToMetadata;
    private static final Gson gson = GsonSingleton.getInstance();

    private static final String componentName = "backend.http.DiskCache";

    public DiskCache() {
        this.cacheDir = ((Supplier<Path>) () -> {
            try {
                return Files.createTempDirectory("csc207-backend-http");
            } catch (Throwable e) {
                Logger.error(e.getMessage(), componentName);
                return null;
            }
        }).get();
        this.urlToMetadata = new HashMap<>();
    }

    public CachedResponse get(String url) {
        if (this.cacheDir == null) {
            return null;
        }
        CacheMetadata metadata = urlToMetadata.get(url);
        if (metadata != null && System.currentTimeMillis() < metadata.expiryTime) {
            Path cachedFilePath = getCacheFilePath(metadata.hash);
            if (Files.exists(cachedFilePath)) {
                try (BufferedReader reader = new BufferedReader(new FileReader(cachedFilePath.toFile()))) {
                    return gson.fromJson(reader, CachedResponse.class);
                } catch (Throwable e) {
                    Logger.error(e.getMessage(), componentName);
                    return null;
                }
            }
        }
        return null;
    }

    public void put(String url, CachedResponse response, long ttl) {
        if (cacheDir == null) {
            return;
        }
        try {
            String serializedResponse = gson.toJson(response);
            Logger.log("put/serializedResponse: length = " + serializedResponse.length(), componentName);
            String hash = computeSHA256(serializedResponse);
            Logger.log("put/computeSHA256: result = " + hash, componentName);

            Path cachedFilePath = getCacheFilePath(hash);
            Files.createDirectories(cachedFilePath.getParent());
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(cachedFilePath.toFile()))) {
                writer.write(serializedResponse);
            }

            // Record both hash and expiry time for the URL
            urlToMetadata.put(url, new CacheMetadata(hash, System.currentTimeMillis() + ttl));
        } catch (Throwable e) {
            Logger.error(e.getMessage(), componentName);
            return;
        }
    }

    private String computeSHA256(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(input.getBytes());
        StringBuilder hashString = new StringBuilder();
        for (byte b : hashBytes) {
            hashString.append(String.format("%02x", b));
        }
        return hashString.toString();
    }

    private Path getCacheFilePath(String hash) {
        String dir1 = hash.substring(0, 2);
        String dir2 = hash.substring(2, 4);
        String restOfHash = hash.substring(4);
        return cacheDir.resolve(Paths.get("cache", dir1, dir2, restOfHash + ".json"));
    }

    private static class CacheMetadata {
        String hash;
        long expiryTime;

        CacheMetadata(String hash, long expiryTime) {
            this.hash = hash;
            this.expiryTime = expiryTime;
        }
    }
}
