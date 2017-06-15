package com.pcrssblog.screen.article.list;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.pcrssblog.util.ViewUtils;

/**
 * Initializes ArticleList Presenter and View.
 */
public class ArticleListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final FrameLayout container = new FrameLayout(this);
        setContentView(container);

        // Initialize ArticleListView
        final ArticleListView articleListView = ArticleListView.newInstance();
        ViewUtils.addViewToActivity(getSupportFragmentManager(), articleListView);

        // Initialize ArticleListPresenter
        new ArticleListPresenter(articleListView, this);
    }
}