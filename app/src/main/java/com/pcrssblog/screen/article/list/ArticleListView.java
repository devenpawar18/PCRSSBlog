package com.pcrssblog.screen.article.list;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pcrssblog.R;
import com.pcrssblog.api.model.Article;
import com.pcrssblog.api.model.Channel;
import com.pcrssblog.screen.article.detail.ArticleDetailActivity;
import com.pcrssblog.screen.article.detail.ArticleDetailView;
import com.pcrssblog.util.SpacesItemDecoration;
import com.pcrssblog.util.ViewUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * ArticleListView - A Fragment which implements the ArticleListContract.View interface.
 */
public class ArticleListView extends Fragment implements ArticleListContract.View, ArticleListAdapter.OnListItemClickListener, View.OnClickListener {
    private static final String REFRESH_VIEW = "refresh_view";
    private static final int PORTRAIT_GRID = 2;
    private static final int LANDSCAPE_GRID = 3;
    private ArticleListContract.Presenter mPresenter;
    private Context mContext;

    private TextView mTitle;
    private ImageView mRefresh;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutManager;
    private ArticleListAdapter mAdapter;
    private List<Article> mArticles = new ArrayList<>();
    private ProgressDialog mProgressDialog;
    private SpacesItemDecoration mDecoration;

    /**
     * @return ArticleListView Instance
     */
    public static ArticleListView newInstance() {
        return new ArticleListView();
    }

    @Override
    public void setPresenter(final Context pContext, final ArticleListContract.Presenter pPresenter) {
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
        LinearLayout layout = new LinearLayout(this.mContext);
        layout.setOrientation(LinearLayout.VERTICAL);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.mRecyclerView = new RecyclerView(this.mContext);
        this.mRecyclerView.setLayoutParams(params);
        this.mDecoration = new SpacesItemDecoration(this.mContext, R.dimen.view_article_list_spacing);
        this.mRecyclerView.addItemDecoration(this.mDecoration);
        if (this.mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            this.mGridLayoutManager = new GridLayoutManager(getActivity(), LANDSCAPE_GRID);
            this.updateRecyclerView(LANDSCAPE_GRID);
        } else {
            this.mGridLayoutManager = new GridLayoutManager(getActivity(), PORTRAIT_GRID);
            this.updateRecyclerView(PORTRAIT_GRID);
        }
        this.mAdapter = new ArticleListAdapter(this.mRecyclerView, this.mArticles, getActivity());
        this.mAdapter.setOnListClickListener(this);
        this.mRecyclerView.setAdapter(this.mAdapter);
        layout.addView(this.getHeaderView(), 0);
        layout.addView(this.mRecyclerView, 1);

        return layout;
    }

    /**
     * Update Article List
     *
     * @param pChannel
     */
    @Override
    public void updateView(final Channel pChannel) {
        this.updateHeaderView(pChannel);
        final List<Article> articles = pChannel.getArticles();
        if (articles != null && !articles.isEmpty()) {
            this.mArticles.clear();
            this.mArticles.addAll(articles);
            this.mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Start fetching articles
     */
    @Override
    public void onResume() {
        super.onResume();
        this.mPresenter.startFetching();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        this.mPresenter.stopFetching();
        if (this.mDecoration != null) {
            this.mRecyclerView.removeItemDecoration(this.mDecoration);
        }
        super.onDestroy();
    }

    /**
     * Handle Configuration Change
     *
     * @param newConfig
     */
    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            this.updateRecyclerView(LANDSCAPE_GRID);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            this.updateRecyclerView(PORTRAIT_GRID);
        }
    }

    /**
     * Update Article List View based on orientation
     *
     * @param pColumns
     */
    private void updateRecyclerView(final int pColumns) {
        this.mGridLayoutManager.setSpanCount(pColumns);
        this.mGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 ? pColumns : 1;
            }
        });
        this.mRecyclerView.setLayoutManager(this.mGridLayoutManager);
    }

    @Override
    public Context getContext() {
        return this.mContext;
    }

    @Override
    public void showProgressDialog() {
        this.mProgressDialog = ProgressDialog.show(this.mContext, "", this.mContext.getResources().getString(R.string.view_article_list_fetching_articles));
    }

    @Override
    public void dismissProgressDialog() {
        if (this.mProgressDialog != null && this.mProgressDialog.isShowing()) {
            this.mProgressDialog.dismiss();
        }
    }

    @Override
    public void showFailureMessage(final String pMessage) {
        Toast.makeText(this.mContext, pMessage, Toast.LENGTH_SHORT).show();
    }

    /**
     * Go to article detail screen
     *
     * @param pArticle
     * @param position
     */
    @Override
    public void onItemClick(final Article pArticle, final int position) {
        Intent intent = new Intent(getContext(), ArticleDetailActivity.class);
        intent.putExtra(ArticleDetailView.ARGUMENT_ARTICLE, pArticle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    /**
     * Update Header with Channel Title
     *
     * @param pChannel
     */
    private void updateHeaderView(final Channel pChannel) {
        this.mTitle.setText(pChannel.getTitle());
    }

    /**
     * @return HeaderView
     */
    @Override
    public RelativeLayout getHeaderView() {
        RelativeLayout headerView = new RelativeLayout(this.mContext);
        headerView.setBackgroundColor(this.mContext.getResources().getColor(R.color.colorPrimary));
        // Refresh Icon
        this.mRefresh = new ImageView(this.mContext);
        this.mRefresh.setId(1);
        RelativeLayout.LayoutParams refreshParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        refreshParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        refreshParams.setMargins(ViewUtils.getPixelsFromDP(this.mContext, 8), ViewUtils.getPixelsFromDP(this.mContext, 12), ViewUtils.getPixelsFromDP(this.mContext, 16), ViewUtils.getPixelsFromDP(this.mContext, 12));
        this.mRefresh.setLayoutParams(refreshParams);
        this.mRefresh.setTag(REFRESH_VIEW);
        this.mRefresh.setImageDrawable(this.mContext.getResources().getDrawable(R.drawable.ic_refresh));
        this.mRefresh.setOnClickListener(this);
        headerView.addView(this.mRefresh);
        // Title
        this.mTitle = new TextView(this.mContext);
        RelativeLayout.LayoutParams headerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        headerParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        headerParams.addRule(RelativeLayout.LEFT_OF, this.mRefresh.getId());
        headerParams.setMargins(ViewUtils.getPixelsFromDP(this.mContext, 16), ViewUtils.getPixelsFromDP(this.mContext, 12), 0, ViewUtils.getPixelsFromDP(this.mContext, 12));
        this.mTitle.setLayoutParams(headerParams);
        this.mTitle.setGravity(Gravity.CENTER_HORIZONTAL);
        this.mTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, this.mContext.getResources().getDimension(R.dimen.view_article_detail_header));
        this.mTitle.setTextColor(this.mContext.getResources().getColor(R.color.color_view_article_detail_header));
        this.mTitle.setSingleLine(true);
        this.mTitle.setEllipsize(TextUtils.TruncateAt.END);
        headerView.addView(this.mTitle);
        return headerView;
    }

    /**
     * Refresh Article List
     *
     * @param pView
     */
    @Override
    public void onClick(final View pView) {
        String tag = (String) pView.getTag();
        switch (tag) {
            case REFRESH_VIEW: {
                this.mPresenter.startFetching();
            }
        }
    }
}