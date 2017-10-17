package com.example.konstantin.scrollweather;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.konstantin.scrollweather.Dagger.DependencyInjector;

import javax.inject.Inject;

/**
 * Created by Konstantin on 12.08.2017.
 */

public class ForecastsFragment extends Fragment implements IForecastsFragment {

    @Inject ForecastsFragmentPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forecasts, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DependencyInjector.getComponent().inject(this);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        // фрагмент готов, можно получать ссылки на элементы и использовать в логике работы
        // приложения (обновить все погодные данные)
        presenter.attachView(this);
        presenter.bindViews(); // Butterknife binding
        presenter.configureRecyclerViews();
        presenter.updateForecastsForCityByID(CitiesNavTypes.GET_FIRST_CITY);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.attachView(this);
        presenter.configureRecyclerViews();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.detachView();
    }

    @Override
    public void onDestroyView() {
        presenter.unbindViews(); // // Butterknife unbinding
        super.onDestroyView();
    }
}
