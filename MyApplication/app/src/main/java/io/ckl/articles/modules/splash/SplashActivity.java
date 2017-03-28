package io.ckl.articles.modules.splash;

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

    //@BindView(R.id.downloadingDots)
    AVLoadingIndicatorView downloadingDots;

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
        startActivity(new Intent(this, MainActivity.class), ActivityOptions.
                makeCustomAnimation(this, R.anim.main_in, R.anim.splash_out).toBundle());
        finish();
    }

    //end region
}
