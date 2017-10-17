package com.example.konstantin.scrollweather.POJO.WeatherThreeHourForecast;

import com.google.gson.annotations.SerializedName;

/**
 *  Created by Konstantin on 27.07.2017.
 *
 *  Облачность в процентах.
 */

public class Clouds {
    @SerializedName("all")
    private float cloudiness;

    public float getCloudiness() {
        return cloudiness;
    }
}
