package singleton;

import com.google.gson.Gson;

/**
 * A singleton class that provides a single instance of Gson for JSON
 * serialization and deserialization.
 * <p>
 * This class is designed to ensure that there is only one instance of Gson
 * throughout the application,
 * which offers a centralized point of access. The use of a singleton pattern is
 * beneficial for several reasons:
 * <ul>
 * <li>
 * <strong>Single Point of Entry:</strong> By providing a single access point to
 * the Gson instance,
 * it allows for consistent configuration of formatting options and settings
 * across the application.
 * </li>
 * <li>
 * <strong>Performance Optimization:</strong> Instantiating Gson can be
 * relatively expensive,
 * especially if done multiple times. This singleton approach avoids the
 * overhead of creating
 * multiple instances, thereby improving performance.
 * </li>
 * <li>
 * <strong>Controlled Configuration:</strong> If there need to be changes in the
 * Gson configuration
 * (such as custom serializers or deserializers), they can be centrally managed
 * within this class,
 * ensuring that all parts of the application receive these changes
 * automatically.
 * </li>
 * </ul>
 * </p>
 */
public class GsonSingleton {
    private static final Gson instance = new Gson();

    // Private constructor to prevent instantiation
    private GsonSingleton() {
        // private constructor to prevent instantiation
    }

    /**
     * Provides access to the single instance of Gson.
     *
     * @return the singleton instance of Gson
     */
    public static Gson getInstance() {
        return instance;
    }
}
