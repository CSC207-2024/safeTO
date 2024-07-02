package location;

import java.util.Optional;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class OpenStreetMapLocation extends SimpleLocation {
    private OSMResponse cache;

    private Optional<String> postalCode;
    private Optional<String> address;

    public double getLatitude() {
        return super.getLatitude();
    }

    public double getLongitude() {
        return super.getLongitude();
    }

    public OpenStreetMapLocation(double latitude, double longitude) {
        super(latitude, longitude);
    }
};

// https://nominatim.openstreetmap.org/reverse?lat=43.6667501&lon=-79.3919743&format=json&addressdetails=1

class OSMResponse {
    @SerializedName("place_id")
    private long placeId;

    @SerializedName("licence")
    private String licence;

    @SerializedName("osm_type")
    private String osmType;

    @SerializedName("osm_id")
    private long osmId;

    @SerializedName("lat")
    private String lat;

    @SerializedName("lon")
    private String lon;

    @SerializedName("class")
    private String classType;

    @SerializedName("type")
    private String type;

    @SerializedName("place_rank")
    private int placeRank;

    @SerializedName("importance")
    private double importance;

    @SerializedName("addresstype")
    private String addresstype;

    @SerializedName("name")
    private String name;

    @SerializedName("display_name")
    private String displayName;

    @SerializedName("address")
    private Address address;

    @SerializedName("boundingbox")
    private String[] boundingbox;

    // Getters and Setters...

    public static void main(String[] args) {
        String json = "{\"place_id\":7937679,\"licence\":\"Data © OpenStreetMap contributors, ODbL 1.0. http://osm.org/copyright\",\"osm_type\":\"way\",\"osm_id\":1004829152,\"lat\":\"43.66674125\",\"lon\":\"-79.39198131930586\",\"class\":\"leisure\",\"type\":\"garden\",\"place_rank\":30,\"importance\":9.99999999995449e-06,\"addresstype\":\"leisure\",\"name\":\"\",\"display_name\":\"Queen's Park Crescent East, Bloor Street Culture Corridor, University—Rosedale, Old Toronto, Toronto, Golden Horseshoe, Ontario, M5S 1K7, Canada\",\"address\":{\"road\":\"Queen's Park Crescent East\",\"neighbourhood\":\"Bloor Street Culture Corridor\",\"quarter\":\"University—Rosedale\",\"city\":\"Old Toronto\",\"state_district\":\"Golden Horseshoe\",\"state\":\"Ontario\",\"ISO3166-2-lvl4\":\"CA-ON\",\"postcode\":\"M5S 1K7\",\"country\":\"Canada\",\"country_code\":\"ca\"},\"boundingbox\":[\"43.6666818\",\"43.6667634\",\"-79.3921205\",\"-79.3919253\"]}";

        Gson gson = new Gson();
        OSMResponse place = gson.fromJson(json, OSMResponse.class);

        System.out.println("Place ID: " + place.getPlaceId());
        System.out.println("Road: " + place.getAddress().getRoad());
        // Add more print statements if needed to print other fields
    }
}
