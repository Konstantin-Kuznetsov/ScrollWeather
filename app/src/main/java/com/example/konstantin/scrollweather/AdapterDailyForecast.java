package com.example.konstantin.scrollweather;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.konstantin.scrollweather.Dagger.DependencyInjector;
import com.example.konstantin.scrollweather.POJO.WeatherDailyForecast.OneDayForecast;
import com.squareup.picasso.Picasso;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

/**
 * Created by Konstantin on 05.09.2017.
 *
 * Адаптер для RecyclerView с прогнозом по дням
 */

public class AdapterDailyForecast extends RecyclerView.Adapter<AdapterDailyForecast.ViewHolder> {

    @Inject Context context;
    private ArrayList<OneDayForecast> weatherData;
    private OnItemClickListener listener;
    private static View viewSelected;

    public interface OnItemClickListener { // интерфейс листенера
        void onItemClick(OneDayForecast item, View selectedView);
    }

    // при восстановлении(прикреплении) View к RecyclerView, проверяется, является ли
    // этот View выбранным.
    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (holder.itemView != viewSelected) holder.itemView.setBackgroundColor(Color.TRANSPARENT);
    }

    public AdapterDailyForecast(OnItemClickListener listener) { // в конструктор передаем listener
        this.listener = listener;
        DependencyInjector.getComponent().inject(this);
    }

    // передаем массив с актуальными данными о погоде
    public void setOrUpdateDataset (@NonNull ArrayList<OneDayForecast> weatherData) {
        this.weatherData = weatherData;
    }

    public OneDayForecast getFirstElementData() {
        return weatherData.get(0);
    }

    // создание "хранилица" для элемента с данными
    @Override
    public AdapterDailyForecast.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_days_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterDailyForecast.ViewHolder holder, int position) {

        holder.bind(weatherData.get(position), listener, position);

        // подгрузка иконки с типом погоды
        Picasso.with(context)
                .load(weatherData.get(position).getWeatherIconUrl())
                .into(holder.imageWeather);

        // время, на которое построен данный прогноз
        holder.timeOfForecast.setText(convertTime(weatherData.get(position).getDt()));

        // температура мин и макс
        holder.tempMin.setText(String.format(Locale.getDefault(), "%.0f%s", weatherData.get(position).getTemp().getMinTemp(), context.getString(R.string.deg_sign)));
        holder.tempMax.setText(String.format(Locale.getDefault(), "%.0f%s", weatherData.get(position).getTemp().getMaxTemp(), context.getString(R.string.deg_sign)));

        // сразу выбираем первый элемент для показа подробного прогноза на текущий день
        if (position == 0) {
            listener.onItemClick(weatherData.get(position), holder.itemView);
            viewSelected = holder.itemView;
        }
    }

    @Override
    public int getItemCount() {
        return weatherData.size();
    }

    // холдер реализует OnClickListener для отслеживания выбора конкретного элемента в смиске
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView timeOfForecast;
        private TextView tempMin;
        private TextView tempMax;
        private ImageView imageWeather;


        public ViewHolder(View itemView) {
            super(itemView);

            timeOfForecast = itemView.findViewById(R.id.daysItemDT);
            tempMin = itemView.findViewById(R.id.daysItemTempMin);
            tempMax = itemView.findViewById(R.id.daysItemTempMax);
            imageWeather = itemView.findViewById(R.id.daysItemWeatherImage);
        }

        // обработка нажатия на элемент списка
        public void bind(final OneDayForecast item, final OnItemClickListener listener, final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewSelected = view;
                    listener.onItemClick(item, view);
                }
            });
        }
    }

    // Data receiving time (in unix, UTC format).
    // dt is the time of data receiving in unixtime GMT (greenwich mean time).
    private String convertTime(long time){
        Date date = new Date(time*1000);
        Format sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return sdf.format(date);
    }
}
