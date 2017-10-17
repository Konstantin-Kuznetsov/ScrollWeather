package com.example.konstantin.scrollweather.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.konstantin.scrollweather.Custom_Markers.AbstractMarker;
import com.example.konstantin.scrollweather.Custom_Markers.CityMarker;
import com.example.konstantin.scrollweather.Custom_Markers.MyPositionMarker;
import com.example.konstantin.scrollweather.Dagger.DependencyInjector;
import com.example.konstantin.scrollweather.FindCityFragmentPresenter;
import com.example.konstantin.scrollweather.ForecastsFragmentPresenter;
import com.example.konstantin.scrollweather.MapOverviewFragmentPresenter;
import com.example.konstantin.scrollweather.POJO.BasePOJO;
import com.example.konstantin.scrollweather.POJO.CitiesListAdded.CitiesList;
import com.example.konstantin.scrollweather.POJO.CitiesListAdded.CityInfo;
import com.example.konstantin.scrollweather.POJO.CitiesListByNamePattern.City;
import com.example.konstantin.scrollweather.POJO.CitiesListByNamePattern.ListOfCities;
import com.example.konstantin.scrollweather.POJO.WeatherDailyForecast.DailyForecast;
import com.example.konstantin.scrollweather.POJO.WeatherDay.WeatherDay;
import com.example.konstantin.scrollweather.POJO.WeatherThreeHourForecast.ThreeHourForecast;
import com.example.konstantin.scrollweather.R;
import com.google.gson.Gson;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

/**
 * Created by Konstantin on 15.08.2017.
 *
 * Проверка наличия интернет-соединения и возвращает сохраненные данные локально,
 * либо запращивает новые через API.
 *
 */

public class DataManager implements IBaseData {

    @Inject Context context;
    @Inject WeatherApi.WeatherApiInterface api;
    @Inject SharedPreferences sharedPreferences;
    @Inject Gson gson;
    @Inject RESTDataProvider restDataProvider;
    @Inject LocalDataProvider localDataProvider;

    private ForecastsFragmentPresenter forecastsFragmentPresenter;
    private FindCityFragmentPresenter findCityFragmentPresenter;
    private MapOverviewFragmentPresenter mapOverviewFragmentPresenter;
    private String key;

    private final String TAG = "WeatherApp";
    private static final String LAST_APP_VERSION = "last_app_version";

    // актуальный список добавленных пользователем городов
    private CitiesList citiesList;
    private int currentCityNum = 0;

    // базовые настройки для запроса
    private String langSetting;
    private String unitsSetting;
    private String accuracySetting;
    private int cntDaysSetting;
    private int cntThreeHourIntervalSetting;

    public DataManager() {
        // инъекция зависимостей с помощью Dagger2
        DependencyInjector.getComponent().inject(this);

        key = context.getString(R.string.openweathermap_key);

        // загрузка пользовательских настроек
        reloadSettings();

        // загрузка сохраненных городов
        reloadCitiesList();
    }

    public void setForecastsFragmentPresenter(ForecastsFragmentPresenter forecastsFragmentPresenter) {
        // сохранение ссылки на презентер для обратной связи с презентером отображения прогнозов
        this.forecastsFragmentPresenter = forecastsFragmentPresenter;
    }

    public void setFindCityFragmentPresenter(FindCityFragmentPresenter findCityFragmentPresenter) {
        // сохранение ссылки на презентер для обратной связи с презентером фпагмента поиска городов
        this.findCityFragmentPresenter = findCityFragmentPresenter;
    }

    public void setMapOverviewFragmentPresenter(MapOverviewFragmentPresenter mapOverviewFragmentPresenter) {
        // сохранение ссылки на презентер для обратной связи с презентером фпагмента с Google map
        this.mapOverviewFragmentPresenter = mapOverviewFragmentPresenter;
    }


    // считываем настройки при первичной инициализации DataManager и при изменении настроек
    public void reloadSettings() {
        langSetting = localDataProvider.getLangSetting(sharedPreferences);
        unitsSetting = localDataProvider.getUnitsSetting(sharedPreferences);
        cntDaysSetting = localDataProvider.getCntDaysSetting(sharedPreferences);
        cntThreeHourIntervalSetting = localDataProvider.getCntThreeHourIntervalSetting(sharedPreferences);
        accuracySetting = localDataProvider.getAccuracySetting(sharedPreferences);
    }

    // возвращает знак(C, F или K) для отображения температуры
    public String getTempSign() {
        String tempSign = "";
        switch (unitsSetting) {
            case "metric":
                tempSign = context.getString(R.string.deg_celsius);
                break;
            case "imperial":
                tempSign =  context.getString(R.string.deg_fahrenheit);
                break;
            case "standard":
                tempSign =  String.format(String.format(Locale.getDefault(), "%s%s", context.getString(R.string.deg_sign), context.getString(R.string.deg_kelvin)));
                break;
            default:
                return tempSign;
        }
        return tempSign;
    }

    // загрузка списка сохраненных городов
    public CitiesList getCitiesList() {
        return localDataProvider.getCitiesList(sharedPreferences, gson);
    }

    // обновление UI после загрузки списка городов
    public void reloadCitiesList() {
        citiesList = getCitiesList();
        if (citiesList != null && findCityFragmentPresenter != null) {
            findCityFragmentPresenter.updateUI(citiesList);
        }

    }

    // перезапись списка сохраненных городов
    public void updateCitiesList(CitiesList citiesList) {
        localDataProvider.saveCitiesListLocally(citiesList, sharedPreferences, gson);
    }

    public CityInfo getFirstCity() {
        if (citiesList != null && !citiesList.getCitiesList().isEmpty()) {
            currentCityNum = 0;
            return citiesList.getCitiesList().get(0);
        } else return null;
    }

    public CityInfo getNextCity() {
        if (citiesList != null && !citiesList.getCitiesList().isEmpty()) {
            int itemsToEnd = (citiesList.getCitiesList().size() -1) - currentCityNum;
            // возвращаем следующий элемент
            if (itemsToEnd >1) {
                currentCityNum++;
                return citiesList.getCitiesList().get(currentCityNum + 1);
            } else {
                // возвращаем первый(нулевой)
                currentCityNum = 0;
                return citiesList.getCitiesList().get(0);
            }
            // нет элементов в списке
        } else return null;
    }

    public CityInfo getPreviousCity() {
        if (citiesList != null && !citiesList.getCitiesList().isEmpty()) {
            // возвращаем предыдущий элемент
            if (currentCityNum >=1) {
                currentCityNum--;
                return citiesList.getCitiesList().get(currentCityNum + 1);
            } else {
                // возвращаем последний
                currentCityNum = citiesList.getCitiesList().size() -1;
                return citiesList.getCitiesList().get(currentCityNum);
            }
            // нет элементов в списке
        } else return null;
    }


    @Override
    public void getWeatherForToday(int cityID) {
        if (isInternetAvailable()) {
            // перенаправляем запрос погоды RESTDataProvider
            restDataProvider.loadWeatherForToday(
                    this,
                    api,
                    cityID,
                    langSetting,
                    unitsSetting,
                    key);
        } else {
            // перенаправляем запрос погоды LocalDataProvider для чтения последних сохраненных данных
            WeatherDay weatherDay = (WeatherDay)localDataProvider.loadWeatherForToday(sharedPreferences, gson, cityID);
            if (weatherDay != null) {
                newForecastUpdate(weatherDay, false);
            } else {
                // данные еще не загружались или нелоступны локально
                forecastsFragmentPresenter.updateUI(null, false);
            }
        }
    }

    @Override
    public void getThreeHourForecast(int cityID) {
        if (isInternetAvailable()) {
            restDataProvider.loadThreeHourForecast(
                    this,
                    api,
                    cityID,
                    langSetting,
                    unitsSetting,
                    cntThreeHourIntervalSetting,
                    key);
        } else {
            ThreeHourForecast threeHourForecast = (ThreeHourForecast) localDataProvider.loadThreeHourForecast(sharedPreferences, gson, cityID);
            if (threeHourForecast != null) {
                newForecastUpdate(threeHourForecast, false);
            } else {
                forecastsFragmentPresenter.updateUI(null, false);
            }
        }
    }

    @Override
    public void getDailyForecast(int cityID) {
        if (isInternetAvailable()) {
            restDataProvider.loadDailyForecast(
                    this,
                    api,
                    cityID,
                    langSetting,
                    unitsSetting,
                    cntDaysSetting,
                    key);

        } else {
            DailyForecast dailyForecast = (DailyForecast) localDataProvider.loadDailyForecast(sharedPreferences, gson, cityID);
            if (dailyForecast != null) {
                newForecastUpdate(dailyForecast, false);
            } else {
                forecastsFragmentPresenter.updateUI(null, false);
            }
        }
    }

    public List<City> getCitiesListByPattern(String pattern) {
        ListOfCities citiesList = restDataProvider.loadCitiesListByPattern(
                api,
                pattern,
                langSetting,
                unitsSetting,
                accuracySetting,
                10, // максимальное количество городов в возвращаемой выборке
                key
        );

        if (citiesList != null) {
            return citiesList.getCitiesList();
        }

        return null;
    }

    // подгрузка данных для маркера по ID города, либо текущему местоположению
    @Override
    public WeatherDay getDataForMarker(AbstractMarker marker) {

        if (marker instanceof MyPositionMarker) {
//            TODO сделать загрузку по текущему местоположению
//            if (isInternetAvailable()) {
//                LatLng position = marker.getPosition();
//                //weather = restDataProvider.loadDataForMarkerByPosition(position);
//            }
        } else {
            // загрузка данных по ID города, относящегося к маркеру
            CityMarker cityMarker = (CityMarker) marker;
            int cityID = cityMarker.getCityID();
            return (WeatherDay) localDataProvider.loadWeatherForToday(sharedPreferences, gson, cityID);
        }
        return null;
    }

    // Записываем обновленные данные прогноза, полученные от RESTDataProvider
    public void saveDataLocally(BasePOJO pojoObject) {
        //POJO Object записываем в Shared Preferences
        localDataProvider.saveDataLocally(sharedPreferences, gson, pojoObject);
    }

    // вызывается по окончании асинхронного запроса в сеть, сделать Callback
    public void newForecastUpdate(BasePOJO pojo, boolean isUpToDate) {
        forecastsFragmentPresenter.updateUI(pojo, isUpToDate);
    }

    public void forecastUpdateUnavailable() {
        // новые данные недоступны
    }

    public void checkAppStartStatus() {
        switch (getAppStartStatus()) {
            case NORMAL:
                // обычный режим старта приложения
                break;
            case FIRST_TIME_NEW_VERSION:
                // показать какую-то информацию про обновление
                break;
            case FIRST_TIME:
                // показать экран с выбором городов для отслеживания погоды
                // записать значения по умолчанию при необходимости
                break;

            //
            // можно сделать режим не первый раз, но нет городов в списке отслеживаемых
            //City
        }
    }

    // Проверка статуса запуска приложения - первый раз, первый раз после обновления, обычный запуск
    private AppStartStatus getAppStartStatus() {

        // значение по умолчанию, будет возвращено в случае ошибки чтения текущей версии из PackageManager
        AppStartStatus appStartStatus = AppStartStatus.NORMAL;

        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            int lastCheckedVersionCode = sharedPreferences.getInt(LAST_APP_VERSION, -1);
            // корректные versionCode(int) versionName(String) нужно прописать в build.gradle(app)
            int currentVersionCode = pInfo.versionCode;

            if (lastCheckedVersionCode == -1) {
                return AppStartStatus.FIRST_TIME;
            } else if (lastCheckedVersionCode < currentVersionCode) {
                return AppStartStatus.FIRST_TIME_NEW_VERSION;
            } else if (lastCheckedVersionCode < currentVersionCode) { // понижение версии, запускаем с предупреждением
                Log.w(TAG, "Текущая версия программы (" + currentVersionCode
                        + ") ниже, чем была при последнем запуске ("
                        + lastCheckedVersionCode
                        + "). Режим запуска - NORMAL.");
                return AppStartStatus.NORMAL;
            } else {
                return AppStartStatus.NORMAL;
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Невозможно корректно определить установленную версию приложения. Задействован режим по умолчанию - NORMAL");
        }
        return appStartStatus;
    }

    private boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
