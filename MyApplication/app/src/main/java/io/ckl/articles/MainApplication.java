package io.ckl.articles;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import io.realm.Realm;

/**
 * Created by Endy on 27/03/2017.
 */

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        // Initialize Realm. Should only be done once when the application starts.
        Realm.init(this);
    }
}
