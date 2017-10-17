package com.example.konstantin.scrollweather.Dagger;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;

import com.example.konstantin.scrollweather.AdapterCitiesAutoComplete;
import com.example.konstantin.scrollweather.HorizontalStartSnapHelper;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Konstantin on 06.09.2017.
 */

@Module
public class UIutilsModule {
    @Provides
    @NonNull
    @Singleton
    @Named("llmThreeHourForecast")
    public LinearLayoutManager providellmThreeHourForecast(Context context) {
        return new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
    }

    @Provides
    @NonNull
    @Singleton
    @Named("llmDailyForecast")
    public LinearLayoutManager providellmDailyForecast(Context context) {
        return new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
    }

    @Provides
    @NonNull
    @Singleton
    @Named("llmAddedCities")
    public LinearLayoutManager providellmAddedCities(Context context) {
        return new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
    }

    @Provides
    @NonNull
    @Singleton
    @Named("shThreeHourForecast")
    public HorizontalStartSnapHelper provideThreeHourSnapHelper() {
        return new HorizontalStartSnapHelper();
    }

    @Provides
    @NonNull
    @Singleton
    @Named("shDailyForecast")
    public HorizontalStartSnapHelper provideDailyForecastSnapHelper() {
        return new HorizontalStartSnapHelper();
    }


    @Provides
    @NonNull
    @Singleton
    public AdapterCitiesAutoComplete provideCitiesAutoCompleteAdapter() {
        return new AdapterCitiesAutoComplete();
    }

//    @Provides
//    @NonNull
//    @Singleton
//    public AdapterThreeHourForecast provideAdapterThreeHourForecast() {
//        return new AdapterThreeHourForecast();
//    }
//
//    @Provides
//    @NonNull
//    @Singleton
//    public AdapterDailyForecast provideAdapterDailyForecast() {
//        return new AdapterDailyForecast();
//    }

}
