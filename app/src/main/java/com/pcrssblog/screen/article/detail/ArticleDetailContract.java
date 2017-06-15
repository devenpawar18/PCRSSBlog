package com.pcrssblog.screen.article.detail;

import android.content.Context;

import com.pcrssblog.api.model.Article;
import com.pcrssblog.screen.BasePresenter;
import com.pcrssblog.screen.BaseView;

/**
 * Defines connection between the ArticleDetailView and the ArticleDetailPresenter.
 */

public interface ArticleDetailContract {
    interface View extends BaseView<Presenter> {
        Context getContext();

        void showProgressDialog();

        void dismissProgressDialog();

        void updateView(final Article pArticle);
    }

    interface Presenter extends BasePresenter {

    }
}