package io.ckl.articles.modules.splash;

import android.content.Context;

import java.util.ArrayList;

import io.ckl.articles.models.Articles;
import io.ckl.articles.modules.base.BaseInterfaces;

/**
 * Created by Endy on 25/03/2017.
 */

public class SplashInterfaces {

    public interface Presenter extends BaseInterfaces.Presenter {
    }

    public interface View extends BaseInterfaces.View {
        void startMainActivity();
        Context getViewContext();
    }
}
