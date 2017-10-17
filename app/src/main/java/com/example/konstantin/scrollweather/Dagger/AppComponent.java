package com.example.konstantin.scrollweather.Dagger;

import com.example.konstantin.scrollweather.AdapterAddedCities;
import com.example.konstantin.scrollweather.AdapterDailyForecast;
import com.example.konstantin.scrollweather.AdapterThreeHourForecast;
import com.example.konstantin.scrollweather.AdapterCitiesAutoComplete;
import com.example.konstantin.scrollweather.FindCityFragment;
import com.example.konstantin.scrollweather.FindCityFragmentPresenter;
import com.example.konstantin.scrollweather.ForecastsFragment;
import com.example.konstantin.scrollweather.ForecastsFragmentPresenter;
import com.example.konstantin.scrollweather.MainActivity;
import com.example.konstantin.scrollweather.MapOverviewFragment;
import com.example.konstantin.scrollweather.MapOverviewFragmentPresenter;
import com.example.konstantin.scrollweather.Model.DataManager;
import com.example.konstantin.scrollweather.SettingsFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Konstantin on 26.08.2017.
 */
@Singleton
@Component (modules = {AppModule.class, UtilsModule.class, DataProvidersModule.class, PresenterModule.class, UIutilsModule.class})
public interface AppComponent {
    // в интерфейсе указывается куда конкретно вставлять сгенерированный библиотекой код
    void inject(DataManager dataManager);

    void inject(ForecastsFragment forecastsFragment);
    void inject(MainActivity mainActivity);
    void inject(FindCityFragment findCityFragment);
    void inject(MapOverviewFragment mapOverviewFragment);
    void inject(SettingsFragment settingsFragment);

    void inject(AdapterCitiesAutoComplete adapterCitiesAutoComplete);
    void inject(AdapterAddedCities adapterAddedCities);
    void inject(AdapterThreeHourForecast adapterThreeHourForecast);
    void inject(AdapterDailyForecast adapterDailyForecast);

    void inject(ForecastsFragmentPresenter forecastsFragmentPresenter);
    void inject(FindCityFragmentPresenter findCityFragmentPresenter);
    void inject(MapOverviewFragmentPresenter mapOverviewFragmentPresenter);


}
