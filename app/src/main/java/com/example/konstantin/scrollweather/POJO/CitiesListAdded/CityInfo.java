package com.example.konstantin.scrollweather.POJO.CitiesListAdded;


import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Konstantin on 20.09.2017.
 */

public class CityInfo {
    private int cityId;
    private String cityName;
    private String countryCode;
    private float lat;
    private float lon;

    public CityInfo(int cityId, String cityName, String countryCode, float lat, float lon) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.countryCode = countryCode;
        this.lat = lat;
        this.lon = lon;
    }

    public int getCityId() {
        return cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public float getLat() {
        return lat;
    }

    public float getLon() {
        return lon;
    }

    public LatLng GetLatLngPosition() {
        return new LatLng(lat, lon);
    }
}
