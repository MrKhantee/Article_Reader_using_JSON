package io.ckl.articles.modules.read;

import io.ckl.articles.models.Articles;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Endy on 23/03/2017.
 */

public class ReadPresenter implements ReadInterfaces.Presenter {

    ReadInterfaces.View readView;

    public ReadPresenter(ReadInterfaces.View readView) {
        this.readView = readView;
    }

    // region ReadInterfaces.Presenter

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {
        this.readView = null;
    }

    // end region


    // region private

    // end region
}
