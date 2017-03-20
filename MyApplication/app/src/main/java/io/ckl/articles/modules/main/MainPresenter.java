package io.ckl.articles.modules.main;

import io.ckl.articles.api_services.GreetingAPIService;
import io.ckl.articles.models.Articles;
import io.ckl.articles.models.Greeting;

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

    private String title, website, authors, date, art_content, label, imageUrl;
    private Integer id;

    public MainPresenter(MainInterfaces.View view) {
        this.view = view;
    }

    // region MainInterfaces.Presenter

    @Override
    public void onCreate() {
        greet();
        fillArays();
    }

    @Override
    public void onGreetButtonPressed() {
        greet();
    }

    @Override
    public void onArticleListPressed() {
        listArticles();
    }

    @Override
    public void onDestroy() {
        this.view = null;
    }

    // end region


    // region private

    private void fillArays() {
        title = ("Obama Offers Hopeful Vision While Noting Nation's Fears");
        website = ("MacStories");
        authors = ("Graham Spencer");
        date = ("05/26/2014");
        art_content = ("In his last State of the Union address, President Obama sought to paint a" +
                " hopeful portrait. But he acknowledged that many Americans felt shut out of a " +
                "political and economic system they view as rigged.");
        id = 1;
        label = ("Politics");
        imageUrl = ("http://res.cloudinary.com/cheesecakelabs/image/upload/v1488993901" +
                "/challenge/news_01_illh01.jpg");

        Articles testArticle = new Articles (title, website, authors, date, art_content,
                label, imageUrl, id);

        view.fillList(testArticle);
    }


    private void greet() {
        Greeting greeting = GreetingAPIService.fetchGreeting();
        if (view == null) { return; }
        //view.showGreeting(greeting.getContent());
    }

    private void listArticles() {
        fillArays();
        //view.showGreeting("Testing a new functiong");
    }

    // end region
}
