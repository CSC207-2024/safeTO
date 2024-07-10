package types;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class Place {
    @SerializedName("place_id")
    private long placeId;
    private String licence;
    @SerializedName("osm_type")
    private String osmType;
    @SerializedName("osm_id")
    private long osmId;
    private String lat;
    private String lon;
    @SerializedName("class")
    private String classType;
    private String type;
    @SerializedName("place_rank")
    private int placeRank;
    private double importance;
    @SerializedName("addresstype")
    private String addressType;
    private String name;
    @SerializedName("display_name")
    private String displayName;
    private Address address;
    private String[] boundingbox;

    public long getPlaceId() {
        return placeId;
    }

    public String getLicence() {
        return licence;
    }

    public String getOsmType() {
        return osmType;
    }

    public long getOsmId() {
        return osmId;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getClassType() {
        return classType;
    }

    public String getType() {
        return type;
    }

    public int getPlaceRank() {
        return placeRank;
    }

    public double getImportance() {
        return importance;
    }

    public String getAddressType() {
        return addressType;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Address getAddress() {
        return address;
    }

    public String[] getBoundingbox() {
        return boundingbox;
    }

   // @Override

    public static void main(String[] args) {
        String json = "{\"place_id\":7937679,\"licence\":\"Data © OpenStreetMap contributors, ODbL 1.0. http://osm.org/copyright\",\"osm_type\":\"way\",\"osm_id\":1004829152,\"lat\":\"43.66674125\",\"lon\":\"-79.39198131930586\",\"class\":\"leisure\",\"type\":\"garden\",\"place_rank\":30,\"importance\":9.99999999995449e-06,\"addresstype\":\"leisure\",\"name\":\"\",\"display_name\":\"Queen's Park Crescent East, Bloor Street Culture Corridor, University—Rosedale, Old Toronto, Toronto, Golden Horseshoe, Ontario, M5S 1K7, Canada\",\"address\":{\"road\":\"Queen's Park Crescent East\",\"neighbourhood\":\"Bloor Street Culture Corridor\",\"quarter\":\"University—Rosedale\",\"city\":\"Old Toronto\",\"state_district\":\"Golden Horseshoe\",\"state\":\"Ontario\",\"ISO3166-2-lvl4\":\"CA-ON\",\"postcode\":\"M5S 1K7\",\"country\":\"Canada\",\"country_code\":\"ca\"},\"boundingbox\":[\"43.6666818\",\"43.6667634\",\"-79.3921205\",\"-79.3919253\"]}";

        Gson gson = new Gson();
        Place place = gson.fromJson(json, Place.class);

        System.out.println("Place ID: " + place.getPlaceId());
        System.out.println("Road: " + place.getAddress().getRoad());
        // Add more print statements if needed to print other fields
    }
}
