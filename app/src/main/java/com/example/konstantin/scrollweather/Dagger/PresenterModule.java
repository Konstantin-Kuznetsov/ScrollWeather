package com.example.konstantin.scrollweather.Dagger;

import android.support.annotation.NonNull;

import com.example.konstantin.scrollweather.FindCityFragmentPresenter;
import com.example.konstantin.scrollweather.ForecastsFragmentPresenter;
import com.example.konstantin.scrollweather.MapOverviewFragmentPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Konstantin on 29.08.2017.
 */

@Module
public class PresenterModule {
    @Provides
    @NonNull
    @Singleton
    public ForecastsFragmentPresenter provideForecastFragmentPresenter() {
        return new ForecastsFragmentPresenter();
    }

    @Provides
    @NonNull
    @Singleton
    public FindCityFragmentPresenter provideFindCityFragmentPresenter() {
        return new FindCityFragmentPresenter();
    }

    @Provides
    @NonNull
    @Singleton
    public MapOverviewFragmentPresenter provideMapOverviewFragmentPresenter() {
        return new MapOverviewFragmentPresenter();
    }
}
