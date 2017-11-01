package com.example.konstantin.scrollweather.Model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.konstantin.scrollweather.POJO.CitiesListByNamePattern.ListOfCities;
import com.example.konstantin.scrollweather.POJO.ErrorResponse.ErrorResponse;
import com.example.konstantin.scrollweather.POJO.WeatherDailyForecast.DailyForecast;
import com.example.konstantin.scrollweather.POJO.WeatherDay.WeatherDay;
import com.example.konstantin.scrollweather.POJO.WeatherThreeHourForecast.ThreeHourForecast;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Konstantin on 15.08.2017.
 */

public class RESTDataProvider implements IRESTDataProvider {

    private final String TAG = "WeatherApp";

    // запрос подробного прогноза погоды на текущий день
    public void loadWeatherForToday(final DataManager dataManager, WeatherApi.WeatherApiInterface api, int cityID, String lang, String units, String key) {
        Call<WeatherDay> callTodayWeather = api.getToday(cityID, lang, units, key);
        callTodayWeather.enqueue(new Callback<WeatherDay>() {
            @Override
            public void onResponse(@NonNull Call<WeatherDay> call, @NonNull Response<WeatherDay> response) {
                // ответ получен, но необходимо проверить данные на null и прочие несоответствия
                if (response.isSuccessful()) {
                    /// Записываем полученные данные через LocalDataProvider
                    WeatherDay todayWeather = response.body();
                    dataManager.saveDataLocally(todayWeather);
                    dataManager.newForecastUpdate(todayWeather, true);
                } else {
                    handleError(call, response);
                }
            }

            @Override
            public void onFailure(Call<WeatherDay> call, Throwable t) {
                handleOnFailure(call, t);
            }
        });
    }

    // запрос почасового прогноза погоды(интервал 3 часа) на текущий день
    public void loadThreeHourForecast(final DataManager dataManager, WeatherApi.WeatherApiInterface api, int cityID, String lang, String units, int cntIntervals, String key) {
        Call<ThreeHourForecast> callThreeHourForecast = api.getThreeHourForecast(cityID, lang, units, cntIntervals, key);
        callThreeHourForecast.enqueue(new Callback<ThreeHourForecast>() {
            @Override
            public void onResponse(@NonNull Call<ThreeHourForecast> call, @NonNull Response<ThreeHourForecast> response) {
                if (response.isSuccessful()) {
                    ThreeHourForecast threeHourForecast = response.body();
                    dataManager.saveDataLocally(threeHourForecast);
                    dataManager.newForecastUpdate(threeHourForecast, true);
                } else {
                    handleError(call, response);
                }
            }

            @Override
            public void onFailure(Call<ThreeHourForecast> call, Throwable t) {
                handleOnFailure(call, t);
            }
        });
    }

    // запрос прогноза погоды по дням
    public void loadDailyForecast(final DataManager dataManager, WeatherApi.WeatherApiInterface api, int cityID, String lang, String units, int cntDays, String key) {
        Call<DailyForecast> callDailyForecast = api.getDailyForecast(cityID, lang, units, cntDays, key);
        callDailyForecast.enqueue(new Callback<DailyForecast>() {
            @Override
            public void onResponse(@NonNull Call<DailyForecast> call, @NonNull Response<DailyForecast> response) {
                if (response.isSuccessful()) {
                    DailyForecast dailyForecast = response.body();
                    dataManager.saveDataLocally(dailyForecast);
                    dataManager.newForecastUpdate(dailyForecast, true);
                } else {
                    handleError(call, response);
                }
            }

            @Override
            public void onFailure(Call<DailyForecast> call, Throwable t) {
                handleOnFailure(call, t);
            }
        });
    }

    // запрос списка городов, примерно похожих по названию на переданную строку (pattern)
    public ListOfCities loadCitiesListByPattern(WeatherApi.WeatherApiInterface api, String pattern, String lang, String units, String accuracy, int cntCities, String key) {
        ListOfCities citiesList = null;
        Call<ListOfCities> callListOfCities = api.getListByPattern(pattern, lang, units, accuracy, cntCities, key);

        try {
            citiesList = callListOfCities.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Неверный ответ сервера, измените запрос " + e.getMessage());
        }
        return citiesList;
    }

    // обрабатываем response.errorBody(), формирую корректную формулировку сообщения об ощибке
    private void handleError(Call call, Response response) {
        try {
            if (response.errorBody() != null) {
                Gson gson = new Gson();
                ErrorResponse errorResponse = gson.fromJson(response.errorBody().string(), ErrorResponse.class);
                Log.e(TAG, "Сервер возвратил ответ с ошибкой " + errorResponse.getErrCode()
                        + '\n' + errorResponse.getErrMessage()
                        + '\n' + "на запрос " + call.toString());
            }
        } catch (IOException e) {
            Log.e(TAG, "Сервер возвратил ответ с ошибкой.");
            e.printStackTrace();
        }
    }

    // обработка неудачного запроса к серверу
    private void handleOnFailure(Call call, Throwable t) {
        Log.e(TAG, "Ошибка обновления данных с http://api.openweathermap.org"
                + '\n' + t.getMessage()
                + '\n' + "Запрос: " + call.toString());
    }
}
