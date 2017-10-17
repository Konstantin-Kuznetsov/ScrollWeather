package com.example.konstantin.scrollweather.POJO.CitiesListByNamePattern;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by Konstantin on 01.08.2017.
 */

public class City {
    @SerializedName("id")
    private int cityId;
    @SerializedName("name")
    private String cityName;
    @SerializedName("coord")
    private Coord coord;
    @SerializedName("wind")
    private Wind wind;
    @SerializedName("clouds")
    private Clouds clouds;
    @SerializedName("main")
    private MainMeasurements mainMeasurements;
    @SerializedName("sys")
    private Country country;
    @SerializedName("weather")
    private List<WeatherType> weatherType;

    public String getWeatherIconUrl() {
        return "http://openweathermap.org/img/w/" + getWeatherType().get(0).getIcon() + ".png";
    }

    public int getCityId() {
        return cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public Coord getCoord() {
        return coord;
    }

    public MainMeasurements getMainMeasurements() {
        return mainMeasurements;
    }

    public Country getCountry() {
        return country;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setCoord(Coord coord) {
        this.coord = coord;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public List<WeatherType> getWeatherType() {
        return weatherType;
    }

}
