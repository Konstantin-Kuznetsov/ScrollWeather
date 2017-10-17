package com.example.konstantin.scrollweather.POJO;

import java.util.ArrayList;

/**
 * Created by Konstantin on 17.08.2017.
 *
 * Базобый интерфейс для всех POJO с прогнозами
 */

public interface BasePOJO {
    TypeOfData getTypeOfData();
    int getCityId();
    ArrayList getDataArrayForAdapter();
}
