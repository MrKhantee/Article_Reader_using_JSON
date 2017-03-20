package io.ckl.articles.modules.main;

import io.ckl.articles.models.Articles;
import io.ckl.articles.modules.base.BaseInterfaces;

public class MainInterfaces {

    public interface Presenter extends BaseInterfaces.Presenter {
        void onGreetButtonPressed();
        void onArticleListPressed();
    }

    public interface View extends BaseInterfaces.View {
        //void showGreeting(String greeting);
        void fillList(Articles infoArticle);
    }

}
