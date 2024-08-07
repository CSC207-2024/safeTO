package user;

import location.SimpleLocation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        SimpleLocation location = new SimpleLocation(43.6532, -79.3832);
        user = new User(
                "John", "Doe", "john.doe@example.com", "123-456-7890", "123 Elm Street",
                true, location
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
    public void testGetEmail() {
        assertEquals("john.doe@example.com", user.getEmail(), "Email should be john.doe@example.com");
    }

    @Test
    public void testSetEmail() {
        user.setEmail("jane.doe@example.com");
        assertEquals("jane.doe@example.com", user.getEmail(), "Email should be updated to jane.doe@example.com");
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
    public void testGetAddress() {
        assertEquals("123 Elm Street", user.getAddress(), "Address should be 123 Elm Street");
    }

    @Test
    public void testSetAddress() {
        user.setAddress("456 Oak Street");
        assertEquals("456 Oak Street", user.getAddress(), "Address should be updated to 456 Oak Street");
    }

    @Test
    public void testIsSubscribed() {
        assertTrue(user.isSubscribed(), "User should be subscribed");
    }

    @Test
    public void testSetSubscribed() {
        user.setSubscribed(false);
        assertFalse(user.isSubscribed(), "User subscription status should be updated to false");
    }

    @Test
    public void testGetLocation() {
        SimpleLocation location = user.getLocation();
        assertEquals(43.6532, location.getLatitude(), "Latitude should be 43.6532");
        assertEquals(-79.3832, location.getLongitude(), "Longitude should be -79.3832");
    }

    @Test
    public void testSetLocation() {
        SimpleLocation newLocation = new SimpleLocation(44.6532, -80.3832);
        user.setLocation(newLocation);
        assertEquals(newLocation, user.getLocation(), "Location should be updated");
    }
}
