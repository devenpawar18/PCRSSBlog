package com.pcrssblog.screen;

import android.content.Context;
import android.widget.RelativeLayout;

/**
 * Base View to link presenter
 *
 * @param <T>
 */

public interface BaseView<T> {

    void setPresenter(final Context pContext, final T pPresenter);

    RelativeLayout getHeaderView();
}