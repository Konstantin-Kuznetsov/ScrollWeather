package com.example.konstantin.scrollweather;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Konstantin on 06.09.2017.
 *
 * Отличие от стандартного LinearSnapHelper только одно - доводка до левого
 * края левого элемента (по умолчанию доводка до видимости центральных элементов)
 * Если "докрутили" до последнего - показываем полностью последний элемент.
 *
 * Переопределяются по сути только calculateDistanceToFinalSnap и findSnapView
 * для изменения поведения
 */

public class HorizontalStartSnapHelper extends LinearSnapHelper {

    private OrientationHelper horizontalHelper;

    public HorizontalStartSnapHelper() { }

    @Override
    public void attachToRecyclerView(@Nullable RecyclerView recyclerView) throws IllegalStateException {
        super.attachToRecyclerView(recyclerView);
    }

    @Nullable
    @Override
    public int[] calculateDistanceToFinalSnap(@NonNull RecyclerView.LayoutManager layoutManager, @NonNull View targetView) {
        int[] out = new int[2];

        if (layoutManager.canScrollHorizontally()) {
            out[0] = distanceToStart(targetView, getHorizontalHelper(layoutManager));
        } else {
            out[0] = 0;
        }

        return out;
    }

    @Nullable
    @Override
    public View findSnapView(RecyclerView.LayoutManager layoutManager) {
            return getStartView(layoutManager, getHorizontalHelper(layoutManager));
    }

    // getDecoratedStart(View view)
    //Returns the start of the view including its decoration and margin.

    // getStartAfterPadding()
    //Returns the start position of the layout after the start padding is added.

    private int distanceToStart(View targetView, OrientationHelper helper) {
        return helper.getDecoratedStart(targetView) - helper.getStartAfterPadding();
    }

    private View getStartView(RecyclerView.LayoutManager layoutManager, OrientationHelper helper) {

        int firstChild = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        boolean isLastItem = ((LinearLayoutManager) layoutManager)
                .findLastCompletelyVisibleItemPosition() == layoutManager.getItemCount() - 1;

        // не скроллим, если докрутили до последнего(возвращаем null)
        if (firstChild == RecyclerView.NO_POSITION || isLastItem) {
            return null;
        }

        View child = layoutManager.findViewByPosition(firstChild);

        if (helper.getDecoratedEnd(child) >= helper.getDecoratedMeasurement(child) / 2 && helper.getDecoratedEnd(child) > 0) {
                return child;
        } else {
            if (((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition() == layoutManager.getItemCount() - 1) {
                return null;
            } else {
                return layoutManager.findViewByPosition(firstChild + 1);
            }
        }
    }

    private OrientationHelper getHorizontalHelper(RecyclerView.LayoutManager layoutManager) {
        if (horizontalHelper == null) {
            horizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager);
        }
        return horizontalHelper;
    }

}
