package com.example.konstantin.scrollweather.POJO.WeatherThreeHourForecast;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Konstantin on 10.08.2017.
 */

public class City {
    @SerializedName("id") // id города
    private int cityId;
    @SerializedName("name")
    private String cityName;
    @SerializedName("coord")
    private Coord coord;
    @SerializedName("country")
    private String countryCode;

    public int getCityId() {
        return cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public Coord getCoord() {
        return coord;
    }

    public String getCountryCode() {
        return countryCode;
    }
}
