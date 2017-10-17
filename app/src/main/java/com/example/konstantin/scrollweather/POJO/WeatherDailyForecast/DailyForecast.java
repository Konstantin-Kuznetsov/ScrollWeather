package com.example.konstantin.scrollweather.POJO.WeatherDailyForecast;

import com.example.konstantin.scrollweather.POJO.BasePOJO;
import com.example.konstantin.scrollweather.POJO.TypeOfData;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Konstantin on 11.08.2017.
 */

public class DailyForecast implements BasePOJO {
    @SerializedName("city")
    private City city;
    @SerializedName("cod")
    private int cod;
    @SerializedName("cnt")
    private int cnt;
    @SerializedName("list")
    private ArrayList<OneDayForecast> forecastList = null;


    //private float rain; ??? snow - ???


    public City getCity() {
        return city;
    }

    public int getCod() {
        return cod;
    }

    public int getCnt() {
        return cnt;
    }

    public List<OneDayForecast> getForecastList() {
        return forecastList;
    }

    @Override
    public TypeOfData getTypeOfData() {
        return TypeOfData.DAILY_FORECAST;
    }

    @Override
    public int getCityId() {
        return getCity().getCityId();
    }

    @Override
    public ArrayList getDataArrayForAdapter() {
        return forecastList;
    }
}
