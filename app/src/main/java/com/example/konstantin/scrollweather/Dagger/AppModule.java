package com.example.konstantin.scrollweather.Dagger;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

/**
 * Created by Konstantin on 26.08.2017.
 *
 */

@Module
public class AppModule {

    private Context appContext;
    private static final String APP_SETTINGS = "app_settings";

    public AppModule(@NonNull Context context) {
        appContext = context;
    }

    @Provides
    @Singleton
    Context provideContext () {
        return appContext;
    }

    @Provides
    @NonNull
    @Singleton
    public SharedPreferences provideSharedPreferences(Context context) {
        return context.getSharedPreferences(APP_SETTINGS, Context.MODE_PRIVATE);
    }
}
