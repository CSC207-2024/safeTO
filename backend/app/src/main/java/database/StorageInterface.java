package database;

/**
 * An interface for storing user data in the SafeTo database.
 */
public interface StorageInterface {
    boolean storeEmail(String collection, String userId, String email);
}
