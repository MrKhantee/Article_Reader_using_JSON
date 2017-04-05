package io.ckl.articles.modules.splash;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;

import io.ckl.articles.R;
import io.ckl.articles.api_services.APIError;
import io.ckl.articles.api_services.RetrofitArrayAPI;
import io.ckl.articles.models.Articles;
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

    private Context splashPresenterContext;

    private Realm realm;

    public SplashPresenter(SplashInterfaces.View splashView) {
        this.splashView = splashView;
    }

    // region MainInterfaces.Presenter

    @Override
    public void onCreate() {
        splashPresenterContext = splashView.getViewContext();

        // Get the Database
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        realm = Realm.getInstance(realmConfiguration);

        // Start the download of Articles after verification
        downloadArticles();
    }

    @Override
    public void onDestroy() {
        realm.close();
        this.splashView = null;
    }

    // end region


    // region private

    private void downloadArticles() {
        // Database number of Articles verification - Fixed at 6 because of the Project requirements
        if (realm.where(Articles.class).findAll().size() == 6) {
            // If all the 6 Articles were downloaded, start the Main Screen after 1 sec
            int SPLASH_TIME_OUT = 1000;

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
                            // List all the info fetched, store at database and start MainActivity
                            List<Articles> StudentData = response.body();

                        // If the number of articles could be changed, there should be more steps
                        // Verifying which Articles was already at the Database for a right operation
                            realm.beginTransaction();
                            Collection<Articles> realmArticles = realm.copyToRealm(StudentData);
                            realm.commitTransaction();

                            splashView.startMainActivity();
                        }
                        else {
                        // Did not enter in all tests, but it's better to have this option
                        // Get the error at the request and show it
                            Converter<ResponseBody, APIError> converter = retrofit
                                    .responseBodyConverter(APIError.class, new Annotation[0]);

                            APIError error;
                            try {
                                error = converter.convert(response.errorBody());
                            } catch (IOException e) {
                                error = new APIError();
                            }

                            String errorMessage = splashPresenterContext.getString(R.string.errorMessage) + error.message() + splashPresenterContext.getString(R.string.errorStatus)
                                    + error.status();

                            Log.d("onCall", errorMessage);
                            splashView.showErrorMessage(splashPresenterContext.getString(R.string.errorRequest) + errorMessage +
                                    splashPresenterContext.getString(R.string.errorServer));
                        }
                    } catch (Exception e) {
                        Log.d("onCall", "There is an error");
                        splashView.showErrorMessage(
                                splashPresenterContext.getString(R.string.errorException));
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<List<Articles>> call, Throwable t) {
                    // In the tests, only entered here when there was some network problem
                    // Show the network error message
                    Log.d("onCall", t.getMessage());
                    splashView.showErrorMessage(
                            splashPresenterContext.getString(R.string.errorNetwork));
                }
            });
        }
    }
    // end region
}
