package com.example.konstantin.scrollweather;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.konstantin.scrollweather.Custom_Markers.AbstractMarker;
import com.example.konstantin.scrollweather.Custom_Markers.CityMarker;
import com.example.konstantin.scrollweather.Custom_Markers.CustomClasterRenderer;
import com.example.konstantin.scrollweather.Dagger.DependencyInjector;
import com.example.konstantin.scrollweather.Model.DataManager;
import com.example.konstantin.scrollweather.POJO.CitiesListAdded.CitiesList;
import com.example.konstantin.scrollweather.POJO.CitiesListAdded.CityInfo;
import com.example.konstantin.scrollweather.POJO.WeatherDay.WeatherDay;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Konstantin on 19.09.2017.
 */

public class MapOverviewFragmentPresenter implements IMapOverviewFragmentPresenter {

    @Inject DataManager dataManager;
    @Inject Context context;

    @Nullable @BindView(R.id.map) MapView mapView;

    private WeakReference<MapOverviewFragment> bindedView;
    private static final int REQUEST_ERROR = 0;

    private GoogleApiClient googleApiClient;
    private GoogleMap googleMap;
    private Unbinder viewsUnbinder;
    private Location currentLocation;

    private ClusterManager<AbstractMarker> clusterManager;
    private AbstractMarker chosenMarker; // последний выбранный маркер
    private Cluster<AbstractMarker> chosenCluster; // последний выбранный кластер маркеров

    private CitiesList citiesList;
    private List<CityInfo> itemsList;

    private final String TAG = "WeatherApp";

    public MapOverviewFragmentPresenter() {
        DependencyInjector.getComponent().inject(this);
        dataManager.setMapOverviewFragmentPresenter(this);

        checkGoogleApiAvailability();
    }

    private void checkGoogleApiAvailability() {
        // проверяем, установлен ли на устройстве Google Play Services
        GoogleApiAvailability apiAvailabilityInstance = GoogleApiAvailability.getInstance();
        int errorCode = apiAvailabilityInstance.isGooglePlayServicesAvailable(context);


        // Сигнатура: public Dialog getErrorDialog (Activity activity, int errorCode, int requestCode,
        // DialogInterface.OnCancelListener cancelListener)
        //
        // The returned dialog displays a localized message about the error and upon user
        // confirmation (by tapping on dialog) will direct them to the Play Store if Google Play
        // services is out of date or missing, or to system settings if Google Play services
        // is disabled on the device.

        if (errorCode != ConnectionResult.SUCCESS) {
            Dialog errorDialog = apiAvailabilityInstance.getErrorDialog(bindedView.get().getActivity(), errorCode, REQUEST_ERROR,
                    new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            //
                            // TODO если сервис недоступен - выйти, оповестив пользователя
                            //
                        }
                    });
            errorDialog.show();
        }
    }

    public void getGoogleApiClient() {
        // Создаем и настраиваем экземпляр-клиент GoogleApiClient
        // для использования с API геолокации
        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        //TODO соединение установлено, обновить UI
                    }
                    @Override
                    public void onConnectionSuspended(int i) {
                        //TODO соединение приостановлено, обновить UI
                    }
                } )
                .build();
    }

    public void connectGoogleApiClient() {
        googleApiClient.connect();
    }

    public void disconnectGoogleApiClient() {
        googleApiClient.disconnect();
    }

    // загрузка списка городов, добавленных пользователем
    public CitiesList getCitiesList() {
        return dataManager.getCitiesList();
    }


    public void initMapAndClusterManager(Bundle savedInstanceState) {
        // инициализация карты
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap map) {
                googleMap = map;

                // Настройка кластеризации маркеров. Инициализация ClusterManager
                clusterManager = new ClusterManager<AbstractMarker>(context, googleMap);
                // устанавливаем кастомизированный обработчик событий и вида кластеров
                // (алгоритм по умолчанию - Distance-based Clustering)
                clusterManager.setRenderer(new CustomClasterRenderer(context, googleMap, clusterManager));

                // клик на маркере, перезапись текущего маркера
                clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<AbstractMarker>() {
                    @Override
                    public boolean onClusterItemClick(AbstractMarker item) {
                        chosenMarker = item;
                        return false;
                    }
                });

                // клик на кластере, перезапись текущего кластера, наводим фокус камеры на маркеры текущего кластера
                clusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<AbstractMarker>() {
                    @Override
                    public boolean onClusterClick(Cluster<AbstractMarker> cluster) {
                        chosenCluster = cluster;

                        LatLngBounds.Builder latLngBuilder = new LatLngBounds.Builder();
                        int margin = bindedView.get().getResources().getDimensionPixelSize(R.dimen.map_inset_margin);

                        for (AbstractMarker absMarker: chosenCluster.getItems()) {
                            latLngBuilder.include(absMarker.getPosition());
                        }

                        CameraUpdate zoomToFitClusterItems = CameraUpdateFactory.newLatLngBounds(latLngBuilder.build(), margin);
                        googleMap.animateCamera(zoomToFitClusterItems);

                        return true;
                    }
                });

                // Клик на всплывающем информационном окне.
                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        if (chosenMarker instanceof CityMarker) {
                            CityMarker m = (CityMarker) chosenMarker;
                            /*
                            // TODO: показывать подробный прогноз ?
                            Intent openForecast =
                            startActivity(openForecast);
                            */
                        }
                    }
                });

                // обработка нажатий на маркеры передаетсся ClusterManager
                googleMap.setOnMarkerClickListener(clusterManager);

                // ставим слушателя перемешения камеры по карте для перерисовки маркеров
                googleMap.setOnCameraIdleListener(clusterManager);

                // передаем управление InfoWindow к ClusterManager
                googleMap.setInfoWindowAdapter(clusterManager.getMarkerManager());

                // размещение маркеров на карте
                setCityMarkersOnMap();
            }
        });
    }

    public void setCityMarkersOnMap() {

        citiesList = getCitiesList();

        if (googleMap == null || citiesList == null ) {
            // TODO карта не готова, либо города не удалось загрузить. Оповещение пользователя, обновление UI
        } else {
            // очищаем карту
            googleMap.clear();

            // TODO добавление собственного местоположения
            //LatLng myPointOnMap = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            //clusterManager.addItem(new MyPositionMarker(myPointOnMap));

            LatLngBounds.Builder builderLatLng = new LatLngBounds.Builder();

            //builderLatLng.include(myPointOnMap); // сначала добавляем собственную позицию

            if (citiesList.getCitiesList().isEmpty()) {
                // TODO Города не добавлены, список пуст. Оповещение пользователя, обновление UI
            } else {
                // добавляем в LatLngBounds все маркеры для дальнейшего позиционирования карты и корректного масштабирования
                for (CityInfo ci: citiesList.getCitiesList()) {
                    clusterManager.addItem(new CityMarker(ci));
                    builderLatLng.include(ci.GetLatLngPosition());
                }
            }

            // TODO устанавливаем обработчик нажатий еа каждый маркер в коллекции кластеров ClusterManager
            clusterManager.getMarkerCollection().setOnInfoWindowAdapter(new MarkerInfoWindow());

            // отступ для оформления берем из ресурсов
            int margin = bindedView.get().getResources().getDimensionPixelSize(R.dimen.map_inset_margin);

            // Класс CameraUpdateFactory содержит разнообразные статические методы для построения
            // различных видов объектов CameraUpdate, которые изменяют позицию, уровень увеличения
            // и другие свойства участка, отображаемого на карте.
            clusterManager.cluster();

            // В данном случае камера наводится на объект LatLngBounds bounds
            CameraUpdate camAnimation = CameraUpdateFactory.newLatLngBounds(builderLatLng.build(), margin);

            // Карта обновляется одним из двух способов: методом moveCamera(CameraUpdate)
            // или animateCamera(CameraUpdate).
            googleMap.animateCamera(camAnimation);

        }

    }

    // Адаптер InfoWindow (окна, появляющегопя по нажатию на маркер)
    private class MarkerInfoWindow implements GoogleMap.InfoWindowAdapter {
        @Override
        public View getInfoContents(Marker marker) {

            // запрос данных для маркера
            if (chosenMarker != null) {
                WeatherDay data = dataManager.getDataForMarker(chosenMarker);
                // view всплывающего окошка InfoWindow
                View view = bindedView.get().getActivity().getLayoutInflater().inflate(R.layout.custom_info_window, null);

                TextView cityName = view.findViewById(R.id.cityName_popup_map);
                cityName.setText(String.format(Locale.getDefault(), "%s, %s", data.getCityName(), data.getSun().getCountryCode()));

                TextView temp = view.findViewById(R.id.temp_popup_map);
                temp.setText(String.format(Locale.getDefault(), "%.1f " + context.getString(R.string.deg_celsius), data.getMainMeasurements().getTemp()));

                ImageView weatherImage = view.findViewById(R.id.image_popup_map);
                if (data.getWeatherIconUrl() != null) {
                    Picasso.with(context)
                            .load(data.getWeatherIconUrl())
                            .placeholder(R.drawable.not_applicable)
                            .into(weatherImage, new InfoWindowRefresher(marker));
                }

                return view;
            } else return null;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }
    }

    // Picasso's callback, по завершении загрузки фото, обновляет InfoWindow(закрывает и открывает заново)
    private class InfoWindowRefresher implements Callback {
        Marker markerToRefresh = null;

        private InfoWindowRefresher(Marker marker) {
            this.markerToRefresh = marker;
        }

        @Override
        public void onSuccess() {
            if (markerToRefresh != null && markerToRefresh.isInfoWindowShown()) {
                markerToRefresh.hideInfoWindow();
                markerToRefresh.showInfoWindow();
            }
        }

        @Override
        public void onError() {
            Log.e(TAG, "Ошибка загрузки изображения в InfoWindow");
        }
    }

    // обновление полей всплывающего InfoWindow
    public void updateMarkerView(WeatherDay data, Marker markerToRefresh) {
        if (data != null) {


        }
    }

    @Override
    public void attachView(@NonNull MapOverviewFragment view) {
        bindedView = new WeakReference<>(view);
    }

    @Override
    public void detachView() {
        bindedView = null;
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
}
