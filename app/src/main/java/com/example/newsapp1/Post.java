package com.example.newsapp1;

public class Post {

    private String msectionName;

    private String mDate;

    private String mTitle;

    private String mUrl;

    private String mAuthorFirstName;
    private String mAuthorLastName;

    public Post(String sectionName, String date, String title, String url, String authorFirstName, String authorLastName) {
        msectionName = sectionName;
        mDate = date;
        mTitle = title;
        mUrl = url;
        mAuthorFirstName = authorFirstName;
        mAuthorLastName = authorLastName;
    }

    public String getSectionName() {
        return msectionName;
    }

    public String getDate() {
        return mDate;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getAuthorFirstName() {
        return mAuthorFirstName;
    }

    public String getAuthorLastName() {
        return mAuthorLastName;
    }
}

