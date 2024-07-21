package userTester;

import location.SimpleLocation;
import user.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        SimpleLocation homeLocation = new SimpleLocation(43.6532, -79.3832);
        List<SimpleLocation> savedLocations = Arrays.asList(
                new SimpleLocation(44.6532, -80.3832),
                new SimpleLocation(45.6532, -81.3832)
        );
        Map<String, Boolean> notificationPreferences = new HashMap<>();
        notificationPreferences.put("Email", true);
        notificationPreferences.put("SMS", false);
        List<String> contacts = Arrays.asList("contact1@example.com", "contact2@example.com");
        List<Long> lastLoginsTimestamps = Arrays.asList(1627884000000L, 1627970400000L);

        user = new User(
                "John", "Doe", "user123", "john.doe@example.com", "hashedpassword",
                "123-456-7890", homeLocation, savedLocations, notificationPreferences,
                contacts, 1627814400000L, lastLoginsTimestamps
        );
    }

    @Test
    public void testGetFirstName() {
        assertEquals("John", user.getFirstName(), "First name should be John");
    }

    @Test
    public void testSetFirstName() {
        user.setFirstName("Jane");
        assertEquals("Jane", user.getFirstName(), "First name should be updated to Jane");
    }

    @Test
    public void testGetLastName() {
        assertEquals("Doe", user.getLastName(), "Last name should be Doe");
    }

    @Test
    public void testSetLastName() {
        user.setLastName("Smith");
        assertEquals("Smith", user.getLastName(), "Last name should be updated to Smith");
    }

    @Test
    public void testGetUserId() {
        assertEquals("user123", user.getUserId(), "User ID should be user123");
    }

    @Test
    public void testSetUserId() {
        user.setUserId("user456");
        assertEquals("user456", user.getUserId(), "User ID should be updated to user456");
    }

    @Test
    public void testGetEmail() {
        assertEquals("john.doe@example.com", user.getEmail(), "Email should be john.doe@example.com");
    }

    @Test
    public void testSetEmail() {
        user.setEmail("jane.doe@example.com");
        assertEquals("jane.doe@example.com", user.getEmail(), "Email should be updated to jane.doe@example.com");
    }

    @Test
    public void testGetPassword() {
        assertEquals("hashedpassword", user.getPassword(), "Password should be hashedpassword");
    }

    @Test
    public void testSetPassword() {
        user.setPassword("newhashedpassword");
        assertEquals("newhashedpassword", user.getPassword(), "Password should be updated to newhashedpassword");
    }

    @Test
    public void testGetPhoneNumber() {
        assertEquals("123-456-7890", user.getPhoneNumber(), "Phone number should be 123-456-7890");
    }

    @Test
    public void testSetPhoneNumber() {
        user.setPhoneNumber("987-654-3210");
        assertEquals("987-654-3210", user.getPhoneNumber(), "Phone number should be updated to 987-654-3210");
    }

    @Test
    public void testGetHomeLocation() {
        SimpleLocation location = user.getHomeLocation();
        assertEquals(43.6532, location.getLatitude(), "Latitude should be 43.6532");
        assertEquals(-79.3832, location.getLongitude(), "Longitude should be -79.3832");
    }

    @Test
    public void testSetHomeLocation() {
        SimpleLocation newLocation = new SimpleLocation(44.6532, -80.3832);
        user.setHomeLocation(newLocation);
        assertEquals(newLocation, user.getHomeLocation(), "Home location should be updated");
    }

    @Test
    public void testGetSavedLocations() {
        List<SimpleLocation> locations = user.getSavedLocations();
        assertEquals(2, locations.size(), "Saved locations size should be 2");
    }

    @Test
    public void testSetSavedLocations() {
        List<SimpleLocation> newLocations = Arrays.asList(
                new SimpleLocation(46.6532, -82.3832)
        );
        user.setSavedLocations(newLocations);
        assertEquals(newLocations, user.getSavedLocations(), "Saved locations should be updated");
    }

    @Test
    public void testGetNotificationPreferences() {
        Map<String, Boolean> preferences = user.getNotificationPreferences();
        assertTrue(preferences.get("Email"), "Email notifications should be enabled");
        assertFalse(preferences.get("SMS"), "SMS notifications should be disabled");
    }

    @Test
    public void testSetNotificationPreferences() {
        Map<String, Boolean> newPreferences = new HashMap<>();
        newPreferences.put("Push", true);
        user.setNotificationPreferences(newPreferences);
        assertEquals(newPreferences, user.getNotificationPreferences(), "Notification preferences should be updated");
    }

    @Test
    public void testGetContacts() {
        List<String> contacts = user.getContacts();
        assertEquals(2, contacts.size(), "Contacts size should be 2");
    }

    @Test
    public void testSetContacts() {
        List<String> newContacts = Arrays.asList("contact3@example.com", "contact4@example.com");
        user.setContacts(newContacts);
        assertEquals(newContacts, user.getContacts(), "Contacts should be updated");
    }

    @Test
    public void testGetRegistrationTimestamp() {
        assertEquals(1627814400000L, user.getRegistrationTimestamp(), "Registration timestamp should be 1627814400000");
    }

    @Test
    public void testSetRegistrationTimestamp() {
        user.setRegistrationTimestamp(1627900800000L);
        assertEquals(1627900800000L, user.getRegistrationTimestamp(), "Registration timestamp should be updated to 1627900800000");
    }

    @Test
    public void testGetLastLoginsTimestamps() {
        List<Long> timestamps = user.getLastLoginsTimestamps();
        assertEquals(2, timestamps.size(), "Last logins timestamps size should be 2");
    }

    @Test
    public void testSetLastLoginsTimestamps() {
        List<Long> newTimestamps = Arrays.asList(1627987200000L, 1628073600000L);
        user.setLastLoginsTimestamps(newTimestamps);
        assertEquals(newTimestamps, user.getLastLoginsTimestamps(), "Last logins timestamps should be updated");
    }
}
