package io.ckl.articles.api_services;

import java.util.List;

import io.ckl.articles.models.Articles;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Endy on 20/03/2017.
 */

public interface RetrofitArrayAPI {
    /*
     * Retrofit get annotation with our URL
     * And our method that will return us details of Articles.
    */
    @GET("challenge")
    Call<List<Articles>> getArticlesDetails();
}
