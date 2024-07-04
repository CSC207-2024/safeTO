package types;

import com.google.gson.annotations.SerializedName;

public class Address {
    private String road;
    private String neighbourhood;
    private String quarter;
    private String city;
    @SerializedName("state_district")
    private String stateDistrict;
    private String state;
    @SerializedName("ISO3166-2-lvl4")
    private String iso31662Lvl4;
    private String postcode;
    private String country;
    @SerializedName("country_code")
    private String countryCode;

    public String getRoad() {
        return road;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public String getQuarter() {
        return quarter;
    }

    public String getCity() {
        return city;
    }

    public String getStateDistrict() {
        return stateDistrict;
    }

    public String getState() {
        return state;
    }

    public String getIso31662Lvl4() {
        return iso31662Lvl4;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getCountry() {
        return country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    @Override
    public boolean equals(Object rhs) {
        if (!(rhs instanceof Address)) {
            return false;
        }
        Address ref = (Address) rhs;
    }
}
