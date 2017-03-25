package io.ckl.articles.modules.splash;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

import io.ckl.articles.R;
import io.ckl.articles.models.Articles;
import io.ckl.articles.modules.main.MainInterfaces;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Endy on 25/03/2017.
 */

public class SplashPresenter implements SplashInterfaces.Presenter {

    SplashInterfaces.View view;

    public SplashPresenter(SplashInterfaces.View view) {
        this.view = view;
    }

    // region MainInterfaces.Presenter

    @Override
    public void onCreate() {
        view.startMainActivity();
    }


    @Override
    public void onDestroy() {
        this.view = null;
    }

}
