package io.ckl.articles.modules.splash;

import android.os.Handler;
import android.util.Log;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;

import io.ckl.articles.api_services.APIError;
import io.ckl.articles.api_services.RetrofitArrayAPI;
import io.ckl.articles.models.Articles;
import io.ckl.articles.modules.read.ReadInterfaces;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.internal.IOException;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Endy on 25/03/2017.
 */

public class SplashPresenter implements SplashInterfaces.Presenter {

    SplashInterfaces.View splashView;

    private Realm realm;

    public SplashPresenter(SplashInterfaces.View splashView) {
        this.splashView = splashView;
    }

    // region MainInterfaces.Presenter

    @Override
    public void onCreate() {
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
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
        // Database number of Articles verification - Fixed at 6 because of the Project requirements
        if (realm.where(Articles.class).findAll().size() == 6) {
            // If all the 6 the Articles were downloaded, start the Main Screen after 1.5 secs
            int SPLASH_TIME_OUT = 1500;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    splashView.startMainActivity();
                }
            }, SPLASH_TIME_OUT);
        }
        else {
            // If there's no Article downloaded, start the 3 dots animation and the download
            splashView.startDownloadAnimation();

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
                        // Check if the request was successful
                        if (response.isSuccessful()) {
                            List<Articles> StudentData = response.body();

                            Log.d("onCall", "Downloading");

                            realm.beginTransaction();
                            Collection<Articles> realmArticles = realm.copyToRealm(StudentData);
                            realm.commitTransaction();

                            splashView.startMainActivity();
                        }
                        else {
                        // Not entered in all tests, but it's better to have this option if happens

                            Log.d("onCall", "Not Success!");

                            Converter<ResponseBody, APIError> converter = retrofit
                                    .responseBodyConverter(APIError.class, new Annotation[0]);

                            APIError error;

                            try {
                                error = converter.convert(response.errorBody());
                            } catch (IOException e) {
                                error = new APIError();
                            }

                            String errorMessage = "Message: " + error.message()
                                    + " - Status: " + error.status();
                            Log.d("onCall", errorMessage);

                            splashView.showErrorMessage("Request not Successful\n" + errorMessage
                                    + "\nCheck the server and try again...");
                        }
                    } catch (Exception e) {
                        Log.d("onCall", "There is an error");
                        splashView.showErrorMessage("There is an error... Check the server and try again...");
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<List<Articles>> call, Throwable t) {
                    Log.d("onCall", t.getMessage());
                    // t.getMessage() +
                    splashView.showErrorMessage("Check your network connection and try again...");
                }
            });
        }
    }

    // end region
}
