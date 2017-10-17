package com.example.konstantin.scrollweather.POJO.CitiesListByNamePattern;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Konstantin on 01.08.2017.
 */

public class ListOfCities {
    @SerializedName("cod")
    private int responseCode; // ответ сервера
    @SerializedName("count")
    private int count; // количество найденных городов по паттерну
    @SerializedName("list")
    private List<City> citiesList = null; // краткая информация по городам для выпадающего меню поиска

    public int getResponseCode() {
        return responseCode;
    }

    public int getCount() {
        return count;
    }

    public List<City> getCitiesList() {
        return citiesList;
    }
}
