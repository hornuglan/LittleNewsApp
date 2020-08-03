package com.example.newsapp1;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class PostLoader extends AsyncTaskLoader<List<Post>> {

    private static final String LOG_TAG = PostLoader.class.getName();

    private String mUrl;

    public PostLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Post> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        List<Post> posts = QueryUtils.fetchPostData(mUrl);
        return posts;
    }
}
