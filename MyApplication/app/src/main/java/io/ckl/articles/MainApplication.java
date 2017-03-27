package io.ckl.articles;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by Endy on 27/03/2017.
 */

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize Realm. Should only be done once when the application starts.
        Realm.init(this);
    }
}
