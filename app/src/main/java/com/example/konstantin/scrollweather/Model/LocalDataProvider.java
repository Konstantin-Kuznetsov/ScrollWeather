package com.example.konstantin.scrollweather.Model;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.konstantin.scrollweather.POJO.BasePOJO;
import com.example.konstantin.scrollweather.POJO.CitiesListAdded.CitiesList;
import com.example.konstantin.scrollweather.POJO.WeatherDailyForecast.DailyForecast;
import com.example.konstantin.scrollweather.POJO.WeatherDay.WeatherDay;
import com.example.konstantin.scrollweather.POJO.WeatherThreeHourForecast.ThreeHourForecast;
import com.google.gson.Gson;

/**
 * Created by Konstantin on 15.08.2017.
 */

public class LocalDataProvider implements ILocalDataProvider {

    private final String TAG = "WeatherApp";
    private static final String APP_PREFERENCES_LANG = "lang";
    private static final String APP_PREFERENCES_CNT_DAYS = "cnt_days";
    private static final String APP_PREFERENCES_CNT_3_HOUR_INTERVALS = "cnt_3_hour_intervals";
    private static final String APP_PREFERENCES_UNITS = "units";
    private static final String APP_PREFERENCES_ACCURACY = "accuracy";
    private static final String WEATHER_FOR_TODAY = "weather_for_today";
    private static final String THREE_HOUR_FORECAST = "three_hour_forecast";
    private static final String DAILY_FORECAST = "daily_forecast";
    private static final String CITIES_LIST = "cities_list";

    @Override
    public BasePOJO loadWeatherForToday(SharedPreferences sharedPreferences, Gson gson, int id) {
        String jsonStr = sharedPreferences.getString(WEATHER_FOR_TODAY + id, null);
        if (jsonStr != null) {
            return gson.fromJson(jsonStr, WeatherDay.class);
        }
        // нет данных по запросу, обработка исключения и передача NULL для DataManager
        return null;
    }


    @Override
    public BasePOJO loadThreeHourForecast(SharedPreferences sharedPreferences, Gson gson, int id) {
        String jsonStr = sharedPreferences.getString(THREE_HOUR_FORECAST + id, null);
        if (jsonStr != null) {
            return gson.fromJson(jsonStr, ThreeHourForecast.class);
        }
        // нет данных по запросу, обработка исключения и передача NULL для DataManager
        return null;
    }

    @Override
    public BasePOJO loadDailyForecast(SharedPreferences sharedPreferences, Gson gson, int id) {
        String jsonStr = sharedPreferences.getString(DAILY_FORECAST + id, null);
        if (jsonStr != null) {
            return gson.fromJson(jsonStr, DailyForecast.class);
        }
        // нет данных по запросу, обработка исключения и передача NULL для DataManager
        return null;
    }

    // Запись данных с полученными прогнозами.
    // apply() записывает данные асинхронно, в отличии от commit() и ничего не возвращает
    public void saveDataLocally(SharedPreferences preferences, Gson gson, BasePOJO pojoObject) {
        switch (pojoObject.getTypeOfData()) {
            case WEATHER_DAY:
                // записываем CityInfo
                preferences.edit()
                        .putString(WEATHER_FOR_TODAY + pojoObject.getCityId(), gson.toJson(pojoObject, WeatherDay.class))
                        .apply();
                break;
            case THREE_HOUR_FORECAST:
                // записываем ThreeHourForecast
                preferences.edit()
                        .putString(THREE_HOUR_FORECAST + pojoObject.getCityId(), gson.toJson(pojoObject, ThreeHourForecast.class))
                        .apply();
                break;
            case DAILY_FORECAST:
                // записываем DailyForecast
                preferences.edit()
                        .putString(DAILY_FORECAST + pojoObject.getCityId(), gson.toJson(pojoObject, DailyForecast.class))
                        .apply();
                break;
            default:
                // ошибка типа возвращенных данных
                break;
        }
    }

    // загрузка списка городов, добавленных пользователем
    public CitiesList getCitiesList(SharedPreferences sharedPreferences, Gson gson) {
        String jsonStr = sharedPreferences.getString(CITIES_LIST, null);
        if (jsonStr != null) {
            return gson.fromJson(jsonStr, CitiesList.class);
        }
        // нет данных по запросу, передача NULL для DataManager
        return null;
    }

    // сохранение списка городов, добавленных пользователем
    public void saveCitiesListLocally(CitiesList citiesList, SharedPreferences preferences, Gson gson) {
        preferences.edit()
                .putString(CITIES_LIST, gson.toJson(citiesList, CitiesList.class))
                .apply();
    }

    public String getLangSetting(SharedPreferences sharedPreferences) {
        String lang = sharedPreferences.getString(APP_PREFERENCES_LANG, null);
        if (lang == null) {
            Log.w(TAG, "Ошибка чтения настроечного параметра - \"язык\"");
            return "ru";
        }
        return lang;
    }

    public int getCntDaysSetting(SharedPreferences sharedPreferences) {
        int cntDays = Integer.valueOf(sharedPreferences.getString(APP_PREFERENCES_CNT_DAYS, "-1"));
        if (cntDays == -1) {
            Log.w(TAG, "Ошибка чтения настроечного параметра - \"количество дней в прогнозе\"");
            return 7; // прогноз на 7 дней вперед по умолчанию
        }
        return cntDays;
    }

    public int getCntThreeHourIntervalSetting(SharedPreferences sharedPreferences) {
        int cntDays = Integer.valueOf(sharedPreferences.getString(APP_PREFERENCES_CNT_3_HOUR_INTERVALS, "-1"));
        if (cntDays == -1) {
            Log.w(TAG, "Ошибка чтения настроечного параметра - \"количество интервалов в почасовом прогнозе\"");
            return 8; // 24 часа(8 интервалов по 3) по умолчанию
        }
        return cntDays;
    }

    public String getUnitsSetting(SharedPreferences sharedPreferences) {
        String units = sharedPreferences.getString(APP_PREFERENCES_UNITS, null);
        if (units == null) {
            Log.w(TAG, "Ошибка чтения настроечного параметра - \"система единиц измерения\"");
            return "metric";
        }
        return units;
    }

    public String getAccuracySetting(SharedPreferences sharedPreferences) {
        String accuracy = sharedPreferences.getString(APP_PREFERENCES_ACCURACY, null);
        if (accuracy == null) {
            Log.w(TAG, "Ошибка чтения настроечного параметра - \"accuracy/precision\"");
            return "like";
        }
        return accuracy;
    }
}
