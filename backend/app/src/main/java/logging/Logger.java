package logging;

// Singleton Design Pattern: <https://bit.ly/3Ygkmdl>
public class Logger {
    private static final String DEFAULT_COMPONENT = "/backend/logging/Logger";

    public static void log(String message, String component) {

    }

    public static void log(String message) {
        log(message, DEFAULT_COMPONENT);
    }

    public static void warn(String message, String component) {

    }

    public static void warn(String message) {
        warn(message, DEFAULT_COMPONENT);
    }

    public static void error(String message, String component) {

    }

    public static void error(String message) {
        error(message, DEFAULT_COMPONENT);
    }
}
