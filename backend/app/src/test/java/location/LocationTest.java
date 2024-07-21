package location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LocationTest {

    private SimpleLocation simpleLocation;
    private StrongLocation strongLocation;

    @BeforeEach
    public void setUp() {
        simpleLocation = new SimpleLocation(43.737207, -79.343448); // Example coordinates for Toronto
        strongLocation = new StrongLocation(43.6532, -79.3832, "M5V 3C6", "123 Main St, Toronto, ON");
    }

    // ================= Test Simple Location==================
    @Test
    public void testGetLatitude() {
        assertEquals(43.737207, simpleLocation.getLatitude(), "Latitude should be 43.737207");
    }

    @Test
    public void testSetLatitude() {
        simpleLocation.setLatitude(34.0522); // Example coordinates for Los Angeles
        assertEquals(34.0522, simpleLocation.getLatitude(), "Latitude should be updated to 34.0522");
    }

    @Test
    public void testGetLongitude() {
        assertEquals(-79.343448, simpleLocation.getLongitude(), "Longitude should be -79.343448");
    }

    @Test
    public void testSetLongitude() {
        simpleLocation.setLongitude(-118.2437); // Example coordinates for Los Angeles
        assertEquals(-118.2437, simpleLocation.getLongitude(), "Longitude should be updated to -118.2437");
    }

    @Test
    public void testToString1() {
        String expected = "Location{latitude=43.737207, longitude=-79.343448}";
        assertEquals(expected, simpleLocation.toString(),
                "toString() method should return correct string representation");
    }

    // ================= Test Strong Location==================
    @Test
    public void testConstructorValidPostalCode() {
        assertEquals("M5V 3C6", strongLocation.getPostalCode(), "Postal code should be M5V 3C6");
        assertEquals("123 Main St, Toronto, ON", strongLocation.getAddress(),
                "Address should be 123 Main St, Toronto, ON");
    }

    @Test
    public void testConstructorInvalidPostalCode() {
        assertThrows(IllegalArgumentException.class, () -> {
            new StrongLocation(43.6532, -79.3832, "12345", "123 Main St, Toronto, ON");
        }, "Should throw IllegalArgumentException for invalid postal code format");
    }

    @Test
    public void testSetPostalCodeValid() {
        strongLocation.setPostalCode("K1A 0B1");
        assertEquals("K1A 0B1", strongLocation.getPostalCode(), "Postal code should be updated to K1A 0B1");
    }

    @Test
    public void testSetPostalCodeInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            strongLocation.setPostalCode("ABCDE");
        }, "Should throw IllegalArgumentException for invalid postal code format");
    }

    @Test
    public void testSetAddress() {
        strongLocation.setAddress("456 Elm St, Toronto, ON");
        assertEquals("456 Elm St, Toronto, ON", strongLocation.getAddress(),
                "Address should be updated to 456 Elm St, Toronto, ON");
    }

    @Test
    public void testToString2() {
        String expected = "Location{latitude=43.6532, longitude=-79.3832, postalCode='M5V 3C6', address='123 Main St, Toronto, ON'}";
        assertEquals(expected, strongLocation.toString(),
                "toString() method should return the correct string representation");
    }
}
