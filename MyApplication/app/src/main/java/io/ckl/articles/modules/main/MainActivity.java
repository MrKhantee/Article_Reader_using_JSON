package io.ckl.articles.modules.main;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import io.ckl.articles.R;
import io.ckl.articles.models.Articles;
import io.ckl.articles.models.ArticlesAdapter;
import io.ckl.articles.modules.base.BaseActivity;

/**
 * This activity implements the View protocol.
 * The view should be passive. It only tells the presenter that events have happen and shows information that comes from the presenter.
 * Should set the presenter to null whenever onDestroy() is called
 */
public class MainActivity extends BaseActivity implements MainInterfaces.View {

    MainInterfaces.Presenter presenter = new MainPresenter(this);

//    @BindView(R.id.greetingTextView)
//    TextView greetingTextView;
    @BindView(R.id.listArticles)
    ListView listArticles;

    static ArticlesAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ArrayList<Articles> arrayOfArticles = new ArrayList<>();

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


    //region MainInterfaces.View

    @Override
    public void fillList(Articles infoArticle) {
        mAdapter.add(infoArticle);
    }

//    @Override
//    public void showGreeting(String greeting) {
//        greetingTextView.setText(greeting);
//    }

    //end region


    //region click listeners

//    @OnClick(R.id.greetButton)
//    public void onGreetButtonClicked() {
//        if (presenter == null) { return; }
//        presenter.onGreetButtonPressed();
//    }

    @OnItemClick(R.id.listArticles)
    public void onItemClicked() {
        //mAdapter.notifyDataSetChanged();
        if (presenter == null) { return; }
        presenter.onArticleListPressed();
    }
    //end region
}
