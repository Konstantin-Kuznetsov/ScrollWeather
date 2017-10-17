package com.example.konstantin.scrollweather.Model;

/**
 * Created by Konstantin on 15.08.2017.
 *
 * Загрузка данных из сети
 */

public interface IRESTDataProvider {

    void loadWeatherForToday(DataManager dataManager, WeatherApi.WeatherApiInterface api, int id, String lang, String units, String key);
    void loadThreeHourForecast(DataManager dataManager, WeatherApi.WeatherApiInterface api, int cityID, String lang, String units, int cntIntervals, String key);
    void loadDailyForecast(DataManager dataManager, WeatherApi.WeatherApiInterface api, int cityID, String lang, String units, int cntDays, String key);
}
