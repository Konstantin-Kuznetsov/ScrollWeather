package com.example.konstantin.scrollweather;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.example.konstantin.scrollweather.Dagger.DependencyInjector;
import com.example.konstantin.scrollweather.Model.DataManager;
import com.example.konstantin.scrollweather.POJO.CitiesListAdded.CitiesList;
import com.example.konstantin.scrollweather.POJO.CitiesListByNamePattern.City;

import java.lang.ref.WeakReference;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Konstantin on 15.09.2017.
 */

public class FindCityFragmentPresenter implements IFindCityFragmentPresenter {


    @Nullable @BindView(R.id.requestedCity) DelayAutoCompleteTextView requestedCity;
    @Nullable @BindView(R.id.progressBarFindCity) ProgressBar progressBar;
    @Nullable @BindView(R.id.recyclerCitiesList) RecyclerView addedCitiesRecycler;

    @Inject DataManager dataManager;
    @Inject AdapterCitiesAutoComplete autoCompleteAdapter;
    @Inject @Named("llmAddedCities") LinearLayoutManager llmAddedCities;

    private WeakReference<FindCityFragment> bindedView;
    private Unbinder viewsUnbinder;
    private ItemTouchHelper itemTouchHelper;

    // адаптер для RecyclerView
    private AdapterAddedCities adapterAddedCities;

    public FindCityFragmentPresenter() {
        DependencyInjector.getComponent().inject(this);
        dataManager.setFindCityFragmentPresenter(this);
    }

    // Прикрепление и открепление активити в зависимости от ЖЦ
    @Override
    public void attachView(@NonNull FindCityFragment view) {
        bindedView = new WeakReference<>(view);
    }

    @Override
    public void detachView() {
        bindedView = null;

        // Сделать объекты живущими пока живет фрагмент
        adapterAddedCities = null;
        addedCitiesRecycler.setLayoutManager(null);
        addedCitiesRecycler = null;
    }

    // конфигурирование RecyclerViev (удаление по свайпу и перетаскивание элементов)
    public void configureRecyclerViews() {
        addedCitiesRecycler.setLayoutManager(llmAddedCities);
        adapterAddedCities = new AdapterAddedCities();

        //----------
        //
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.START | ItemTouchHelper.END) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                adapterAddedCities.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                adapterAddedCities.onItemDismiss(viewHolder.getAdapterPosition());
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    AdapterAddedCities.ViewHolder holder = (AdapterAddedCities.ViewHolder) viewHolder;
                    holder.itemView.setBackgroundColor(Color.LTGRAY);
                }
                super.onSelectedChanged(viewHolder, actionState);
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                AdapterAddedCities.ViewHolder holder = (AdapterAddedCities.ViewHolder) viewHolder;
                holder.itemView.setBackgroundColor(0);
            }
        };

        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(addedCitiesRecycler);
        //
        //----------

        // подгрузка данных в список
        updateCitiesListData();

        // устанавливаем адаптер на поле ввода
        requestedCity.setAdapter(autoCompleteAdapter);
        // устанавливаем индикатор процесса загрузки подсказок
        requestedCity.setLoadingIndicator(progressBar);
        // по выбору подсказки - заносим в строку поиска выбранный город
        requestedCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                City city = (City) adapterView.getItemAtPosition(position);

                // Установка имени выбранного города тексту строки поиска
                requestedCity.setText(String.format(Locale.getDefault(), "%s, %s", city.getCityName(), city.getCountry().getCountryCode()));

                // Добавление выбранного в выпавшем списке города в адаптер AdapterAddedCities
                adapterAddedCities.addCityInList(city);

                // Загрузка данных о погоде и сохранение в shared preferences
                dataManager.getWeatherForToday(city.getCityId());

                // Сохранение обновленного списка
                dataManager.updateCitiesList(adapterAddedCities.getCitiesList());
            }
        });
    }

    public void bindViews() {
        if (bindedView != null) {
            viewsUnbinder = ButterKnife.bind(this, bindedView.get().getView());
        }
    }

    public void unbindViews() {
        if (viewsUnbinder != null) {
            viewsUnbinder.unbind();
        }
    }

    // запрос списка городов от dataManager для обновления UI
    public void updateCitiesListData() {
        dataManager.reloadCitiesList();
    }

    public void updateUI(CitiesList cities) {
        // обновление RecyclerView
        if (bindedView == null) return;
        if (cities.getCitiesList()!=null && !cities.getCitiesList().isEmpty()) {
            adapterAddedCities.setOrUpdateDataset(cities.getCitiesList());
            addedCitiesRecycler.setAdapter(adapterAddedCities);
        } else {
            // TODO пустой список городов, информируем пользователя
        }

    }
}

