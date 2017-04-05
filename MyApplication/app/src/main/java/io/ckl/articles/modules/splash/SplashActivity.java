package io.ckl.articles.modules.splash;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.TextView;

import io.ckl.articles.R;
import io.ckl.articles.modules.base.BaseActivity;
import io.ckl.articles.modules.main.MainActivity;

/**
 * Created by Endy on 25/03/2017.
 */

public class SplashActivity extends BaseActivity implements SplashInterfaces.View {

    SplashInterfaces.Presenter splashPresenter = new SplashPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        splashPresenter.onCreate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    //region MainInterfaces.View

    @Override
    public void startDownloadAnimation() {
        // Start the Downloading layout - with 3 dots animation
        setContentView(R.layout.activity_splash_downloading);
    }

    @Override
    public void showErrorMessage(String errorMessage) {
        // Start the Error layout - with the error message
        setContentView(R.layout.activity_splash_error);

        TextView errorText = (TextView) findViewById(R.id.errorText);
        errorText.append(errorMessage);
    }

    @Override
    public void startMainActivity() {
        //Start the main Activity with a "fade" transition
        startActivity(new Intent(this, MainActivity.class));

        overridePendingTransition(R.anim.main_in, R.anim.splash_out);
        finish();
    }

    @Override
    public Context getViewContext() {
        // Return the context to let the Presenter access the Shared Preferences
        return this;
    }
    //end region
}
