package com.example.konstantin.scrollweather.Model;

import android.content.SharedPreferences;

import com.example.konstantin.scrollweather.POJO.BasePOJO;
import com.google.gson.Gson;

/**
 * Created by Konstantin on 15.08.2017.
 *
 * Работа с локально сохраненными погодными данными и настройками программы
 */

interface ILocalDataProvider{

    BasePOJO loadWeatherForToday(SharedPreferences preferences, Gson gson, int cityId);
    BasePOJO loadThreeHourForecast(SharedPreferences preferences, Gson gson, int cityId);
    BasePOJO loadDailyForecast(SharedPreferences preferences, Gson gson, int cityId);
}
