package io.ckl.articles.modules.splash;

import android.content.Context;
import android.util.Log;

import java.util.Collection;
import java.util.List;

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
 * Created by Endy on 25/03/2017.
 */

public class SplashPresenter implements SplashInterfaces.Presenter {

    SplashInterfaces.View splashView;

    Context splashPresenterContext;

    private Realm realm;

    public SplashPresenter(SplashInterfaces.View splashView) {
        this.splashView = splashView;
    }

    // region MainInterfaces.Presenter

    @Override
    public void onCreate() {
        splashPresenterContext = splashView.getViewContext();

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();

        // Create a new empty instance of Realm
        realm = Realm.getInstance(realmConfiguration);

        Log.d("onSplash", "Started!");

        downloadArticles();
    }


    @Override
    public void onDestroy() {
        realm.close();
        this.splashView = null;
    }

    // end region


    // region private

    private void downloadArticles()
    {
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
                        Log.d("onResponse", "Downloaded");
                    }

                    Log.d("onLoading", "Flag Download true");
                    splashView.startMainActivity();

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
    // end region
}
