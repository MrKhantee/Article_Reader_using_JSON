package io.ckl.articles.modules.main;

import android.content.Context;

import java.util.ArrayList;

import io.ckl.articles.models.Articles;
import io.ckl.articles.modules.base.BaseInterfaces;

public class MainInterfaces {

    public interface Presenter extends BaseInterfaces.Presenter {
        void onArticleListPressed(String sortType, boolean decreasing);

    }

    public interface View extends BaseInterfaces.View {
        void fillList(ArrayList<Articles> infoArticle);
        void setMenuTitle(String sortBy, boolean sortDec);
        Context getViewContext();
    }

}
