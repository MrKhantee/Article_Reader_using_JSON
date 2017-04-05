package io.ckl.articles.modules.main;

import android.content.Context;

import java.util.ArrayList;

import io.ckl.articles.models.Articles;
import io.ckl.articles.modules.base.BaseInterfaces;

/**
 * Created by Endy on 15/03/2017.
 */
public class MainInterfaces {

    public interface Presenter extends BaseInterfaces.Presenter {
        void sortAticleList(String sortType, boolean descending);
    }

    public interface View extends BaseInterfaces.View {
        void fillList(ArrayList<Articles> infoArticle);
        void setMenuTitle(String sortBy, boolean sortDesc);
        Context getViewContext();
    }
}
