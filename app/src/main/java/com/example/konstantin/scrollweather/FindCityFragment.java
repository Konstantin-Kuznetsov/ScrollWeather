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
 * Created by Konstantin on 15.09.2017.
 *
 * Фрагмент редактирования списка городов (добавление/удаление)
 */

public class FindCityFragment extends Fragment {

    @Inject FindCityFragmentPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_city, container, false);

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
        presenter.attachView(this);
        presenter.bindViews(); // Butterknife binding
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.attachView(this); // перенести прикрепление фрагмента к презентеру
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
