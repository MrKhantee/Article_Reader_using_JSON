package io.ckl.articles.models;

import android.content.Context;

import io.realm.RealmObject;

/**
 * Created by Endy on 20/03/2017.
 */

public class Tags extends RealmObject {

    private int id;
    private String label;

    public Tags () {

    }

    public Tags (int id, String label)
    {
        this.id     = id;
        this.label  = label;
    }

    public String getLabel() {
        return label;
    }

    public int getId() {
        return id;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setId(int id) {
        this.id = id;
    }

}
