package com.pcrssblog.screen.splash;

import android.content.Context;

import com.pcrssblog.screen.BasePresenter;
import com.pcrssblog.screen.BaseView;

/**
 * Defines connection between the SplashView and the SplashPresenter.
 */
public interface SplashContract {

    interface View extends BaseView<Presenter> {
        Context getContext();

        SplashActivity getSplashActivity();
    }

    interface Presenter extends BasePresenter {
        void nextScreen();
    }
}