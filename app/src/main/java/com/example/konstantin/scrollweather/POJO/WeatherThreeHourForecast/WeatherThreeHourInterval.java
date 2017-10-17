package com.example.konstantin.scrollweather.POJO.WeatherThreeHourForecast;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Konstantin on 10.08.2017.
 */

public class WeatherThreeHourInterval {
    @SerializedName("dt")
    private long dt; // Time of data forecasted, unix, UTC
    @SerializedName("main")
    private MainMeasurements mainMeasurements;
    @SerializedName("weather")
    private List<WeatherType> weatherTypeList = null;
    @SerializedName("clouds")
    private Clouds clouds;
    @SerializedName("wind")
    private Wind wind;
    @SerializedName("dt_txt") // Data/time of calculation, UTC
    private String dtTxt;

    public long getDt() {
        return dt;
    }

    public MainMeasurements getMainMeasurements() {
        return mainMeasurements;
    }

    public List<WeatherType> getWeatherTypeList() {
        return weatherTypeList;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public Wind getWind() {
        return wind;
    }

    public String getDtTxt() {
        return dtTxt;
    }

    public String getWeatherIconUrl() {
        return "http://openweathermap.org/img/w/" + getWeatherTypeList().get(0).getIcon() + ".png";
    }
}
