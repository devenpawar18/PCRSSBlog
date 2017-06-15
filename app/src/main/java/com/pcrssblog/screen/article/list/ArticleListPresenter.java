package com.pcrssblog.screen.article.list;

import android.content.Context;
import android.support.annotation.NonNull;

import com.pcrssblog.api.model.Channel;
import com.pcrssblog.api.service.ChannelService;
import com.pcrssblog.util.ViewUtils;

/**
 * Implements the presenter interface in the corresponding ArticleListContract.
 */
public class ArticleListPresenter implements ArticleListContract.Presenter, ChannelService.ChannelCallback {
    private ArticleListContract.View mView;
    private ChannelService mChannelService;
    private static final String CHANNEL_URL = "https://www.personalcapital.com/blog/feed/?cat=3%2C891%2C890%2C68%2C284";
    private boolean mDownloading;

    public ArticleListPresenter(@NonNull ArticleListContract.View pView, final Context pContext) {
        this.mView = ViewUtils.checkNotNull(pView, "ArticleListView can't be null!");
        this.mView.setPresenter(pContext, this);
    }

    @Override
    public void startFetching() {
        if (!this.mDownloading) {
            this.stopFetching();
            this.mView.showProgressDialog();
            this.mChannelService = new ChannelService(this, this.mView.getContext());
            this.mChannelService.execute(CHANNEL_URL);
            mDownloading = true;
        }
    }

    @Override
    public void success(final Channel pChannel) {
        if (pChannel != null) {
            this.mView.updateView(pChannel);
        }
    }

    @Override
    public void failure(final String pMessage) {
        if (pMessage != null) {
            this.mView.showFailureMessage(pMessage);
        }
    }

    @Override
    public void downloadComplete() {
        this.mDownloading = false;
        this.mView.dismissProgressDialog();
        this.stopFetching();
    }

    @Override
    public void downloadCancel() {
        this.mDownloading = false;
        this.mView.dismissProgressDialog();
    }

    @Override
    public void stopFetching() {
        if (this.mChannelService != null) {
            this.mChannelService.cancel(true);
        }
        this.downloadCancel();
    }
}