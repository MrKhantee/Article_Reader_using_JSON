package io.ckl.articles.modules.main;

import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;

import io.ckl.articles.R;
import io.ckl.articles.api_services.RetrofitArrayAPI;
import io.ckl.articles.models.Articles;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This is a example presenter.
 *
 * The presenter holds a instance of the View, which is a interface implementation.
 * This view should be set as null whenever the activity reaches onDestroy().
 *
 * The presenter is responsible for the business logic, fetching models and telling the view to update.
 */
public class MainPresenter implements MainInterfaces.Presenter {

    MainInterfaces.View view;

    ArrayList<Articles> arrayArticles = new ArrayList<>();

    public MainPresenter(MainInterfaces.View view) {
        this.view = view;
    }

    private static final int sortDateTag = 0, sortWebsiteTag = 1, sortLabelTag = 2,
            sortTitleTag = 3, sortAuthorTag = 4;

    // region MainInterfaces.Presenter

    @Override
    public void onCreate() {
        fillArrays();
    }

    @Override
    public void onArticleListPressed(int sortType) {
        sortArticles(sortType);
    }

    @Override
    public void onDestroy() {
        this.view = null;
    }

    // end region


    // region private

    private void fillArrays() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.ckl.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitArrayAPI service = retrofit.create(RetrofitArrayAPI.class);

        Call<List<Articles>> call = service.getArticlesDetails();

        call.enqueue(new Callback<List<Articles>>() {
            @Override
            public void onResponse(Call<List<Articles>> call, Response<List<Articles>> response) {
                try {
                    List<Articles> StudentData = response.body();

                    for (int i = 0; i < StudentData.size(); i++) {
                        arrayArticles.add(StudentData.get(i));
                    }

                    sortArticles(sortDateTag);

                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<Articles>> call, Throwable t) {
                Log.d("onCall", t.toString());
            }
        });

    }


    private void sortArticles(int sortType) {
        Collections.sort(arrayArticles, new Comparator<Articles>() {
            @Override
            public int compare(Articles o1, Articles o2) {

                switch (sortType) {
                    case sortWebsiteTag:
                        return o1.getWebsite().compareTo(o2.getWebsite());
                    case sortLabelTag:
                        return o1.getTags().get(0).getLabel().
                                compareTo(o2.getTags().get(0).getLabel());
                    case sortTitleTag:
                        return o1.getTitle().compareTo(o2.getTitle());
                    case sortAuthorTag:
                        return o1.getAuthors().compareTo(o2.getAuthors());
                    default:
                        return o1.getDate().compareTo(o2.getDate());
                }
            }
        });

        for (int i = 0; i < arrayArticles.size(); i++) {
            Log.d("onSort", "Sorting : (" + String.valueOf(i) + ") - " + arrayArticles.get(i).getTitle());
        }
        view.fillList(arrayArticles);
    }

    // end region
}
