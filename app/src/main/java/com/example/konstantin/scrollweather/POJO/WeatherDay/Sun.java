package com.example.konstantin.scrollweather.POJO.WeatherDay;

import com.google.gson.annotations.SerializedName;

/**
 *  Created by Konstantin on 27.07.2017.
 *
 *  Время восхода и заката UTC и код страны
 */

public class Sun {
    @SerializedName("country")
    private String countryCode;
    @SerializedName("sunrise")
    private int sunrise;
    @SerializedName("sunset")
    private int sunset;

    public String getCountryCode() {
        return countryCode;
    }

    public int getSunrise() {
        return sunrise;
    }

    public int getSunset() {
        return sunset;
    }
}
