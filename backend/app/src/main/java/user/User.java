package user;

import location.SimpleLocation;

/**
 * Represents a user in the system.
 */
public class User {

    private String userID;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private boolean subscribed;
    private SimpleLocation location;

    /**
     * Constructs a new User.
     *
     * @param userID the user's user ID
     * @param firstName the user's first name
     * @param lastName  the user's last name
     * @param email     the user's email address
     * @param phoneNumber the user's phone number
     * @param address the user's address
     * @param subscribed the user's subscription status
     * @param location  the user's location
     */
    public User(String userID, String firstName, String lastName, String email, String phoneNumber, String address, boolean subscribed, SimpleLocation location) {
        this,userID = userID
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.subscribed = subscribed;
        this.location = location;
    }

    // Getters and Setters

    /**
     * Gets the user's user ID.
     *
     * @return the user's user ID
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Set the user's first name.
     *
     * @param userID the user ID for the user
     */
    public void setUserID(String userID) {
        this.userID = userID;
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
     * @param firstName the new first name for the user
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
     * @param lastName the new last name for the user
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
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
     * @param email the new email address for the user
     */
    public void setEmail(String email) {
        this.email = email;
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
     * @param phoneNumber the new phone number for the user
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Gets the user's address.
     *
     * @return the user's address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the user's address.
     *
     * @param address the new address for the user
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Checks if the user is subscribed.
     *
     * @return true if the user is subscribed, false otherwise
     */
    public boolean isSubscribed() {
        return subscribed;
    }

    /**
     * Sets the user's subscription status.
     *
     * @param subscribed the new subscription status for the user
     */
    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    /**
     * Gets the user's location.
     *
     * @return the user's location
     */
    public SimpleLocation getLocation() {
        return location;
    }

    /**
     * Sets the user's location.
     *
     * @param location the new location for the user
     */
    public void setLocation(SimpleLocation location) {
        this.location = location;
    }
}
