package io.ckl.articles.modules.main;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.ckl.articles.api_services.GreetingAPIService;
import io.ckl.articles.api_services.RetrofitArrayAPI;
import io.ckl.articles.models.Articles;
import io.ckl.articles.models.Greeting;
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

    public MainPresenter(MainInterfaces.View view) {
        this.view = view;
    }

    // region MainInterfaces.Presenter

    @Override
    public void onCreate() {
        fillArays();
    }

    @Override
    public void onArticleListPressed() {
        listArticles();
    }

    @Override
    public void onDestroy() {
        this.view = null;
    }

    // end region


    // region private

    private void fillArays() {

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

                    ArrayList<Articles> newArticles = new ArrayList<>();

                    List<Articles> StudentData = response.body();

                    for (int i = 0; i < StudentData.size(); i++) {
                        newArticles.add(StudentData.get(i));
                    }

                    view.fillList(newArticles);

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


    private void listArticles() {
//        fillArays();
        //view.showGreeting("Testing a new functiong");
    }

    // end region
}
