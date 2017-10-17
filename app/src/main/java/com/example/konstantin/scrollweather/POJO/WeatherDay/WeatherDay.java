package com.example.konstantin.scrollweather.POJO.WeatherDay;

import com.example.konstantin.scrollweather.POJO.BasePOJO;
import com.example.konstantin.scrollweather.POJO.TypeOfData;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 *   Created by Konstantin on 27.07.2017.
 *
 *   POJO модель прогноза, возвращаемого сервером, на один день в соответствии
 *
 *   http://openweathermap.org/current#current_JSON
 *
 */

public class WeatherDay implements BasePOJO {
    @SerializedName("coord")
    private Coord coord;
    @SerializedName("weather")
    private ArrayList<WeatherType> weatherTypeList = null;
    @SerializedName("main")
    private MainMeasurements mainMeasurements;
    @SerializedName("visibility")
    private long visibility; // Видимость (в метрах)
    @SerializedName("wind")
    private Wind wind;
    @SerializedName("clouds")
    private Clouds clouds;
    @SerializedName("dt")
    private long calculationTime; // Время, когда были зафиксированы текущие данные
    @SerializedName("sys")
    private Sun sun;
    @SerializedName("id")
    private Integer cityId;
    @SerializedName("name")
    private String cityName;
    @SerializedName("cod")
    private Integer responseCode; // ответ сервера


    public Coord getCoord() {
        return coord;
    }

    public List<WeatherType> getWeatherTypeList() {
        return weatherTypeList;
    }

    public long getVisibility() {
        return visibility;
    }

    public Wind getWind() {
        return wind;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public long getCalculationTime() {
        return calculationTime;
    }

    public Sun getSun() {
        return sun;
    }

    @Override
    public int getCityId() {
        return cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public MainMeasurements getMainMeasurements() {
        return mainMeasurements;
    }

    @Override
    public TypeOfData getTypeOfData() {
        return TypeOfData.WEATHER_DAY;
    }


    @Override
    public ArrayList getDataArrayForAdapter() {
        return weatherTypeList;
    }

    public String getWeatherIconUrl() {
        return "http://openweathermap.org/img/w/" + getWeatherTypeList().get(0).getIcon() + ".png";
    }
}
