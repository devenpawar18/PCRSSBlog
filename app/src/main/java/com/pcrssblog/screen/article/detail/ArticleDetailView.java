package com.pcrssblog.screen.article.detail;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pcrssblog.R;
import com.pcrssblog.api.model.Article;
import com.pcrssblog.util.ViewUtils;

/**
 * ArticleDetailView - A Fragment which implements the ArticleDetailContract.View interface.
 */
public class ArticleDetailView extends Fragment implements ArticleDetailContract.View {
    private static final String URL_DETAIL_QUERY = "?displayMobileNavigation=0";
    public static final String ARGUMENT_ARTICLE = "article";

    private ArticleDetailContract.Presenter mPresenter;
    private Context mContext;

    private TextView mTitle;
    private WebView mWebView;

    private ProgressDialog mProgressDialog;

    public static ArticleDetailView newInstance() {
        return new ArticleDetailView();
    }

    @Override
    public void setPresenter(final Context pContext, final ArticleDetailContract.Presenter pPresenter) {
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
        return this.getWebViewLayout();
    }

    /**
     * Load Url to webview
     *
     * @param pArticle
     */
    @Override
    public void updateView(final Article pArticle) {
        if (pArticle.getWebLink() != null) {
            this.updateHeaderView(pArticle);
            final WebSettings settings = this.mWebView.getSettings();
            settings.setJavaScriptEnabled(true);
            this.mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
            this.mWebView.getSettings().setLoadWithOverviewMode(true);
            this.mWebView.getSettings().setUseWideViewPort(true);

            this.showProgressDialog();

            this.mWebView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }

                public void onPageFinished(WebView view, String url) {
                    ArticleDetailView.this.dismissProgressDialog();
                }

                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Toast.makeText(ArticleDetailView.this.mContext, ArticleDetailView.this.mContext.getResources().getString(R.string.view_article_detail_web_view_error) + description, Toast.LENGTH_SHORT).show();
                }
            });
            final String webLink = pArticle.getWebLink() + URL_DETAIL_QUERY;
            this.mWebView.loadUrl(webLink);
        }
    }

    @Override
    public Context getContext() {
        return this.mContext;
    }

    @Override
    public void showProgressDialog() {
        this.mProgressDialog = ProgressDialog.show(this.mContext, "", this.mContext.getResources().getString(R.string.view_article_detail_loading_article));
    }

    @Override
    public void dismissProgressDialog() {
        if (this.mProgressDialog != null && this.mProgressDialog.isShowing()) {
            this.mProgressDialog.dismiss();
        }
    }

    /**
     * Update header with article title
     *
     * @param pArticle
     */
    private void updateHeaderView(final Article pArticle) {
        this.mTitle.setText(pArticle.getTitle());
    }

    /**
     * @return Article detail webview layout
     */
    private LinearLayout getWebViewLayout() {
        LinearLayout layout = new LinearLayout(this.mContext);
        final Article article = getArguments().getParcelable(ARGUMENT_ARTICLE);
        if (article != null) {
            layout.setOrientation(LinearLayout.VERTICAL);
            this.mWebView = new WebView(this.mContext);
            layout.addView(this.getHeaderView(), 0);
            layout.addView(this.mWebView, 1);
            this.updateView(article);
        }

        return layout;
    }

    /**
     * @return Article detail header view
     */
    private RelativeLayout getHeaderView() {
        RelativeLayout headerView = new RelativeLayout(this.mContext);
        headerView.setBackgroundColor(this.mContext.getResources().getColor(R.color.colorPrimary));
        // Title
        this.mTitle = new TextView(this.mContext);
        RelativeLayout.LayoutParams headerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        headerParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        headerParams.setMargins(0, ViewUtils.getPixelsFromDP(this.mContext, 12), ViewUtils.getPixelsFromDP(this.mContext, 16), ViewUtils.getPixelsFromDP(this.mContext, 12));
        this.mTitle.setLayoutParams(headerParams);
        this.mTitle.setGravity(Gravity.CENTER_HORIZONTAL);
        this.mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, this.mContext.getResources().getDimension(R.dimen.view_article_detail_header));
        this.mTitle.setTextColor(this.mContext.getResources().getColor(R.color.color_view_article_detail_header));
        this.mTitle.setSingleLine(true);
        this.mTitle.setEllipsize(TextUtils.TruncateAt.END);
        headerView.addView(this.mTitle);
        return headerView;
    }
}