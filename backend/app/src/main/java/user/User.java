package user;

import location.SimpleLocation;

/**
 * Represents a user in the system.
 */
public class User {

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
     * @param firstName the user's first name
     * @param lastName  the user's last name
     * @param email     the user's email address
     * @param phoneNumber the user's phone number
     * @param address the user's address
     * @param subscribed the user's subscription status
     * @param location  the user's location
     */
    public User(String firstName, String lastName, String email, String phoneNumber, String address, boolean subscribed, SimpleLocation location) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.subscribed = subscribed;
        this.location = location;
    }

    // Getters and Setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    public SimpleLocation getLocation() {
        return location;
    }

    public void setLocation(SimpleLocation location) {
        this.location = location;
    }
}
