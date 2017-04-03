package io.ckl.articles.modules.main;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
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

    private int readRequestCode = 10;
    private int positionReadArticle = 0;

    private MenuItem menuSort;
    private MenuItem menuDecrease;

    private String actualSortStringTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mAdapter = new ArticlesAdapter(this, arrayOfArticles);
        //listArticles.setAdapter(mAdapter);

//        presenter.onCreate();
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        listArticles.setAdapter(mAdapter);
        Log.d("onMain", "Conf Changed!");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        this.menuDecrease = menu.findItem(R.id.sortDec);
        this.menuSort     = menu.findItem(R.id.menuSort);

        presenter.onCreate();           // Moved here because of the time of execution
                                        // Was too fast to load the Title of Menu Sort
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != menuSort) {
            item.setChecked(!item.isChecked());

            if (item != menuDecrease) {
                actualSortStringTag = item.getTitle().toString();
            }
            setMenuTitle(actualSortStringTag, menuDecrease.isChecked());
        }

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuSort:
                return super.onOptionsItemSelected(item);
            default:
                presenter.onArticleListPressed(actualSortStringTag, menuDecrease.isChecked());
                return true;
        }
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        Log.d("onMain", "Resulted!");
        if (requestCode == readRequestCode)
        {
            if (resultCode == RESULT_OK)
            {
                Log.d("onMain", "Position: " + String.valueOf(positionReadArticle));
                View v = listArticles.getChildAt(positionReadArticle
                                                - listArticles.getFirstVisiblePosition());
                CheckBox readToCheck = (CheckBox) v.findViewById(R.id.listCheckBox);
                readToCheck.setChecked(data.getBooleanExtra(getResources()
                        .getString(R.string.extraChecked), false));
            }
        }
    }

    //region MainInterfaces.View

    @Override
    public void setMenuTitle(String sortBy, boolean sortDec)
    {
        String menuModeStr = " (A)";
        if (sortDec) {
            menuModeStr = " (D)";
        }

        actualSortStringTag = sortBy;

        menuSort.setTitle(getResources().getString(R.string.menuSortBy) +
                actualSortStringTag + menuModeStr);

        menuDecrease.setChecked(sortDec);
        if (menuSort.hasSubMenu()) {
            SubMenu sub = menuSort.getSubMenu();
            for (int j = 0; j < sub.size(); j++) {
                if (sub.getItem(j).getTitle().toString().compareTo(actualSortStringTag) == 0) {
                    sub.getItem(j).setChecked(true);
                }
            }
        }
    }

    @Override
    public void fillList(ArrayList<Articles> infoArticle) {
        if (infoArticle.isEmpty()) { return; }
        arrayOfArticles.clear();
        arrayOfArticles.addAll(infoArticle);
        listArticles.setAdapter(mAdapter);

        setTitle(getResources().getString(R.string.app_name) + " (" + arrayOfArticles.size() + ")");
    }

    @Override
    public Context getViewContext() {
        return this;
    }

    //end region


    //region click listeners

    @OnItemClick(R.id.listArticles)
    public void onItemClicked(AdapterView<?> adapter, View v, int position,
                              long arg3) {
        if (presenter == null) { return; }

        positionReadArticle = position;

        Intent i = new Intent(this, ReadActivity.class);

        Log.d("onItemClicked", "Position: " + String.valueOf(position));

        Articles articleToRead = (Articles) mAdapter.getItem(position);
        i.putExtra(getResources().getString(R.string.extraLabel), articleToRead.getTags().get(0).getLabel());
        i.putExtra(getResources().getString(R.string.extraTitle), articleToRead.getTitle());
        i.putExtra(getResources().getString(R.string.extraImage), articleToRead.getImageUrl());
        i.putExtra(getResources().getString(R.string.extraDate), articleToRead.getDate());
        i.putExtra(getResources().getString(R.string.extraAuthor), getResources().getString(R.string.readWriten) + articleToRead.getAuthors());
        i.putExtra(getResources().getString(R.string.extraWebsite), getResources().getString(R.string.readOriginal) + articleToRead.getWebsite());
        i.putExtra(getResources().getString(R.string.extraContent), articleToRead.getContent());
        i.putExtra(getResources().getString(R.string.extraChecked), articleToRead.getRead());

        if (Build.VERSION.SDK_INT > 21) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,
                    Pair.create(v.findViewById(R.id.listImage), "artImage"),
                    Pair.create(v.findViewById(R.id.listTitle), "artTitle"),
                    Pair.create(v.findViewById(R.id.ListAuthor), "artAuthor"),
                    Pair.create(v.findViewById(R.id.listDate), "artDate"));
            startActivityForResult(i, readRequestCode, options.toBundle());
        }
        else
            startActivityForResult(i, readRequestCode);
    }
    //end region
}
