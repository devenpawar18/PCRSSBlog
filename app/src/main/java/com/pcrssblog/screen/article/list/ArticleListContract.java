package com.pcrssblog.screen.article.list;

import android.content.Context;

import com.pcrssblog.api.model.Channel;
import com.pcrssblog.screen.BasePresenter;
import com.pcrssblog.screen.BaseView;

/**
 * Defines connection between the ArticleListView and the ArticleListPresenter.
 */
public interface ArticleListContract {

    interface View extends BaseView<Presenter> {
        Context getContext();

        void showProgressDialog();

        void dismissProgressDialog();

        void showFailureMessage(final String pMessage);

        void updateView(final Channel pChannel);
    }

    interface Presenter extends BasePresenter {

    }
}