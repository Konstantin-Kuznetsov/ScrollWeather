package com.example.konstantin.scrollweather.POJO.CitiesListAdded;

import java.util.ArrayList;

/**
 * Created by Konstantin on 20.09.2017.
 *
 * Основные данные для представления списка городов, добавленных пользователем
 * в список отслеживаемых.
 */

public class CitiesList {
    private ArrayList<CityInfo> citiesList = new ArrayList<>();

    public CitiesList(ArrayList<CityInfo> citiesList) {
        this.citiesList = citiesList;
    }

    public ArrayList<CityInfo> getCitiesList() {
        return citiesList;
    }

    public void setCitiesList(ArrayList<CityInfo> citiesList) {
        this.citiesList = citiesList;
    }

}
