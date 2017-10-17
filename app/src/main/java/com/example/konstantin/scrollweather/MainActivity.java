package com.example.konstantin.scrollweather;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment fragment;
    private FragmentManager fm;
    private NavigationView navigationView;
    private InputMethodManager imm;

    private final String TAG_FORECASTS = "tag_forecasts";
    private final String TAG_EDIT_CITIES = "tag_edit_cities";
    private final String TAG_MAP_OVERVIEW = "tag_map_overview";
    private final String TAG_SETTINGS = "tag_settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_forecasts); // <FrameLayout ... />

        fm = getSupportFragmentManager();
        fragment = fm.findFragmentById(R.id.fragment_container);
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        // если фрагмент еще не существует - создаем новый
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment, TAG_FORECASTS)
                    .addToBackStack(fragment.getClass().getName())
                    .commit();

            //getSupportActionBar().setTitle(R.string.app_name);
        }

        // Установить Toolbar для замены ActionBar'а.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout); // выдвижное меню
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        // при пересоздании активити подсвечиваем первый пункт
        navigationView.setCheckedItem(R.id.forecastsItem);
    }


    protected Fragment createFragment() {
        return new ForecastsFragment();
    }

    // поведение закрытия шторки по нажатию back
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

            // подсветка соответствующего пункта навигационного меню
            Fragment currentFragment = fm.findFragmentById(R.id.fragment_container);

            if (currentFragment instanceof ForecastsFragment) {
                navigationView.setCheckedItem(R.id.forecastsItem);
            }
            else if (currentFragment instanceof FindCityFragment) {
                navigationView.setCheckedItem(R.id.editCitiesItem);
            }
            else if (currentFragment instanceof MapOverviewFragment) {
                navigationView.setCheckedItem(R.id.mapOverviewItem);
            }
            else if (currentFragment instanceof SettingsFragment) {
                navigationView.setCheckedItem(R.id.settingsItem);
            }
        }
    }

    // обработка выбора пунктов в NavigationView
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Fragment tmpFragment;
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        // если была открыта клавиатура(например, на фрагменте редактирования городов) - скрываем
        // при переходе на фрагмент, где она не нужна
        imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);

        // Если повторно выбирается пункт, но просто закрываем боковое меню, иначе- смена фрагмента
        if (id == R.id.forecastsItem && !(currentFragment instanceof ForecastsFragment)) {
            // поиск уже созданного необходимого фрагмента. Если не находим - создаем новый.
            tmpFragment = fm.findFragmentByTag(TAG_FORECASTS);
            if (tmpFragment != null) {
                fm.beginTransaction()
                        .replace(R.id.fragment_container, tmpFragment, TAG_FORECASTS)
                        .addToBackStack(fragment.getClass().getName())
                        .commit();
            } else {
                fm.beginTransaction()
                        .replace(R.id.fragment_container, new ForecastsFragment(), TAG_FORECASTS)
                        .addToBackStack(fragment.getClass().getName())
                        .commit();
            }

//                getSupportActionBar().setTitle("Прогноз погоды");
//                getSupportActionBar().setIcon(R.drawable.map_with_placeholder);

        } else if (id == R.id.editCitiesItem  && !(currentFragment instanceof FindCityFragment)) {
            tmpFragment = fm.findFragmentByTag(TAG_EDIT_CITIES);
            if (tmpFragment != null) {
                fm.beginTransaction()
                        .replace(R.id.fragment_container, tmpFragment, TAG_EDIT_CITIES)
                        .addToBackStack(fragment.getClass().getName())
                        .commit();
            } else {
                fm.beginTransaction()
                        .replace(R.id.fragment_container, new FindCityFragment(), TAG_EDIT_CITIES)
                        .addToBackStack(fragment.getClass().getName())
                        .commit();
            }
//                getSupportActionBar().setTitle("Редактирование мест");
//                getSupportActionBar().setIcon(R.drawable.map_with_placeholder);

        } else if (id == R.id.mapOverviewItem  && !(currentFragment instanceof MapOverviewFragment)) {
            tmpFragment = fm.findFragmentByTag(TAG_MAP_OVERVIEW);
            if (tmpFragment != null) {
                fm.beginTransaction()
                        .replace(R.id.fragment_container, tmpFragment, TAG_MAP_OVERVIEW)
                        .addToBackStack(fragment.getClass().getName())
                        .commit();
            } else {
                fm.beginTransaction()
                        .replace(R.id.fragment_container, new MapOverviewFragment(), TAG_MAP_OVERVIEW)
                        .addToBackStack(fragment.getClass().getName())
                        .commit();
            }
//                getSupportActionBar().setTitle("Обзор прогнозов на карте");
//                getSupportActionBar().setIcon(R.drawable.map_with_placeholder);
        }

        else if (id == R.id.settingsItem  && !(currentFragment instanceof SettingsFragment)) {
            tmpFragment = fm.findFragmentByTag(TAG_SETTINGS);
            if (tmpFragment != null) {
                fm.beginTransaction()
                        .replace(R.id.fragment_container, tmpFragment, TAG_SETTINGS)
                        .addToBackStack(fragment.getClass().getName())
                        .commit();
            } else {
                fm.beginTransaction()
                        .replace(R.id.fragment_container, new SettingsFragment(), TAG_SETTINGS)
                        .addToBackStack(fragment.getClass().getName())
                        .commit();
            }
//                getSupportActionBar().setTitle("Настройки приложения");
//                getSupportActionBar().setIcon(R.drawable.map_with_placeholder);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
