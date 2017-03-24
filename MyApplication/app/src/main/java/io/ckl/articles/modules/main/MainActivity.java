package io.ckl.articles.modules.main;

import android.content.Intent;
import android.os.Bundle;
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

    private static final int sortDateTag = 0, sortWebsiteTag = 1, sortLabelTag = 2,
            sortTitleTag = 3, sortAuthorTag = 4;

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

        MenuItem defaultSort = (MenuItem) findViewById(R.id.sortDate);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.setChecked(true);
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.sortDate:
                presenter.onArticleListPressed(sortDateTag);
                return true;
            case R.id.sortWebsite:
                presenter.onArticleListPressed(sortWebsiteTag);
                return true;
            case R.id.sortLabel:
                presenter.onArticleListPressed(sortLabelTag);
                return true;
            case R.id.sortTitle:
                presenter.onArticleListPressed(sortTitleTag);
                return true;
            case R.id.sortAuthor:
                presenter.onArticleListPressed(sortAuthorTag);
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
