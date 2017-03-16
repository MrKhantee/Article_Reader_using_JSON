package io.ckl.boilerplate.modules.main;

import io.ckl.boilerplate.api_services.GreetingAPIService;
import io.ckl.boilerplate.models.Greeting;

/**
 * This is a example presenter.
 *
 * The presenter holds a instance of the View, which is a interface implementation.
 * This view should be set as null whenever the activity reaches onDestroy().
 *
 * The presenter is responsible for the business logic, fetching models and telling the view to update.
 */
public class MainPresenter implements MainInterfaces.Presenter {

    MainInterfaces.View view;

    public MainPresenter(MainInterfaces.View view) {
        this.view = view;
    }

    // region MainInterfaces.Presenter

    @Override
    public void onCreate() {
        greet();
    }

    @Override
    public void onGreetButtonPressed() {
        greet();
    }

    @Override
    public void onDestroy() {
        this.view = null;
    }

    // end region


    // region private

    private void greet() {
        Greeting greeting = GreetingAPIService.fetchGreeting();
        if (view == null) { return; }
        view.showGreeting(greeting.getContent());
    }

    // end region
}
