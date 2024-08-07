package access.data;

import java.io.File;

public class CacheDirectory {
    // Static block to initialize the CACHE_DIR during class loading
    private static final String CACHE_DIR;

    static {
        CACHE_DIR = determineCacheDir();
    }

    private static String determineCacheDir() {
        // Check if environment variable is set
        String cacheDir = System.getenv("SAFETO_ANALYSIS_CACHE_DIR");
        if (cacheDir != null && !cacheDir.isEmpty()) {
            return cacheDir;
        }

        // Check the system temp directory
        String tempDir = System.getProperty("java.io.tmpdir");
        if (tempDir != null && !tempDir.isEmpty()) {
            File customTempDir = new File(tempDir, ".safeTO-cache");
            if (customTempDir.exists() || customTempDir.mkdirs()) {
                return customTempDir.getAbsolutePath();
            }
        }

        // Fallback to default directory
        return "../cache/";
    }

    public static String getCacheDir() {
        return CACHE_DIR;
    }

    public static void main(String[] args) {
        System.out.println("Cache Directory: " + CacheDirectory.getCacheDir());
    }
}
