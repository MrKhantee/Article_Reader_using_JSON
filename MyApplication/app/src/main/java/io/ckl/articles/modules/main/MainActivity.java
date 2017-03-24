package io.ckl.articles.modules.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import butterknife.Optional;
import io.ckl.articles.R;
import io.ckl.articles.api_services.ArticlesAdapter;
import io.ckl.articles.models.Articles;
import io.ckl.articles.modules.base.BaseActivity;
import io.ckl.articles.modules.read.ReadActivity;

/**
 * This activity implements the View protocol.
 * The view should be passive. It only tells the presenter that events have happen and shows information that comes from the presenter.
 * Should set the presenter to null whenever onDestroy() is called
 */
public class MainActivity extends BaseActivity implements MainInterfaces.View {

    MainInterfaces.Presenter presenter = new MainPresenter(this);

    @BindView(R.id.listArticles)
    ListView listArticles;

    ArrayList<Articles> arrayOfArticles = new ArrayList<>();
    private ArticlesAdapter mAdapter;

    private MenuItem menuSort;
    private MenuItem sortDecrease;

    private String actualSortStringTag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mAdapter = new ArticlesAdapter(this, arrayOfArticles);
        listArticles.setAdapter(mAdapter);

        presenter.onCreate();
    }

    @Override
    protected void onDestroy() {
        if (presenter != null) {
            presenter.onDestroy();
        }
        presenter = null;
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        Log.d("onMain", "Resumed!");
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.sortDecrease = menu.findItem(R.id.sortDec);
        this.menuSort     = menu.findItem(R.id.menuSort);

    // This can be modified when develop the DataBase
        actualSortStringTag = getResources().getString(R.string.menuSortDate);
        String menuModeStr = " (I)";
        if (sortDecrease.isChecked()) {
            menuModeStr = " (D)";
        }

        menuSort.setTitle(getResources().getString(R.string.menuSortBy) + " " +
                actualSortStringTag + menuModeStr);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != menuSort) {
            item.setChecked(!item.isChecked());

            if (item != sortDecrease)
            {
                actualSortStringTag = item.getTitle().toString();
            }

            String menuModeStr = " (I)";
            if (sortDecrease.isChecked()) {
                menuModeStr = " (D)";
            }

            menuSort.setTitle(getResources().getString(R.string.menuSortBy) + " " +
                    actualSortStringTag + menuModeStr);

        }

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuSort:
                return super.onOptionsItemSelected(item);
            default:
                presenter.onArticleListPressed(actualSortStringTag, sortDecrease.isChecked());
                return true;
        }
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        Log.d("onMain", "Resulted!");
    }

    //region MainInterfaces.View

    @Override
    public void fillList(ArrayList<Articles> infoArticle) {
        if (infoArticle.isEmpty()) { return; }
        arrayOfArticles.clear();
        arrayOfArticles.addAll(infoArticle);
        //mAdapter.notifyDataSetChanged();
        listArticles.setAdapter(mAdapter);

        setTitle(getResources().getString(R.string.app_name) + " (" + arrayOfArticles.size() + ")");
    }

    @Override
    public Context getCont() {
        return this;
    }

    //end region


    //region click listeners

    @OnItemClick(R.id.listArticles)
    public void onItemClicked(AdapterView<?> adapter, View v, int position,
                              long arg3) {
        if (presenter == null) { return; }

        Intent i = new Intent(this, ReadActivity.class);
        Articles articleToRead = (Articles) mAdapter.getItem(position);
        i.putExtra("Label", articleToRead.getTags().get(0).getLabel());
        i.putExtra("Title", articleToRead.getTitle());
        i.putExtra("Image", articleToRead.getImageUrl());
        i.putExtra("Date", articleToRead.getDate());
        i.putExtra("Author", getResources().getString(R.string.readWriten) + articleToRead.getAuthors());
        i.putExtra("Website", getResources().getString(R.string.readOriginal) + articleToRead.getWebsite());
        i.putExtra("Content", articleToRead.getContent());

        startActivityForResult(i, position);
    }
    //end region
}
