package com.example.konstantin.scrollweather.Dagger;

import android.app.Application;

/**
 * Created by Konstantin on 27.08.2017.
 */

public class DependencyInjector extends Application {

    private static AppComponent component;

    //private RefWatcher refWatcher; // объект для отслеживания утечек

    public static AppComponent getComponent() {
        return component;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        // библиотека для отследивания утечек
        //if (LeakCanary.isInAnalyzerProcess(this)) {
        //    return;
        //}
        //LeakCanary.install(this);

        component = buildComponent();
    }

    //public static RefWatcher getRefWatcher(Context context) {
    //    DependencyInjector application = (DependencyInjector) context.getApplicationContext();
    //    return application.refWatcher;
    //}



    protected AppComponent buildComponent() {
        return DaggerAppComponent
                .builder()
                .appModule(new AppModule(this.getApplicationContext()))
                .utilsModule(new UtilsModule())
                .dataProvidersModule(new DataProvidersModule())
                .presenterModule(new PresenterModule())
                .uIutilsModule(new UIutilsModule())
                .build();
    }
}
