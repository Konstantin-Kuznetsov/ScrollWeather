package com.example.konstantin.scrollweather.POJO.WeatherDailyForecast;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Konstantin on 27.07.2017.
 */

class WeatherType {
    @SerializedName("id")
    private int id; // id типа(характера) погоды
    @SerializedName("main")
    private String type; // Group of weather parameters (Rain, Snow, Extreme etc.)
    @SerializedName("description")
    private String description; // Weather condition within the group (light intensity drizzle etc)
    @SerializedName("icon")
    private String icon;

    public int getId() {
        return id;
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
