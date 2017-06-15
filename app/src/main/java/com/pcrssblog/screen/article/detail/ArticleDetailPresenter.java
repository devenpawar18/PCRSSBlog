package com.pcrssblog.screen.article.detail;

import android.content.Context;
import android.support.annotation.NonNull;

import com.pcrssblog.util.ViewUtils;

/**
 * Implements the presenter interface in the corresponding ArticleDetailContract.
 */

public class ArticleDetailPresenter implements ArticleDetailContract.Presenter {
    private ArticleDetailContract.View mView;

    public ArticleDetailPresenter(@NonNull ArticleDetailContract.View pView, final Context pContext) {
        this.mView = ViewUtils.checkNotNull(pView, "ArticleDetailView can't be null!");
        this.mView.setPresenter(pContext, this);
    }

    @Override
    public void startFetching() {

    }

    @Override
    public void stopFetching() {

    }
}