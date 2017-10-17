package com.example.konstantin.scrollweather.POJO.CitiesListByNamePattern;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Konstantin on 27.07.2017.
 */

public class WeatherType {
    @SerializedName("id")
    private int idWeather; // id типа(характера) погоды
    @SerializedName("main")
    private String type; // Group of weather parameters (Rain, Snow, Extreme etc.)
    @SerializedName("description")
    private String description; // Weather condition within the group (light intensity drizzle etc)
    @SerializedName("icon")
    private String icon;

    public int getIdWeather() {
        return idWeather;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

}
