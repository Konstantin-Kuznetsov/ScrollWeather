package com.example.konstantin.scrollweather.Dagger;

import android.support.annotation.NonNull;

import com.example.konstantin.scrollweather.Model.DataManager;
import com.example.konstantin.scrollweather.Model.LocalDataProvider;
import com.example.konstantin.scrollweather.Model.RESTDataProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Konstantin on 28.08.2017.
 */

@Module
public class DataProvidersModule {
    @Provides
    @NonNull
    @Singleton
    public LocalDataProvider provideLocalDataProvider() {
        return new LocalDataProvider();
    }

    @Provides
    @NonNull
    @Singleton
    public RESTDataProvider provideRestDataProvider() {
        return new RESTDataProvider();
    }

    @Provides
    @NonNull
    @Singleton
    public DataManager provideDataManager() {
        return new DataManager();
    }
}
