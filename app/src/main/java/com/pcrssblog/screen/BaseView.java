package com.pcrssblog.screen;

import android.content.Context;

/**
 * Base View to link presenter
 *
 * @param <T>
 */

public interface BaseView<T> {

    void setPresenter(final Context pContext, final T pPresenter);
}