package io.ckl.articles.models;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Endy on 18/03/2017.
 */
// Articles Database
public class Articles extends RealmObject{
    // Info the be fetched with the JSON
    private String title, website, authors, date, content, image_url;
    private RealmList<Tags> tags;   // Tags Database list to be fetched (array inside array of JSON)

    // Local info of Articles marked as "Read"
    private boolean read;

    public Articles () {
    }

    public Articles (String title, String website, String authors, String date, String content,
                     String image_url, RealmList<Tags> tags, boolean read)
    {
        this.title      = title;
        this.website    = website;
        this.authors    = authors;
        this.date       = date;
        this.content    = content;
        this.image_url  = image_url;
        this.tags       = tags;
        this.read       = read;
    }

    public String getTitle() {
        return title;
    }

    public String getWebsite() {
        return website;
    }

    public String getAuthors() {
        return authors;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public List<Tags> getTags() {
        return tags;
    }

    public String getImageUrl() {
        return image_url;
    }

    public boolean getRead() { return read;}

    public void setTitle(String title) {
        this.title = title;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setContent(String art_content) {
        this.content = art_content;
    }

    public void setTags(RealmList<Tags> tags) {
        this.tags = tags;
    }

    public void setImageUrl(String imageUrl) {
        this.image_url = imageUrl;
    }

    public void setRead(boolean read) {this.read = read;}
}
