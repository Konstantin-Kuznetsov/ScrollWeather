package com.example.konstantin.scrollweather;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.konstantin.scrollweather.Dagger.DependencyInjector;
import com.example.konstantin.scrollweather.Model.DataManager;

import javax.inject.Inject;

/**
 * Created by Konstantin on 06.10.2017.
 *
 * Экран настроек приложения.
 */

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject DataManager dataManager;
    private static final String APP_SETTINGS = "app_settings";
    private final String TAG = "WeatherApp";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DependencyInjector.getComponent().inject(this);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // установка необходимого файла настроек(по умолчанию все пишет в default)
        getPreferenceManager().setSharedPreferencesName(APP_SETTINGS);
        getPreferenceManager().setSharedPreferencesMode(Context.MODE_PRIVATE);

        // загрузка списка настроек из файла
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        // обновление текущих настроек для корректного отображения данных
        dataManager.reloadSettings();

        String value = findPreference(key).toString();

        if (getView() != null) {
            Snackbar snackbar = Snackbar.make(getView(),"Настроечный параметр изменен:" + '\n' + value, Snackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            TextView tv= snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            tv.setMaxLines(2);
            snackbar.show();
        }

        Log.i(TAG, "Параметр " + key + " был изменен." + '\n' + value);
    }
}
