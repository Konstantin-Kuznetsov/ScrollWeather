package com.example.konstantin.scrollweather.POJO.WeatherThreeHourForecast;

import com.example.konstantin.scrollweather.POJO.BasePOJO;
import com.example.konstantin.scrollweather.POJO.TypeOfData;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Konstantin on 10.08.2017.
 */

public class ThreeHourForecast implements BasePOJO {
    @SerializedName("city")
    private City city;
    @SerializedName("cod")
    private int cod;
    @SerializedName("cnt")
    private int cnt;
    @SerializedName("list")
    private ArrayList<WeatherThreeHourInterval> forecastList = null;


    public City getCity() {
        return city;
    }

    public int getCod() {
        return cod;
    }

    public int getCnt() {
        return cnt;
    }

    public List<WeatherThreeHourInterval> getForecastList() {
        return forecastList;
    }

    @Override
    public TypeOfData getTypeOfData() {
        return TypeOfData.THREE_HOUR_FORECAST;
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
