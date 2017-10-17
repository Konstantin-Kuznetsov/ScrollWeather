package com.example.konstantin.scrollweather;

import android.support.annotation.NonNull;

/**
 * Created by Konstantin on 19.09.2017.
 */

public interface IMapOverviewFragmentPresenter {
    void attachView(@NonNull MapOverviewFragment view);
    void detachView();
}
