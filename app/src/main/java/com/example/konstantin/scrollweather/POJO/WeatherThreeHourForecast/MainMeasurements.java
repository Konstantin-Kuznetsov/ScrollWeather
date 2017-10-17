package com.example.konstantin.scrollweather.POJO.WeatherThreeHourForecast;

import com.google.gson.annotations.SerializedName;

/**
 *  Created by Konstantin on 27.07.2017.
 *
 *  Основные погодные измерения.
 */

public class MainMeasurements {
    @SerializedName("temp")
    private float temp;
    @SerializedName("pressure")
    private float pressure;
    @SerializedName("humidity")
    private float humidity;
    @SerializedName("temp_min")
    private float tempMin;
    @SerializedName("temp_max")
    private float tempMax;
    @SerializedName("sea_level") // Atmospheric pressure on the sea level, hPa
    private float seaLevel;
    @SerializedName("grnd_level") // Atmospheric pressure on the ground level, hPa
    private float grndLevel;

    public float getTemp() {
        return temp;
    }

    public float getPressure() {
        return pressure;
    }

    public float getHumidity() {
        return humidity;
    }

    public float getTempMin() {
        return tempMin;
    }

    public float getTempMax() {
        return tempMax;
    }

    public float getSeaLevelPressure() {
        return seaLevel;
    }

    public float getGrndLevelPressure() {
        return grndLevel;
    }
}
