package io.ckl.articles.modules.main;

import io.ckl.articles.modules.base.BaseInterfaces;

public class MainInterfaces {

    public interface Presenter extends BaseInterfaces.Presenter {
        void onGreetButtonPressed();
    }

    public interface View extends BaseInterfaces.View {
        void showGreeting(String greeting);
    }

}
