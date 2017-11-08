package com.example.konstantin.scrollweather.Model;

import com.example.konstantin.scrollweather.POJO.CitiesListByNamePattern.ListOfCities;
import com.example.konstantin.scrollweather.POJO.WeatherDailyForecast.DailyForecast;
import com.example.konstantin.scrollweather.POJO.WeatherDay.WeatherDay;
import com.example.konstantin.scrollweather.POJO.WeatherThreeHourForecast.ThreeHourForecast;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Konstantin on 27.07.2017.
 */

public class WeatherApi {

    public interface WeatherApiInterface {
        @GET("weather") // запрос метода weather от сервера по ID города с указанными ниже параметрами
        Call<WeatherDay> getToday(
                @Query("id") int id,
                @Query("lang") String lang,
                @Query("units") String units,
                @Query("appid") String appid
        );

        @GET("find") // запрос списка городов, подходящих по паттерну в поисковой строке(для выбора при добавлении новых)
        Call<ListOfCities> getListByPattern(
                @Query("q") String name,
                @Query("lang") String lang,
                @Query("units") String units,
                @Query("type") String accuracy,
                @Query("cnt") int cnt,
                @Query("appid") String appid
        );

        @GET("forecast") // запрос прогноза погоды с интервалом 3 часа, начиная от текущего времени
        Call<ThreeHourForecast> getThreeHourForecast(
                @Query("id") int id,
                @Query("lang") String lang,
                @Query("units") String units,
                @Query("cnt") int cnt,
                @Query("appid") String appid
        );

        @GET("forecast/daily") // запрос прогноза погоды посуточно с интервалом 1 день
        Call<DailyForecast> getDailyForecast(
                @Query("id") int id,
                @Query("lang") String lang,
                @Query("units") String units,
                @Query("cnt") int cnt, // количество запрашиваемых дней в прогнозе
                @Query("appid") String appid
        );
    }
}
