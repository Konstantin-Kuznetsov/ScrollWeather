package com.example.konstantin.scrollweather;

import android.support.annotation.NonNull;

/**
 * Created by Konstantin on 15.08.2017.
 */

public interface IForecastsFragmentPresenter {
    void attachView(@NonNull ForecastsFragment view); //
    void detachView();
}
