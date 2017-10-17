package com.example.konstantin.scrollweather;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;


/**
 * Created by Konstantin on 03.08.2017.
 *
 * Кастомизированное поле ввода с задержкой перед формированием списка подсказок.
 * Переопределяются методы performFiltering и onFilterComplete.
 *
 * performFiltering согдасно документации выполняется в отдельном потоке, а публикация результатов
 * для обновления UI в основном, так что сетевые запросы в методе вполне уместны.
 */

public class DelayAutoCompleteTextView extends AppCompatAutoCompleteTextView {

    private static final int MESSAGE_TEXT_CHANGED = 1;
    private static final int DEFAULT_AUTOCOMPLETE_DELAY = 500;

    ProgressBar loadingIndicator;
    // используется как задержка по умолчанию(если не переопределено в setAutocompleteDelay)
    int autoCompleteDelay = DEFAULT_AUTOCOMPLETE_DELAY;

    public DelayAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private final Handler filterHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            DelayAutoCompleteTextView.super.performFiltering((CharSequence) msg.obj, msg.arg1);
        }
    };

    // просто задержка 500мс перед отправкой запроса с прокручиванием индикатора.
    // после указанного времени вызывается
    @Override
    protected void performFiltering(CharSequence text, int keyCode) {
        if (loadingIndicator != null) {
            loadingIndicator.setVisibility(View.VISIBLE);
        }
        filterHandler.removeMessages(MESSAGE_TEXT_CHANGED);
        filterHandler.sendMessageDelayed(filterHandler.obtainMessage(MESSAGE_TEXT_CHANGED, text), autoCompleteDelay);
    }

    @Override
    public void onFilterComplete(int count) {
        if (loadingIndicator != null) {
            loadingIndicator.setVisibility(View.GONE);
        }
        super.onFilterComplete(count);
    }

    public void setLoadingIndicator(ProgressBar progressBar) {
        loadingIndicator = progressBar;
    }

    public void setAutoCompleteDelay(int autocompleteDelay) {
        this.autoCompleteDelay = autocompleteDelay;
    }
}
