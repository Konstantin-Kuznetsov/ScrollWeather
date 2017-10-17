package com.example.konstantin.scrollweather;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.konstantin.scrollweather.Dagger.DependencyInjector;
import com.example.konstantin.scrollweather.Model.DataManager;
import com.example.konstantin.scrollweather.POJO.BasePOJO;
import com.example.konstantin.scrollweather.POJO.CitiesListAdded.CityInfo;
import com.example.konstantin.scrollweather.POJO.WeatherDailyForecast.OneDayForecast;
import com.example.konstantin.scrollweather.POJO.WeatherDay.WeatherDay;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import butterknife.Unbinder;


/**
 * Created by Konstantin on 13.08.2017.
 *
 * Основной фрагмент со всеми данными из прогноза.
 */

public class ForecastsFragmentPresenter implements IForecastsFragmentPresenter  {

    private WeakReference<ForecastsFragment> bindedView;
    private Unbinder viewsUnbinder;

    // RecyclerView с трехчасовым прогнозом и прогнозом по дням
    @Nullable @BindView(R.id.horizontalRecyclerViewHours) RecyclerView threeHourForecastRecycler;
    @Nullable @BindView(R.id.horizontalRecyclerViewDays) RecyclerView dailyForecastRecycler;

    // View с подробной информацией по выбранному дню из Days Forecast
    @Nullable @BindView(R.id.valueHumidityDay) TextView dayHumidity;
    @Nullable @BindView(R.id.valuePressureDay) TextView dayPressure;
    @Nullable @BindView(R.id.valueCloudinessDay) TextView dayCloudiness;
    @Nullable @BindView(R.id.valueWindSpeedDay) TextView dayWindspeed;
    @Nullable @BindView(R.id.valueTempMorn) TextView dayTempMorn;
    @Nullable @BindView(R.id.valueTempDay) TextView dayTempDay;
    @Nullable @BindView(R.id.valueTempEvening) TextView dayTempEvening;
    @Nullable @BindView(R.id.valueTempNight) TextView dayTempNight;

    // View обозначающие тип данных (ImageView обозначающие давление, указание Утро-День-Вечер... и т.д)
    @Nullable @BindView(R.id.imagePressureDay) ImageView imagePressureDay;
    @Nullable @BindView(R.id.imageHumidityDay) ImageView imageHumidityDay;
    @Nullable @BindView(R.id.imageCloudinessDay) ImageView imageCloudinessDay;
    @Nullable @BindView(R.id.imageWindSpeedDay) ImageView imageWindSpeedDay;
    @Nullable @BindView(R.id.textViewMorning) TextView textViewMorning;
    @Nullable @BindView(R.id.textViewDay) TextView textViewDay;
    @Nullable @BindView(R.id.textViewEvening) TextView textViewEvening;
    @Nullable @BindView(R.id.textViewNight) TextView textViewNight;

    // Текушие показания
    @Nullable @BindView(R.id.imageCurrWeather) ImageView imageCurrWeather;
    @Nullable @BindView(R.id.valueCurrTemp) TextView currTemp;
    @Nullable @BindView(R.id.valueCurrCity) TextView currCity;
    @Nullable @BindView(R.id.valueCurrDate) TextView currDate;

    // Кнопки навигации по добавленным городам
    @Nullable @BindView(R.id.previousCityButton) ImageButton loadPreviousCity;
    @Optional @OnClick(R.id.previousCityButton) void onClickPrev() {
        updateForecastsForCityByID(CitiesNavTypes.GET_PREV_CITY);
    }

    @Nullable @BindView(R.id.nextCityButton) ImageButton loadNextCity;
    @Optional @OnClick(R.id.nextCityButton) void onClickNext() {
        updateForecastsForCityByID(CitiesNavTypes.GET_NEXT_CITY);
    }

    // предупреждение при отсутствии городов в списке
    @Nullable @BindView(R.id.textEmtyListWarning) TextView textEmtyListWarning;


    @BindViews({R.id.horizontalRecyclerViewHours, R.id.horizontalRecyclerViewDays,
            R.id.valueHumidityDay, R.id.valuePressureDay,
            R.id.valueCloudinessDay, R.id.valueWindSpeedDay,
            R.id.valueTempMorn, R.id.valueTempDay,
            R.id.valueTempEvening, R.id.valueTempNight,
            R.id.imageCurrWeather, R.id.valueCurrTemp,
            R.id.valueCurrCity, R.id.valueCurrDate,
            R.id.previousCityButton, R.id.nextCityButton,
            R.id.imagePressureDay, R.id.imageHumidityDay,
            R.id.imageCloudinessDay, R.id.imageWindSpeedDay,
            R.id.textViewMorning, R.id.textViewDay,
            R.id.textViewEvening, R.id.textViewNight}) List<View> allViewsList;

    @Inject Context context;
    @Inject DataManager dataManager;
    @Inject ButterKnife.Setter visibilitySetter; // установка видимости для группы View

    @Inject @Named("llmThreeHourForecast") LinearLayoutManager llmThreeHourForecast;
    @Inject @Named("llmDailyForecast") LinearLayoutManager llmDailyForecast;

    // эффект перелистывания элементов с автодоводкой до края RecyclerView
    @Inject @Named("shThreeHourForecast") HorizontalStartSnapHelper threeHourSnapHelper;
    @Inject @Named("shDailyForecast") HorizontalStartSnapHelper dailySnapHelper;

    // адаптеры для RecyclerView
    private AdapterThreeHourForecast adapterThreeHourForecast;
    private AdapterDailyForecast adapterDailyForecast;

    public ForecastsFragmentPresenter() {
        DependencyInjector.getComponent().inject(this);
        dataManager.setForecastsFragmentPresenter(this);
    }

    // Прикрепление и открепление активити в зависимости от ЖЦ
    @Override
    public void attachView(@NonNull ForecastsFragment view) {
        bindedView = new WeakReference<ForecastsFragment>(view);
    }

    @Override
    public void detachView() {
        bindedView = null;
        //TODO Сделать объекты живущими пока живет фрагмент в Dagger (хранят ссылку на фрагмент, мапять течет)
        adapterThreeHourForecast = null;
        adapterDailyForecast = null;
        threeHourForecastRecycler.setLayoutManager(null);
        dailyForecastRecycler.setLayoutManager(null);
    }

    // конфигурирование RecyclerView
    public void configureRecyclerViews() {
        adapterThreeHourForecast = new AdapterThreeHourForecast();

        // по нажатию на элемент обновляются подробные данные по выбранному дню
        adapterDailyForecast = new AdapterDailyForecast(new AdapterDailyForecast.OnItemClickListener() {
            @Override
            public void onItemClick(OneDayForecast item, View selectedView) {
                updateSelectedItem(item, selectedView);
                updateSelectedDayForecast(item);
            }
        });

        threeHourForecastRecycler.setLayoutManager(llmThreeHourForecast);
        threeHourForecastRecycler.setOnFlingListener(null);
        threeHourSnapHelper.attachToRecyclerView(threeHourForecastRecycler);

        dailyForecastRecycler.setLayoutManager(llmDailyForecast);
        dailyForecastRecycler.setOnFlingListener(null);
        dailySnapHelper.attachToRecyclerView(dailyForecastRecycler);

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

    // полное обновление данных для конкретного города
    public void updateForecastsForCityByID(CitiesNavTypes type) {
        CityInfo cityInfo;
        switch (type) {
            case GET_FIRST_CITY:
                cityInfo = dataManager.getFirstCity();
                if (cityInfo != null) {
                    dataManager.getWeatherForToday(cityInfo.getCityId());
                    dataManager.getThreeHourForecast(cityInfo.getCityId());
                    dataManager.getDailyForecast(cityInfo.getCityId());
                } else {
                    //скрываем элементы UI, оповещаем пользователя об отсутствии добавленных городов
                    setAllViewsVisibility(View.INVISIBLE); // всем view ставим View.INVISIBLE
                    textEmtyListWarning.setText(context.getString(R.string.empty_list_warning));
                    textEmtyListWarning.setVisibility(View.VISIBLE); // показываем предупреждение
                }
                break;
            case GET_NEXT_CITY:
                cityInfo = dataManager.getNextCity();
                if (cityInfo != null) {
                    dataManager.getWeatherForToday(cityInfo.getCityId());
                    dataManager.getThreeHourForecast(cityInfo.getCityId());
                    dataManager.getDailyForecast(cityInfo.getCityId());
                }
                break;
            case GET_PREV_CITY:
                cityInfo = dataManager.getPreviousCity();
                if (cityInfo != null) {
                    dataManager.getWeatherForToday(cityInfo.getCityId());
                    dataManager.getThreeHourForecast(cityInfo.getCityId());
                    dataManager.getDailyForecast(cityInfo.getCityId());
                }
                break;
        }
    }

    public void updateSelectedItem(OneDayForecast item, View selectedView) {
        // сброс фона в всех(только прикрепленных и видимых) элементах View
        for (int childCount = dailyForecastRecycler.getChildCount(), i = 0; i < childCount; ++i) {
            dailyForecastRecycler.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
        }
        // цветовое выделение активного элемента
        selectedView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorActiveElementLight));
    }

    // обновление подробных данных по выбранному дню
    public void updateSelectedDayForecast(OneDayForecast oneDayForecast) {

        dayPressure.setText(String.format(Locale.getDefault(), "%.0f %s", oneDayForecast.getPressure(), context.getString(R.string.hPa_sign)));
        dayHumidity.setText(String.format(Locale.getDefault(), "%.0f%s", oneDayForecast.getHumidity(), context.getString(R.string.percent_sign)));
        dayCloudiness.setText(String.format(Locale.getDefault(), "%.0f%s", oneDayForecast.getClouds(), context.getString(R.string.percent_sign)));
        dayWindspeed.setText(String.format(Locale.getDefault(), "%.0f%s", oneDayForecast.getSpeed(), context.getString(R.string.m_sec)));

        dayTempMorn.setText(String.format(Locale.getDefault(), "%.0f%s", oneDayForecast.getTemp().getMornTemp(), context.getString(R.string.deg_sign)));
        dayTempDay.setText(String.format(Locale.getDefault(), "%.0f%s", oneDayForecast.getTemp().getDayTemp(), context.getString(R.string.deg_sign)));
        dayTempEvening.setText(String.format(Locale.getDefault(), "%.0f%s", oneDayForecast.getTemp().getEveTemp(), context.getString(R.string.deg_sign)));
        dayTempNight.setText(String.format(Locale.getDefault(), "%.0f%s", oneDayForecast.getTemp().getNightTemp(), context.getString(R.string.deg_sign)));
    }

    // обновление данных на текущий день
    public void updateCurrentWeather(WeatherDay weatherDay) {
        Picasso.with(context)
                .load(weatherDay.getWeatherIconUrl())
                .into(imageCurrWeather);

        currTemp.setText(String.format(Locale.getDefault(), "%.0f%s", weatherDay.getMainMeasurements().getTemp(), dataManager.getTempSign()));
        currCity.setText(String.valueOf(weatherDay.getCityName()));

        currDate.setText(String.format(Locale.getDefault(), "%s\n%s", context.getString(R.string.time_of_calculation), convertTime(weatherDay.getCalculationTime())));
    }

    private String convertTime(long time){
        Date date = new Date(time*1000);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM HH:mm", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());

        return sdf.format(date);
    }

    // typeOfData - тип данных, которые необходимо обновить на View
    // isUpToDate - true если данные обновлены по REST, false - если восстановлены
    // из Shared Preferences, т.е обновить не удалось
    public void updateUI(BasePOJO pojo, boolean isUpToDate) {

        // не уничтожен ли фрагмент
        if (bindedView == null) return;

        if ( pojo != null) {
            setAllViewsVisibility(View.VISIBLE); // всем view ставим View.VISIBLE
            textEmtyListWarning.setVisibility(View.INVISIBLE); // скрываем предупреждение пользователю

            switch (pojo.getTypeOfData()) {
                case WEATHER_DAY:
                    updateCurrentWeather((WeatherDay)pojo);
                    break;
                case THREE_HOUR_FORECAST:
                    adapterThreeHourForecast.setOrUpdateDataset(pojo.getDataArrayForAdapter());
                    threeHourForecastRecycler.setAdapter(adapterThreeHourForecast);
                    break;
                case DAILY_FORECAST:
                    adapterDailyForecast.setOrUpdateDataset(pojo.getDataArrayForAdapter());
                    dailyForecastRecycler.setAdapter(adapterDailyForecast);
            }

            if (!isUpToDate) {
                // данные не обновлены, но загружены локально сохраненные, информировать пользователя
            } else {
                // все ок, обновили UI новыми данными
            }

        } else {
            // все плохо, через REST API не обновилось, локально данные
            // тоже недоступны(возможно, еще не загружались ни разу)
            //  информировать пользователя
            setAllViewsVisibility(View.INVISIBLE); // всем view ставим View.INVISIBLE
            textEmtyListWarning.setText(context.getString(R.string.no_data_warning));
            textEmtyListWarning.setVisibility(View.VISIBLE); // показываем предупреждение
            }
    }

    // скрыть/показать все View на фрагменте
    private void setAllViewsVisibility(int visibility) {
        ButterKnife.apply(allViewsList, visibilitySetter, visibility);
    }

}
