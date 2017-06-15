package com.pcrssblog.screen.splash;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.pcrssblog.screen.article.list.ArticleListActivity;
import com.pcrssblog.util.ViewUtils;

/**
 * Implements the presenter interface in the corresponding SplashContract.
 */
public class SplashPresenter implements SplashContract.Presenter {
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    private SplashContract.View mView;

    public SplashPresenter(@NonNull SplashContract.View pView, final Context pContext) {
        this.mView = ViewUtils.checkNotNull(pView, "MainView can't be null!");
        this.mView.setPresenter(pContext, this);
    }

    @Override
    public void startFetching() {

    }

    @Override
    public void stopFetching() {

    }

    @Override
    public void nextScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashPresenter.this.mView.getSplashActivity(), ArticleListActivity.class);
                SplashPresenter.this.mView.getSplashActivity().startActivity(mainIntent);
                SplashPresenter.this.mView.getSplashActivity().finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}