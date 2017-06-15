package com.pcrssblog.screen.article.detail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.pcrssblog.api.model.Article;
import com.pcrssblog.util.ViewUtils;

/**
 * Initializes ArticleDetail Presenter and View.
 */
public class ArticleDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final FrameLayout container = new FrameLayout(this);
        setContentView(container);

        // Initialize ArticleDetailView with passing user selected article instance
        final ArticleDetailView articleDetailView = ArticleDetailView.newInstance();
        final Article article = getIntent().getParcelableExtra(ArticleDetailView.ARGUMENT_ARTICLE);
        if (article != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(ArticleDetailView.ARGUMENT_ARTICLE, article);
            articleDetailView.setArguments(bundle);
        }
        ViewUtils.addViewToActivity(getSupportFragmentManager(), articleDetailView);

        // Initialize ArticleDetailPresenter
        new ArticleDetailPresenter(articleDetailView, this);
    }
}