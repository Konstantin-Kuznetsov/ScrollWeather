package com.example.konstantin.scrollweather.POJO.WeatherDailyForecast;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *  Created by Konstantin on 27.07.2017.
 *
 *  Основные погодные измерения.
 */

public class OneDayForecast {
    @SerializedName("dt") // Time of data forecasted
    private long dt;
    @SerializedName("temp")
    private Temp temp; // температура утро-день-вечер на конкретный день
    @SerializedName("pressure")
    private float pressure;
    @SerializedName("humidity")
    private float humidity;
    @SerializedName("weather")
    private List<WeatherType> weatherTypeList = null;
    @SerializedName("speed")
    private float speed; // Wind speed. Unit Default: meter/sec, Metric: meter/sec, Imperial: miles/hour.
    @SerializedName("deg")
    private float deg; // Wind direction, degrees (meteorological)
    @SerializedName("clouds")
    private float clouds; // Cloudiness, %
    @SerializedName("rain")
    private float rain; // rain, mm
    @SerializedName("snow")
    private float snow; // snow, mm


    public Temp getTemp() {
        return temp;
    }

    public float getPressure() {
        return pressure;
    }

    public float getHumidity() {
        return humidity;
    }

    public List<WeatherType> getWeatherTypeList() {
        return weatherTypeList;
    }

    public long getDt() {
        return dt;
    }

    public float getSpeed() {
        return speed;
    }

    public float getDeg() {
        return deg;
    }

    public float getClouds() {
        return clouds;
    }

    public float getRain() {
        return rain;
    }

    public float getSnow() {
        return snow;
    }

    public String getWeatherIconUrl() {
        return "http://openweathermap.org/img/w/" + getWeatherTypeList().get(0).getIcon() + ".png";
    }
}
