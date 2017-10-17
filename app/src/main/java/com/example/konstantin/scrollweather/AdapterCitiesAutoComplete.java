package com.example.konstantin.scrollweather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.konstantin.scrollweather.Dagger.DependencyInjector;
import com.example.konstantin.scrollweather.Model.DataManager;
import com.example.konstantin.scrollweather.POJO.CitiesListByNamePattern.City;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;


/**
 * Created by Konstantin on 01.08.2017.
 */

// BaseAdapter implements ListAdapter
public class AdapterCitiesAutoComplete extends BaseAdapter implements Filterable {

    @Inject Context context;
    @Inject DataManager dataManager;

    private ArrayList<City> citiesCompleteMenu;

    public AdapterCitiesAutoComplete() {
        DependencyInjector.getComponent().inject(this);
        citiesCompleteMenu = new ArrayList<>();
    }

    // фильтр для обработки вводимой строки и поиска по массиву городов
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    List<City> cities = getCitiesListByPattern(constraint.toString());
                    // добавляем загруженный с сервера список в FilterResults
                    filterResults.values = cities;
                    filterResults.count = cities.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    citiesCompleteMenu = (ArrayList<City>) results.values;
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }};
        return filter;
    }

    public List<City> getCitiesListByPattern(String pattern) {
        return dataManager.getCitiesListByPattern(pattern);
    }

    // количество найденных городов
    @Override
    public int getCount() {
        return citiesCompleteMenu.size();
    }

    @Override
    public City getItem(int i) {
        return citiesCompleteMenu.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.element_auto_complete_adapter, viewGroup, false);
        }
        City city = getItem(position);
        ((TextView) view.findViewById(R.id.cityNameAdapter)).setText(String.format(Locale.getDefault(), "%s, %s ", city.getCityName(), city.getCountry().getCountryCode()));
        ((TextView) view.findViewById(R.id.cityTempAdapter)).setText(String.format(Locale.getDefault(), "%.1f %s", city.getMainMeasurements().getTemp() , context.getString(R.string.deg_celsius)));
        Picasso.with(context)
                .load(city.getWeatherIconUrl())
                .into((ImageView) view.findViewById(R.id.weatherPicAdapter));
        return view;
    }
}
