package io.ckl.articles.modules.main;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
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
 * Created by Endy on 23/03/2017.
 */
public class MainActivity extends BaseActivity implements MainInterfaces.View {

    MainInterfaces.Presenter presenter = new MainPresenter(this);

    // ListView Widget
    @BindView(R.id.listArticles)
    ListView listArticles;

    // ArrayList and Adapter to use with the ListView
    ArrayList<Articles> arrayOfArticles = new ArrayList<>();
    private ArticlesAdapter mAdapter;

    // Values used for the interaction with the ReadActivity
    private static int readRequestCode = 10;
    private int positionReadArticle = 0;

    // Menu Widgets
    private MenuItem menuSort;
    private MenuItem menuDescending;

    // String to reflect sorting actual state
    private String actualSortStringTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        try {
            ActionBar ab = getSupportActionBar();
            ab.setDisplayShowHomeEnabled(true);
            ab.setLogo(R.drawable.ic_articles_art);
            ab.setDisplayUseLogoEnabled(true);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        mAdapter = new ArticlesAdapter(this, arrayOfArticles);
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
        super.onResume();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // "Restart" the ListView to fit the new layout
        listArticles.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        this.menuDescending = menu.findItem(R.id.sortDesc);
        this.menuSort       = menu.findItem(R.id.menuSort);

        // Presenter creating moved to here to set the menuSort Title correctly
        presenter.onCreate();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != menuSort) {
            // Set the new check state, prepare to sort and set the new title
            item.setChecked(!item.isChecked());

            if (item != menuDescending) {
                // Set the new sorting type to the String
                // The sorting mode (descending/ascending) is not stored in the String
                actualSortStringTag = item.getTitle().toString();
            }
            setMenuTitle(actualSortStringTag, menuDescending.isChecked());
        }

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuSort:
                // If its the "Menu Title"
                return super.onOptionsItemSelected(item);
            default:
                // If any other item is selected, do the sorting
                presenter.sortAticleList(actualSortStringTag, menuDescending.isChecked());
                return true;
        }
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (requestCode == readRequestCode) // Check if its from the Read Screen
        {
            if (resultCode == RESULT_OK)    // Check if there was changes at "Mark as Read" check
            {
                // Get the View that was clicked
                // Need the "getFirst" treatment to get the right view if scrolled the list
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
    public void setMenuTitle(String sortBy, boolean sortDesc) {
        // Set the Menu title and sync the items checked state with the "last run" (Shared Pref)
        String menuModeStr = " (A)";
        if (sortDesc) {
            menuModeStr = " (D)";
        }

        actualSortStringTag = sortBy;
        menuSort.setTitle(getResources().getString(R.string.menuSortBy) + actualSortStringTag + menuModeStr);

        menuDescending.setChecked(sortDesc);
        if (menuSort.hasSubMenu()) {
            SubMenu sortOptions = menuSort.getSubMenu();
            for (int j = 0; j < sortOptions.size(); j++) {
                MenuItem testingItem = sortOptions.getItem(j);
                if (testingItem.getTitle().toString().compareTo(actualSortStringTag) == 0) {
                    testingItem.setChecked(true);
                }
            }
        }
    }

    @Override
    public void fillList(ArrayList<Articles> infoArticle) {
        // Add the Articles at the Database to the ArrayList and start the list with the Adapter
        if (infoArticle.isEmpty()) { return; }

        arrayOfArticles.clear();
        arrayOfArticles.addAll(infoArticle);
        listArticles.setAdapter(mAdapter);

        setTitle(" (" + arrayOfArticles.size() + ")");
    }

    @Override
    public Context getViewContext() {
        // Return the context to let the Presenter access the Shared Preferences
        return this;
    }

    //end region


    //region click listeners

    @OnItemClick(R.id.listArticles)
    public void onItemClicked(AdapterView<?> adapter, View v, int position,
                              long arg3) {
        // Store the position of the Item Clicked and start ReadActivity to show the Article Content
        positionReadArticle = position;

        Intent i = new Intent(this, ReadActivity.class);

        Articles articleToRead = (Articles) mAdapter.getItem(position);
        i.putExtra(getResources().getString(R.string.extraLabel), articleToRead.getTags().get(0).getLabel());
        i.putExtra(getResources().getString(R.string.extraTitle), articleToRead.getTitle());
        i.putExtra(getResources().getString(R.string.extraImage), articleToRead.getImageUrl());
        i.putExtra(getResources().getString(R.string.extraDate), articleToRead.getDate());
        i.putExtra(getResources().getString(R.string.extraAuthor), getResources().getString(R.string.readWriten) + articleToRead.getAuthors());
        i.putExtra(getResources().getString(R.string.extraWebsite), getResources().getString(R.string.readOriginal) + articleToRead.getWebsite());
        i.putExtra(getResources().getString(R.string.extraContent), articleToRead.getContent());
        i.putExtra(getResources().getString(R.string.extraChecked), articleToRead.getRead());

        // Check the Version of the device to do the Shared Element Animation
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
