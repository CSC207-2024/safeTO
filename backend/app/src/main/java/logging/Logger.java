package logging;

import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.util.logging.Level;
import java.util.logging.ConsoleHandler;
import java.io.IOException;

/**
 * A custom logging class for the backend, providing logging mechanisms
 * for various severity levels while maintaining compatibility with the
 * Java Logging API.
 * <p>
 * This Logger class initializes a logging framework that writes logs to a
 * file named "csc207-backend.log" and optionally to the console. It is set
 * up to ensure all log messages fall under the "backend." namespace to
 * maintain organized log files.
 * 
 * <p>
 * Note: This Logger class exists for compatibility purposes. Newer code
 * should directly acquire their logger from
 * {@link java.util.logging.Logger#getLogger(String)}, as per the
 * documentation from Oracle:
 * <a href=
 * "https://docs.oracle.com/javase/7/docs/api/java/util/logging/Logger.html">Java
 * Logging API</a>.
 * </p>
 */
public class Logger {
    private static final String rootLoggerName = "backend";
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(rootLoggerName);

    static {
        try {
            // Create a FileHandler to log to a file
            FileHandler fileHandler = new FileHandler("csc207-backend.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.ALL);
            logger.addHandler(fileHandler);

            // Optionally, configure the console handler
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.ALL);
            consoleHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(consoleHandler);

            // Set the logger level
            logger.setLevel(Level.ALL);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to initialize file handler", e);
        }
    }

    /**
     * Logs an informational message to the specified component's logger.
     *
     * @param message   the message to log
     * @param component the name of the component logger
     */
    public static void log(String message, String component) {
        java.util.logging.Logger.getLogger(component).info(message);
    }

    /**
     * Logs an informational message to the backend logger.
     *
     * @param message the message to log
     */
    public static void log(String message) {
        logger.info(message);
    }

    /**
     * Logs a warning message to the specified component's logger.
     *
     * @param message   the message to log
     * @param component the name of the component logger
     */
    public static void warn(String message, String component) {
        java.util.logging.Logger.getLogger(component).warning(message);
    }

    /**
     * Logs a warning message to the backend logger.
     *
     * @param message the message to log
     */
    public static void warn(String message) {
        logger.warning(message);
    }

    /**
     * Logs an error message to the specified component's logger.
     *
     * @param message   the message to log
     * @param component the name of the component logger
     */
    public static void error(String message, String component) {
        java.util.logging.Logger.getLogger(component).severe(message);
    }

    /**
     * Logs an error message to the backend logger.
     *
     * @param message the message to log
     */
    public static void error(String message) {
        logger.severe(message);
    }
}
