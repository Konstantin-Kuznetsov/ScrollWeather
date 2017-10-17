package com.example.konstantin.scrollweather;

import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;

/**
 * Created by Konstantin on 14.08.2017.
 *
 * Базовый класс для презентеров
 */

public abstract class BasePresenter<M, V> {

    protected M model;
    private WeakReference<V> view;

    public void setModel(M model) {
        //resetState(); --- ???
        this.model = model;

        //if (setupDone()) {
        //    updateView();
        //}
    }

    protected void  resetState() { } // ?? что делает?

    // "Прикрепляем" view к презентеру
    public void attachView(@NonNull V view) {
        this.view = new WeakReference<V>(view);
        //if (setupDone()) {
        //    updateView();
        //}
    }

    // открепляем view от презентера, например при смене ориентации и пересоздании view
    public void detachView() {
        this.view = null;
    }



    protected abstract void updateView();

    // проверка готовы ли для взаимодействия View и Model
    protected boolean isSetupDone() {
        return checkView() != null && model != null;
    }

    protected V checkView() {
        if (view == null) {
            return null;
        } else {
            return view.get(); // get() создаеn strong-ссылку на объект
        }
    }
}
