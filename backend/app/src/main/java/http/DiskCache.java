package http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import types.CachedResponse;

public class DiskCache {
    private final Path cacheDir;
    private final Map<String, CacheMetadata> urlToMetadata;
    private final Gson gson;

    public DiskCache() throws IOException {
        this.cacheDir = Files.createTempDirectory("csc207-backend-http");
        this.urlToMetadata = new HashMap<>();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public CachedResponse get(String url) throws IOException {
        CacheMetadata metadata = urlToMetadata.get(url);
        if (metadata != null && System.currentTimeMillis() < metadata.expiryTime) {
            Path cachedFilePath = getCacheFilePath(metadata.hash);
            if (Files.exists(cachedFilePath)) {
                try (BufferedReader reader = new BufferedReader(new FileReader(cachedFilePath.toFile()))) {
                    return gson.fromJson(reader, CachedResponse.class);
                }
            }
        }
        return null;
    }

    public void put(String url, CachedResponse response, long ttl) throws IOException, NoSuchAlgorithmException {
        String serializedResponse = gson.toJson(response);
        String hash = computeSHA256(serializedResponse);

        Path cachedFilePath = getCacheFilePath(hash);
        Files.createDirectories(cachedFilePath.getParent());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(cachedFilePath.toFile()))) {
            writer.write(serializedResponse);
        }

        // Record both hash and expiry time for the URL
        urlToMetadata.put(url, new CacheMetadata(hash, System.currentTimeMillis() + ttl));
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
