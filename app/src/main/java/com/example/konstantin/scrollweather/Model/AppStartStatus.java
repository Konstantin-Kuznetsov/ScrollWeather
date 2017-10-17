package com.example.konstantin.scrollweather.Model;

/**
 * Created by Konstantin on 15.08.2017.
 *
 * Константы статуса запуска программы.
 *
 * FIRST_TIME - самый первый запуск программы
 * FIRST_TIME_NEW_VERSION - первый запуск новой версии программы(если в SharedPreferences остались
 *                          данные с прошлой проверки после обновления), потенциально можно
 *                          использовать для показа нововведений последней версии программы.
 * NORMAL - обычный сценарий запуска, не новая версия, не первый запуск.
 */

enum AppStartStatus {
    FIRST_TIME, FIRST_TIME_NEW_VERSION, NORMAL;
}