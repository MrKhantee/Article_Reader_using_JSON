package io.ckl.articles.modules.splash;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wang.avi.AVLoadingIndicatorView;

import io.ckl.articles.R;
import io.ckl.articles.modules.base.BaseActivity;
import io.ckl.articles.modules.main.MainActivity;

public class SplashActivity extends BaseActivity implements SplashInterfaces.View {

    SplashInterfaces.Presenter splashPresenter = new SplashPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashPresenter.onCreate();
    }

    //region MainInterfaces.View

    @Override
    public void startMainActivity()
    {
        View rootView = findViewById(R.id.splashLayout);

        startActivity(new Intent(this, MainActivity.class));

        overridePendingTransition(R.anim.main_in, R.anim.splash_out);
        finish();
    }

    //end region
}
