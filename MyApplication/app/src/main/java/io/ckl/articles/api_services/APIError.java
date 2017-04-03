package io.ckl.articles.api_services;

/**
 * Created by Endy on 31/03/2017.
 */
// API Error to retrieve the Errors at JSON opertaions
public class APIError {
    private int statusCode;
    private String message;

    public APIError() {
    }

    public int status() {
        return statusCode;
    }

    public String message() {
        return message;
    }
}
