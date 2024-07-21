package user;
import java.util.List;
import java.util.Map;
import location.*;

/**
 * Represents a user in the system.
 */
public class User {

    private String firstName;
    private String lastName;
    private String userId;
    private String email;
    //TODO hash the password
    private String password;
    private String phoneNumber;
    private SimpleLocation homeLocation;
    private List<SimpleLocation> savedLocations;
    private Map<String, Boolean> notificationPreferences;
    private List<String> contacts;
    //TODO add profile pic
    //private String profilePictureUrl;
    private long registrationTimestamp;
    private List<Long> lastLoginsTimestamps;

    /**
     * Constructs a new User.
     *
     * @param firstName               the user's first name
     * @param lastName                the user's last name
     * @param userId                  the user's unique identifier
     * @param email                   the user's email address
     * @param password                the user's password
     * @param phoneNumber             the user's phone number
     * @param homeLocation            the user's home location
     * @param savedLocations          a list of locations saved by the user
     * @param notificationPreferences a map of notification preferences (key: notification type, value: enabled/disabled)
     * @param contacts                a list of user contacts
     * @param registrationTimestamp   the timestamp of the user's registration
     * @param lastLoginsTimestamps    a list of timestamps of the user's last logins
     */
    public User(String firstName, String lastName, String userId, String email, String password,
                String phoneNumber, SimpleLocation homeLocation, List<SimpleLocation> savedLocations,
                Map<String, Boolean> notificationPreferences, List<String> contacts, long registrationTimestamp, List<Long> lastLoginsTimestamps) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.homeLocation = homeLocation;
        this.savedLocations = savedLocations;
        this.notificationPreferences = notificationPreferences;
        this.contacts = contacts;
        //this.profilePictureUrl = profilePictureUrl;
        this.registrationTimestamp = registrationTimestamp;
        this.lastLoginsTimestamps = lastLoginsTimestamps;
    }

    /**
     * Gets the user's first name.
     *
     * @return the user's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the user's first name.
     *
     * @param firstName the user's first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the user's last name.
     *
     * @return the user's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the user's last name.
     *
     * @param lastName the user's last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the user's unique identifier.
     *
     * @return the user's unique identifier
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the user's unique identifier.
     *
     * @param userId the user's unique identifier
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Gets the user's email address.
     *
     * @return the user's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email address.
     *
     * @param email the user's email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the hash of the user's password.
     *
     * @return the hash of the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the hash of the user's password.
     *
     * @param passwordHash the hash of the user's password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the user's phone number.
     *
     * @return the user's phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the user's phone number.
     *
     * @param phoneNumber the user's phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the user's home location.
     *
     * @return the user's home location
     */
    public SimpleLocation getHomeLocation() {
        return homeLocation;
    }

    /**
     * Sets the user's home location.
     *
     * @param homeLocation the user's home location
     */
    public void setHomeLocation(SimpleLocation homeLocation) {
        this.homeLocation = homeLocation;
    }

    /**
     * Gets the list of locations saved by the user.
     *
     * @return the list of locations saved by the user
     */
    public List<SimpleLocation> getSavedLocations() {
        return savedLocations;
    }

    /**
     * Sets the list of locations saved by the user.
     *
     * @param savedLocations the list of locations saved by the user
     */
    public void setSavedLocations(List<SimpleLocation> savedLocations) {
        this.savedLocations = savedLocations;
    }

    /**
     * Gets the user's notification preferences.
     *
     * @return the user's notification preferences
     */
    public Map<String, Boolean> getNotificationPreferences() {
        return notificationPreferences;
    }

    /**
     * Sets the user's notification preferences.
     *
     * @param notificationPreferences the user's notification preferences
     */
    public void setNotificationPreferences(Map<String, Boolean> notificationPreferences) {
        this.notificationPreferences = notificationPreferences;
    }

    /**
     * Gets the list of user contacts.
     *
     * @return the list of user contacts
     */
    public List<String> getContacts() {
        return contacts;
    }

    /**
     * Sets the list of user contacts.
     *
     * @param contacts the list of user contacts
     */
    public void setContacts(List<String> contacts) {
        this.contacts = contacts;
    }

    // /**
    //  * Gets the URL of the user's profile picture.
    //  *
    //  * @return the URL of the user's profile picture
    //  */
    // public String getProfilePictureUrl() {
    //     return profilePictureUrl;
    // }

    // /**
    //  * Sets the URL of the user's profile picture.
    //  *
    //  * @param profilePictureUrl the URL of the user's profile picture
    //  */
    // public void setProfilePictureUrl(String profilePictureUrl) {
    //     this.profilePictureUrl = profilePictureUrl;
    // }

    /**
     * Gets the timestamp of the user's registration.
     *
     * @return the timestamp of the user's registration
     */
    public long getRegistrationTimestamp() {
        return registrationTimestamp;
    }

    /**
     * Sets the timestamp of the user's registration.
     *
     * @param registrationTimestamp the timestamp of the user's registration
     */
    public void setRegistrationTimestamp(long registrationTimestamp) {
        this.registrationTimestamp = registrationTimestamp;
    }

    /**
     * Gets the list of timestamps of the user's last logins.
     *
     * @return the list of timestamps of the user's last logins
     */
    public List<Long> getLastLoginsTimestamps() {
        return lastLoginsTimestamps;
    }

    /**
     * Sets the list of timestamps of the user's last logins.
     *
     * @param lastLoginsTimestamps the list of timestamps of the user's last logins
     */
    public void setLastLoginsTimestamps(List<Long> lastLoginsTimestamps) {
        this.lastLoginsTimestamps = lastLoginsTimestamps;
    }
}
