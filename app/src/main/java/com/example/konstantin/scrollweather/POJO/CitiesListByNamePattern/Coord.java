package com.example.konstantin.scrollweather.POJO.CitiesListByNamePattern;

import com.google.gson.annotations.SerializedName;

/**
 *  Created by Konstantin on 27.07.2017.
 *
 *  Координаты города, погода по которому была запрошена.
 */

public class Coord {
    @SerializedName("lon")
    private float lon;
    @SerializedName("lat")
    private float lat;

    public float getLon() {
        return lon;
    }

    public float getLat() {
        return lat;
    }
}
