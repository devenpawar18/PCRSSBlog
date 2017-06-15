package com.pcrssblog.screen.splash;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pcrssblog.R;
import com.pcrssblog.util.ViewUtils;

/**
 * SplashView - A Fragment which implements the SplashContract.View interface.
 */

public class SplashView extends Fragment implements SplashContract.View {
    public static final String ARGUMENT_ARTICLE = "article";

    private SplashContract.Presenter mPresenter;
    private Context mContext;

    /**
     * @return SplashView Instance
     */
    public static SplashView newInstance() {
        return new SplashView();
    }

    @Override
    public void setPresenter(final Context pContext, final SplashContract.Presenter pPresenter) {
        this.mContext = pContext;
        this.mPresenter = ViewUtils.checkNotNull(pPresenter);
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        return this.getSplashView();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.mPresenter.nextScreen();
    }

    @Override
    public Context getContext() {
        return this.mContext;
    }

    @Override
    public SplashActivity getSplashActivity() {
        return (SplashActivity) getActivity();
    }

    /**
     * @return SplashView
     */
    private RelativeLayout getSplashView() {
        RelativeLayout splashView = new RelativeLayout(this.mContext);
        splashView.setBackgroundColor(this.mContext.getResources().getColor(R.color.colorPrimary));
        // Splash View Title
        TextView textView = new TextView(this.mContext);
        RelativeLayout.LayoutParams headerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        headerParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        headerParams.setMargins(20, 30, 20, 30);
        textView.setLayoutParams(headerParams);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, this.mContext.getResources().getDimension(R.dimen.view_splash_title));
        textView.setTextColor(this.mContext.getResources().getColor(R.color.color_view_splash_title));
        textView.setMaxLines(2);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setGravity(Gravity.CENTER);
        textView.setText(R.string.view_splash_title);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        splashView.addView(textView);
        return splashView;
    }
}