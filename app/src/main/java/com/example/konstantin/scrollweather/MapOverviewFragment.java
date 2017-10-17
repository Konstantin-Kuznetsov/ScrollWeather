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
 * Created by Konstantin on 19.09.2017.
 *
 * Фрагмент с обзором погоды на карте.
 */

public class MapOverviewFragment extends Fragment {

    @Inject MapOverviewFragmentPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DependencyInjector.getComponent().inject(this);
        presenter.getGoogleApiClient();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_map_overview, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        presenter.bindViews(); // Butterknife binding
        presenter.initMapAndClusterManager(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.connectGoogleApiClient(); // подключение к клиенту GoogleApi
    }

    @Override
    public void onStop() {
        super.onStop();
        presenter.disconnectGoogleApiClient(); // отключение от клиента GoogleApi
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.attachView(this);
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
