package com.example.konstantin.scrollweather;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.konstantin.scrollweather.Dagger.DependencyInjector;
import com.example.konstantin.scrollweather.POJO.CitiesListAdded.CitiesList;
import com.example.konstantin.scrollweather.POJO.CitiesListAdded.CityInfo;
import com.example.konstantin.scrollweather.POJO.CitiesListByNamePattern.City;


import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by Konstantin on 21.09.2017.
 */

public class AdapterAddedCities extends RecyclerView.Adapter<AdapterAddedCities.ViewHolder> {

    @Inject Context context;
    @Inject FindCityFragmentPresenter presenter;
    private ArrayList<CityInfo> citiesData = new ArrayList<>();

    public AdapterAddedCities() {
        DependencyInjector.getComponent().inject(this);
    }

    // передаем массив с актуальными данными о погоде
    public void setOrUpdateDataset ( ArrayList<CityInfo> cities) {
        citiesData = cities;
    }

    // добавление в адаптер нового города
    public void addCityInList(City city) {
        citiesData.add(
                new CityInfo(city.getCityId(),
                city.getCityName(),
                city.getCountry().getCountryCode(),
                city.getCoord().getLat(),
                city.getCoord().getLon()));

        notifyItemInserted(citiesData.size() - 1);
    }

    // удаление элемента под указанным номером из списка (реализация метода из ItemTouchHelperAdapter)
    // и обновление UI в соответствии с произошедшим событием
    public void onItemDismiss(int position) {
        citiesData.remove(position);
        notifyItemRemoved(position);

        if (getItemCount() == 0) {
            // оповестить пользователя об отсутствии элементов
        }

        //presenter.saveCitiesListAndRefreshUI(getCitiesList());
    }

    // перемещение элемента с fromPosition в toPosition (реализация метода из ItemTouchHelperAdapter)
    // и обновление UI в соответствии с произошедшим событием
    public void onItemMove(int fromPosition, int toPosition) {
        CityInfo tmp = citiesData.remove(fromPosition);
        citiesData.add(toPosition > fromPosition ? toPosition - 1 : toPosition, tmp);
        notifyItemMoved(fromPosition, toPosition);

        //presenter.saveCitiesListAndRefreshUI(getCitiesList());
    }

    public CitiesList getCitiesList() {
        return new CitiesList(citiesData);
    }

    @Override
    public AdapterAddedCities.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_cities_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterAddedCities.ViewHolder holder, int position) {
        holder.cityNameCountrycode.setText(
                citiesData.get(position).getCityName() +
                ", " +
                citiesData.get(position).getCountryCode());
    }

    @Override
    public int getItemCount() {
        return citiesData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView cityNameCountrycode;

        public ViewHolder(View itemView) {
            super(itemView);
            cityNameCountrycode = itemView.findViewById(R.id.cityNameCountrycode);
        }

//        public void onItemSelected() {
//            itemView.setBackgroundColor(Color.LTGRAY);
//        }
//
//        public void onItemClear() {
//            itemView.setBackgroundColor(0);
//        }

    }
}
