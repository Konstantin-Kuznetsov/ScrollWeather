package com.example.konstantin.scrollweather.POJO.CitiesListByNamePattern;

import com.google.gson.annotations.SerializedName;

/**
 *  Created by Konstantin on 27.07.2017.
 *
 *  Скорость и направление ветра
 */

public class Wind {
    @SerializedName("speed")
    public float speed;
    @SerializedName("deg")
    public float deg;

    public float getSpeed() {
        return speed;
    }

    public float getDeg() {
        return deg;
    }
}
