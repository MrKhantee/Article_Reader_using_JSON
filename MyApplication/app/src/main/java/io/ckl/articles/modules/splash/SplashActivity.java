package io.ckl.articles.modules.splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import io.ckl.articles.R;
import io.ckl.articles.modules.base.BaseActivity;
import io.ckl.articles.modules.main.MainActivity;

public class SplashActivity extends BaseActivity implements SplashInterfaces.View {

    SplashInterfaces.Presenter splashPresenter = new SplashPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_splash);

        splashPresenter.onCreate();
    }

    //region MainInterfaces.View

    @Override
    public void startMainActivity()
    {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public Context getViewContext() {
        return this;
    }

    //end region
}
