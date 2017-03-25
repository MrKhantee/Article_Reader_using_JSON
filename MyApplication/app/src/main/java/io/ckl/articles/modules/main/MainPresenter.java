package io.ckl.articles.modules.main;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.ckl.articles.R;
import io.ckl.articles.api_services.RetrofitArrayAPI;
import io.ckl.articles.models.Articles;
import io.realm.Realm;
import io.realm.RealmConfiguration;
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

    Context presenterContext;

    private Realm realm;
    SharedPreferences sortPref;
    ArrayList<Articles> arrayArticles = new ArrayList<>();

    public MainPresenter(MainInterfaces.View view) {
        this.view = view;
    }

    // region MainInterfaces.Presenter

    @Override
    public void onCreate() {
        presenterContext = view.getViewContext();

        Realm.init(presenterContext);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        // Clear the realm from last time
        //Realm.deleteRealm(realmConfiguration);

        // Create a new empty instance of Realm
        realm = Realm.getInstance(realmConfiguration);

        sortPref = presenterContext.getSharedPreferences(
                presenterContext.getString(R.string.prefFile), Context.MODE_PRIVATE);

        fillArrays();

    }

    @Override
    public void onArticleListPressed(String sortType, boolean decreasing) {
        sortArticles(sortType, decreasing);
    }

    @Override
    public void onDestroy() {
        realm.close();
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

                    if (StudentData.size() != realm.where(Articles.class).findAll().size()) {
                        realm.beginTransaction();
                        Collection<Articles> realmArticles = realm.copyToRealm(StudentData);
                        realm.commitTransaction();
                    }

                    arrayArticles = new ArrayList<Articles>(realm.where(Articles.class).findAll());

                    sortArticles(
                            sortPref.getString(presenterContext.getString(R.string.prefKeySortBy),
                                    presenterContext.getString(R.string.menuSortDate)),
                            sortPref.getBoolean(presenterContext.getString(R.string.prefKeySortDesc),
                                    false));

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


    private void sortArticles(String sortType, boolean decreasing) {
        Collections.sort(arrayArticles, new Comparator<Articles>() {
            @Override
            public int compare(Articles o1, Articles o2) {

                if (decreasing) {
                    Articles temp = o1;
                    o1 = o2;
                    o2 = temp;
                }

                if (sortType.equals(presenterContext.getString(R.string.menuSortWebsite)))
                {
                    return o1.getWebsite().compareTo(o2.getWebsite());
                }
                else if (sortType.equals(presenterContext.getString(R.string.menuSortLabel)))
                {
                    return o1.getTags().get(0).getLabel().compareTo(o2.getTags().get(0).getLabel());
                }
                else if (sortType.equals(presenterContext.getString(R.string.menuSortTitle)))
                {
                    return o1.getTitle().compareTo(o2.getTitle());
                }
                else if (sortType.equals(presenterContext.getString(R.string.menuSortAuthor)))
                {
                    return o1.getAuthors().compareTo(o2.getAuthors());
                }
                else  // If date, do the invert to get the newest (the higher date) first
                {
                    return o2.getDate().compareTo(o1.getDate());
                }
            }
        });

        SharedPreferences.Editor editor = sortPref.edit();
        editor.putString(presenterContext.getString(R.string.prefKeySortBy), sortType);
        editor.putBoolean(presenterContext.getString(R.string.prefKeySortDesc), decreasing);
        editor.apply();

        view.setMenuTitle(sortType, decreasing);

        view.fillList(arrayArticles);
    }

    // end region
}
