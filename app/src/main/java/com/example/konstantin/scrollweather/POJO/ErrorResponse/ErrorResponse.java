package com.example.konstantin.scrollweather.POJO.ErrorResponse;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Konstantin on 17.08.2017.
 *
 * Структура ответа с ошибкой от сервера
 */

public class ErrorResponse {
    @SerializedName("cod")
    private int errCode;
    @SerializedName("message")
    private String errMessage;

    public int getErrCode() {
        return errCode;
    }

    public String getErrMessage() {
        return errMessage;
    }
}
