package com.example.konstantin.scrollweather.Custom_Markers;

import android.net.Uri;

import com.example.konstantin.scrollweather.POJO.CitiesListAdded.CityInfo;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

/**
 * Created by Konstantin on 13.07.2017.
 *
 * Маркер - город из списка на карте.
 */

public class CityMarker extends AbstractMarker {

    private final String title;
    private final int cityID;


    public CityMarker(CityInfo cityInfo) {
        markerPosition = new LatLng(cityInfo.getLat(), cityInfo.getLon());
        title = String.format(Locale.getDefault(), "%s, %s", cityInfo.getCityName(), cityInfo.getCountryCode());
        cityID = cityInfo.getCityId(); // ID города, к которому относится маркер для формирования запроса погоды к серверу
    }

    @Override
    public MarkerOptions getMarkerOptions() {
        // создаем и конфигурируем маркеры для показа местоположения пользователя и ближайшей картинки
        return new MarkerOptions()
                .position(markerPosition)
                .title(title);
    }

    @Override
    public Marker addMarkerOnMap(GoogleMap map) {
        return map.addMarker(getMarkerOptions());
    }


    @Override
    public String getTitle() {
        return title;
    }

    public int getCityID() {
        return cityID;
    }
}
