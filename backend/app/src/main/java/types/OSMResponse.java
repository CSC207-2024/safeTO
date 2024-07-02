package types;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class OSMResponse {
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

}