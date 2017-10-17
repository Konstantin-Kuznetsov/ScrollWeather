package com.example.konstantin.scrollweather.Dagger;

import android.support.annotation.NonNull;
import android.view.View;

import com.example.konstantin.scrollweather.Model.WeatherApi;
import com.google.gson.Gson;

import javax.inject.Singleton;

import butterknife.ButterKnife;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Konstantin on 26.08.2017.
 */

@Module
public class UtilsModule {

    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";

    @Provides
    @NonNull
    @Singleton
    public Gson provideGson() {
        return new Gson();
    }

    @Provides
    @NonNull
    @Singleton
    public Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @NonNull
    @Singleton
    public WeatherApi.WeatherApiInterface provideWeatherApi(Retrofit retrofitInstance) {
        return retrofitInstance.create(WeatherApi.WeatherApiInterface.class);
    }

    @Provides
    @NonNull
    @Singleton
    public ButterKnife.Setter provideVisibleSetter() {
        return new ButterKnife.Setter<View, Integer>() {
            @Override
            public void set(@NonNull View view, Integer value, int index) {
                view.setVisibility(value);
            }
        };
    }
}
