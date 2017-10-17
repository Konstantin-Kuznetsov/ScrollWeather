package com.example.konstantin.scrollweather.Model;

import android.support.annotation.Nullable;

import com.example.konstantin.scrollweather.Custom_Markers.AbstractMarker;
import com.example.konstantin.scrollweather.ForecastsFragmentPresenter;
import com.example.konstantin.scrollweather.POJO.WeatherDay.WeatherDay;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Konstantin on 13.08.2017.
 *
 * Базовые методы запроса данных
 */

interface IBaseData {
    // запрос по конкретному городу( по его ID)
    void getWeatherForToday(int cityID);
    void getThreeHourForecast(int cityID);
    void getDailyForecast(int cityID);
    WeatherDay getDataForMarker(AbstractMarker markerh);
}
