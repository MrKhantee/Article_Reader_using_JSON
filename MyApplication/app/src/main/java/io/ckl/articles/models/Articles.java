package io.ckl.articles.models;

/**
 * Created by Endy on 18/03/2017.
 */

public class Articles {
    public String title, website, authors, date, art_content, label, imageUrl;
    public int id;

    public Articles (String title, String website, String authors, String date, String art_content,
                     String label, String imageUrl, int id)
    {
        this.title          = title;
        this.website        = website;
        this.authors        = authors;
        this.date           = date;
        this.art_content    = art_content;
        this.label          = label;
        this.imageUrl       = imageUrl;
        this.id             = id;
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
        return art_content;
    }

    public String getLabel() {
        return label;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getId() {
        return id;
    }

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
        this.art_content = art_content;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setId(int id) {
        this.id = id;
    }
}
