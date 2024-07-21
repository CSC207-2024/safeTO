package types;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

// import org.junit.jupiter.api.BeforeAll;

// import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import types.Address;
import types.Place;

public class PlaceTest {
    private static final String placeJson = "{\"place_id\":7937679,\"licence\":\"Data © OpenStreetMap contributors, ODbL 1.0. http://osm.org/copyright\",\"osm_type\":\"way\",\"osm_id\":1004829152,\"lat\":\"43.66674125\",\"lon\":\"-79.39198131930586\",\"class\":\"leisure\",\"type\":\"garden\",\"place_rank\":30,\"importance\":9.99999999995449e-06,\"addresstype\":\"leisure\",\"name\":\"\",\"display_name\":\"Queen's Park Crescent East, Bloor Street Culture Corridor, University—Rosedale, Old Toronto, Toronto, Golden Horseshoe, Ontario, M5S 1K7, Canada\",\"address\":{\"road\":\"Queen's Park Crescent East\",\"neighbourhood\":\"Bloor Street Culture Corridor\",\"quarter\":\"University—Rosedale\",\"city\":\"Old Toronto\",\"state_district\":\"Golden Horseshoe\",\"state\":\"Ontario\",\"ISO3166-2-lvl4\":\"CA-ON\",\"postcode\":\"M5S 1K7\",\"country\":\"Canada\",\"country_code\":\"ca\"},\"boundingbox\":[\"43.6666818\",\"43.6667634\",\"-79.3921205\",\"-79.3919253\"]}";
    private static final String addressJson = "{\"road\":\"Queen's Park Crescent East\",\"neighbourhood\":\"Bloor Street Culture Corridor\",\"quarter\":\"University—Rosedale\",\"city\":\"Old Toronto\",\"state_district\":\"Golden Horseshoe\",\"state\":\"Ontario\",\"ISO3166-2-lvl4\":\"CA-ON\",\"postcode\":\"M5S 1K7\",\"country\":\"Canada\",\"country_code\":\"ca\"}";
    private static final Gson gson = new Gson();

    private static final Place place = gson.fromJson(placeJson, Place.class);
    private static final Place _place = gson.fromJson(placeJson, Place.class);
    private static final Address address = gson.fromJson(addressJson, Address.class);

    @Test
    void testEquals() {
        assertEquals(place, _place);
    }

    @Test
    void testGetAddress() {
        assertEquals(address, place.getAddress());
    }

    @Test
    void testGetAddressType() {
        assertEquals("leisure", place.getAddressType());
    }

    @Test
    void testGetBoundingbox() {
        assertArrayEquals(new String[] { "43.6666818", "43.6667634", "-79.3921205", "-79.3919253" },
                place.getBoundingbox());
    }

    @Test
    void testGetClassType() {
        assertEquals("leisure", place.getClassType());
    }

    @Test
    void testGetDisplayName() {
        assertEquals(
                "Queen's Park Crescent East, Bloor Street Culture Corridor, University—Rosedale, Old Toronto, Toronto, Golden Horseshoe, Ontario, M5S 1K7, Canada",
                place.getDisplayName());
    }

    @Test
    void testGetImportance() {
        assertEquals(9.99999999995449e-06, place.getImportance());
    }

    @Test
    void testGetLat() {
        assertEquals("43.66674125", place.getLat());
    }

    @Test
    void testGetLicence() {
        assertEquals("Data © OpenStreetMap contributors, ODbL 1.0. http://osm.org/copyright", place.getLicence());
    }

    @Test
    void testGetLon() {
        assertEquals("-79.39198131930586", place.getLon());
    }

    @Test
    void testGetName() {
        assertEquals("", place.getName());
    }

    @Test
    void testGetOsmId() {
        assertEquals(1004829152, place.getOsmId());
    }

    @Test
    void testGetOsmType() {
        assertEquals("way", place.getOsmType());
    }

    @Test
    void testGetPlaceId() {
        assertEquals(7937679, place.getPlaceId());
    }

    @Test
    void testGetPlaceRank() {
        assertEquals(30, place.getPlaceRank());
    }

    @Test
    void testGetType() {
        assertEquals("garden", place.getType());
    }
}
