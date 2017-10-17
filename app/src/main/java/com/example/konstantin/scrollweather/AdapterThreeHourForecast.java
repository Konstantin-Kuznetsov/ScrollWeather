package com.example.konstantin.scrollweather;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.konstantin.scrollweather.Dagger.DependencyInjector;
import com.example.konstantin.scrollweather.POJO.WeatherThreeHourForecast.WeatherThreeHourInterval;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * Created by Konstantin on 29.08.2017.
 *
 * Адаптер для RecyclerView с трехчасовым прогнозом
 */

public class AdapterThreeHourForecast extends RecyclerView.Adapter<AdapterThreeHourForecast.ViewHolder> {

    @Inject Context context;
    private ArrayList<WeatherThreeHourInterval> weatherData;
    private final String TAG = "WeatherApp";


    public AdapterThreeHourForecast() {
        DependencyInjector.getComponent().inject(this);
    }

    // передаем массив с актуальными данными о погоде
    public void setOrUpdateDataset (@NonNull ArrayList<WeatherThreeHourInterval> weatherData) {
        this.weatherData = weatherData;
        Log.i(TAG, "Offset to UTC is " + String.valueOf(TimeZone.getDefault().getRawOffset()/(1000*60*60)) + " hours");
    }

    // создание "хранилица" для элемента с данными
    @Override
    public AdapterThreeHourForecast.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_hours_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterThreeHourForecast.ViewHolder holder, int position) {

        // подгрузка иконки с типом погоды
        Picasso.with(context)
                .load(weatherData.get(position).getWeatherIconUrl())
                .into(holder.imageWeather);

        // время, на которое построен данный прогноз. Данные приводятся к локальной TimeZone
        // и форматируются
        Date dateForecast = new Date(weatherData.get(position).getDt()*1000);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM HH:mm", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());

        // Строка с временем, на которое показан прогноз и целое число часов от текущего времени до
        // времени этого прогноза
        StringBuilder sb = new StringBuilder();
        sb.append(sdf.format(dateForecast))
                .append(" (+")
                .append(TimeUnit.MILLISECONDS.toHours(dateForecast.getTime() - (new Date()).getTime()))
                .append(context.getString(R.string.hour_sign))
                .append(")");

        holder.timeOfForecast.setText(sb.toString());



        // температура
        holder.tempOfForecast.setText(String.format(Locale.getDefault(), "%.0f%s", weatherData.get(position).getMainMeasurements().getTemp(), context.getString(R.string.deg_sign)));
    }

    @Override
    public int getItemCount() {
        return weatherData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView timeOfForecast;
        private TextView tempOfForecast;
        private ImageView imageWeather;

        public ViewHolder(View itemView) {
            super(itemView);
            timeOfForecast = itemView.findViewById(R.id.timeHoursForecast);
            tempOfForecast = itemView.findViewById(R.id.tempHoursForecast);
            imageWeather = itemView.findViewById(R.id.picWeatherTypeHourly);
        }
    }
}
