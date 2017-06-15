package com.pcrssblog.screen.article.list;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pcrssblog.R;
import com.pcrssblog.api.model.Article;
import com.pcrssblog.api.service.ImageDownloaderService;
import com.pcrssblog.util.cache.ImageCache;

import java.util.List;

/**
 * Adapter for Article List
 */

public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ArticleViewHolder> implements View.OnClickListener {
    private static final int TYPE_MAIN_ARTICLE = 0;
    private static final int TYPE_SUB_ARTICLE = 1;
    private static final int MAIN_ARTICLE_IMAGE_HEIGHT = 800;
    private static final int SUB_ARTICLE_IMAGE_HEIGHT = 500;
    private List<Article> mArticles;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private ImageCache mCache;

    private OnListItemClickListener mOnListItemClickListener;

    public static class ArticleViewHolder extends RecyclerView.ViewHolder {
        /**
         * Article view attributes
         */
        public LinearLayout mLayout;
        public ImageView mImage;
        public TextView mTitle;
        public TextView mContent;
        public TextView mHeader;

        public ArticleViewHolder(LinearLayout pItemView) {
            super(pItemView);
            this.mLayout = (LinearLayout) pItemView.getChildAt(0);
            this.mImage = (ImageView) this.mLayout.getChildAt(0);
            this.mTitle = (TextView) this.mLayout.getChildAt(1);
            this.mContent = (TextView) this.mLayout.getChildAt(2);
            this.mHeader = (TextView) pItemView.getChildAt(1);
        }
    }

    public ArticleListAdapter(RecyclerView pRecyclerView, List<Article> pArticles, Context pContext) {
        this.mRecyclerView = pRecyclerView;
        this.mArticles = pArticles;
        this.mContext = pContext;
        this.mCache = new ImageCache(this.getDefaultLruCacheSize());
    }

    /**
     * @param pParent
     * @param pViewType
     * @return Create views based on type
     */
    @Override
    public ArticleListAdapter.ArticleViewHolder onCreateViewHolder(ViewGroup pParent, int pViewType) {
        final Context context = pParent.getContext();
        LinearLayout mainView = this.getMainLinearLayout(context);
        LinearLayout view = this.getLinearLayout(context);
        if (pViewType == TYPE_MAIN_ARTICLE) {
            view.addView(this.getImage(context, true), 0);
            view.addView(this.getTitle(context, true), 1);
            view.addView(this.getContent(context), 2);
        } else {
            view.addView(this.getImage(context, false), 0);
            view.addView(this.getTitle(context, false), 1);
            view.addView(this.getContent(context), 2);
        }
        mainView.addView(view, 0);
        mainView.addView(this.getHeader(context), 1);
        mainView.setOnClickListener(this);
        return new ArticleViewHolder(mainView);
    }

    /**
     * @param pPosition
     * @return Type
     */
    @Override
    public int getItemViewType(int pPosition) {
        if (pPosition == 0) {
            return TYPE_MAIN_ARTICLE;
        } else if (pPosition > 0) {
            return TYPE_SUB_ARTICLE;
        }

        return TYPE_SUB_ARTICLE;
    }

    @Override
    public void onClick(final View pView) {
        final int itemPosition = this.mRecyclerView.getChildLayoutPosition(pView);
        Article item = this.mArticles.get(itemPosition);
        this.mOnListItemClickListener.onItemClick(item, itemPosition);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, final int position) {
        final Article article = this.mArticles.get(position);
        if (holder.mImage != null) {
            Bitmap image = this.mCache.get(article.getImageLink());
            if (image != null) {
                holder.mImage.setImageBitmap(image);
            } else {
                new ImageDownloaderService(holder.mImage, this.mCache).execute(article.getImageLink());
            }
        }
        holder.mTitle.setText(article.getTitle());
        if (position == 0) {
            holder.mContent.setVisibility(View.VISIBLE);
            holder.mContent.setText(Html.fromHtml(article.getContent()));
            holder.mHeader.setVisibility(View.VISIBLE);
            holder.mHeader.setText(this.mContext.getResources().getString(R.string.view_article_list_previous_articles));
        } else {
            holder.mContent.setVisibility(View.GONE);
            holder.mHeader.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return this.mArticles.size();
    }

    public void setOnListClickListener(OnListItemClickListener pOnListItemClickListener) {
        this.mOnListItemClickListener = pOnListItemClickListener;
    }

    /**
     * OnClick for Article cards
     */
    public interface OnListItemClickListener {
        void onItemClick(Article pArticle, int position);
    }

    /**
     * @param pContext
     * @return Article Parent View
     */
    private LinearLayout getMainLinearLayout(final Context pContext) {
        LinearLayout view = new LinearLayout(pContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        view.setOrientation(LinearLayout.VERTICAL);
        return view;
    }

    /**
     * @param pContext
     * @return Article View
     */
    private LinearLayout getLinearLayout(final Context pContext) {
        LinearLayout view = new LinearLayout(pContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(params);
        view.setOrientation(LinearLayout.VERTICAL);
        view.setBackground(pContext.getResources().getDrawable(R.drawable.article_background));
        return view;
    }

    /**
     * @param pContext
     * @param pMain
     * @return ImageView based on main or sub article type
     */
    private ImageView getImage(final Context pContext, final boolean pMain) {
        ImageView image = new ImageView(pContext);
        image.setScaleType(ImageView.ScaleType.FIT_XY);
        LinearLayout.LayoutParams params;
        if (pMain) {
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, MAIN_ARTICLE_IMAGE_HEIGHT);
        } else {
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SUB_ARTICLE_IMAGE_HEIGHT);
        }
        image.setLayoutParams(params);
        image.setImageResource(R.drawable.loading);
        return image;
    }

    /**
     * @param pContext
     * @return Header View for sub articles
     */
    private TextView getHeader(final Context pContext) {
        TextView header = new TextView(pContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 20, 0, 0);
        header.setTextSize(TypedValue.COMPLEX_UNIT_PX, this.mContext.getResources().getDimension(R.dimen.view_article_list_previous_article_header));
        header.setTextColor(this.mContext.getResources().getColor(R.color.color_article_title));
        header.setLayoutParams(params);
        header.setTypeface(null, Typeface.BOLD);
        return header;
    }

    /**
     * @param pContext
     * @param pMain
     * @return Title View based on main or sub article
     */
    private TextView getTitle(final Context pContext, final boolean pMain) {
        TextView title = new TextView(pContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 20, 20, 20);
        params.gravity = Gravity.START;
        if (pMain) {
            title.setTextSize(TypedValue.COMPLEX_UNIT_PX, this.mContext.getResources().getDimension(R.dimen.view_article_list_title_main_size));
            title.setSingleLine(true);
        } else {
            title.setTextSize(TypedValue.COMPLEX_UNIT_PX, this.mContext.getResources().getDimension(R.dimen.view_article_list_title_size));
            title.setMaxLines(2);
        }
        title.setTextColor(this.mContext.getResources().getColor(R.color.color_article_title));
        title.setEllipsize(TextUtils.TruncateAt.END);
        title.setLayoutParams(params);
        return title;
    }

    /**
     * @param pContext
     * @return Content View
     */
    private TextView getContent(final Context pContext) {
        TextView content = new TextView(pContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(20, 0, 20, 20);
        params.gravity = Gravity.START;
        content.setTextSize(TypedValue.COMPLEX_UNIT_PX, this.mContext.getResources().getDimension(R.dimen.view_article_list_content_size));
        content.setTextColor(this.mContext.getResources().getColor(R.color.color_article_content));
        content.setMaxLines(2);
        content.setEllipsize(TextUtils.TruncateAt.END);
        content.setLayoutParams(params);
        return content;
    }

    /**
     * @return Default cache size
     */
    public int getDefaultLruCacheSize() {
        final ActivityManager activityManager = (ActivityManager) this.mContext.getSystemService(Context.ACTIVITY_SERVICE);
        final int memClassBytes = activityManager.getMemoryClass() * 1024 * 1024;
        return memClassBytes / 8;
    }
}