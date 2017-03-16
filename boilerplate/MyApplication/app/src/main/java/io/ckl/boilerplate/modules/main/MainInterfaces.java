package io.ckl.boilerplate.modules.main;

import io.ckl.boilerplate.modules.base.BaseInterfaces;

public class MainInterfaces {

    public interface Presenter extends BaseInterfaces.Presenter {
        void onGreetButtonPressed();
    }

    public interface View extends BaseInterfaces.View {
        void showGreeting(String greeting);
    }

}
