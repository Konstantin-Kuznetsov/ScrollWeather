package com.example.konstantin.scrollweather.POJO.CitiesListByNamePattern;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Konstantin on 01.08.2017.
 */

public class Country {
    @SerializedName("country")
    private String countryCode;

    public String getCountryCode() {
        return countryCode;
    }
}
