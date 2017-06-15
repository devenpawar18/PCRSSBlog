package com.pcrssblog.screen.splash;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.pcrssblog.util.ViewUtils;

/**
 * Initialize Splash Presenter and View
 */
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final FrameLayout container = new FrameLayout(this);
        setContentView(container);

        final SplashView splashView = SplashView.newInstance();
        ViewUtils.addViewToActivity(getSupportFragmentManager(), splashView);

        new SplashPresenter(splashView, this);
    }
}