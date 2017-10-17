package com.example.konstantin.scrollweather;

import android.support.annotation.NonNull;

/**
 * Created by Konstantin on 15.09.2017.
 */

public interface IFindCityFragmentPresenter {
    void attachView(@NonNull FindCityFragment view);
    void detachView();
}
