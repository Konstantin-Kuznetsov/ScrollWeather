package com.example.konstantin.scrollweather.POJO.WeatherDailyForecast;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Konstantin on 11.08.2017.
 */

public class Temp {
    @SerializedName("day")
    private float day;
    @SerializedName("min")
    private float min;
    @SerializedName("max")
    private float max;
    @SerializedName("night")
    private float night;
    @SerializedName("eve")
    private float eve;
    @SerializedName("morn")
    private float morn;

    public float getDayTemp() {
        return day;
    }

    public float getMinTemp() {
        return min;
    }

    public float getMaxTemp() {
        return max;
    }

    public float getNightTemp() {
        return night;
    }

    public float getEveTemp() {
        return eve;
    }

    public float getMornTemp() {
        return morn;
    }
}
