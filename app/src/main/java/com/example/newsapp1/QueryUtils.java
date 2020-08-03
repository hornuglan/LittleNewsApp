package com.example.newsapp1;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static List<Post> fetchPostData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<Post> posts = extractFeatureFromJson(jsonResponse);

        return posts;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the post JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Post> extractFeatureFromJson(String postJSON) {
        if (TextUtils.isEmpty(postJSON)) {
            return null;
        }

        List<Post> posts = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(postJSON);
            JSONObject responseJSon = baseJsonResponse.getJSONObject("response");
            JSONArray newsPostArray = responseJSon.getJSONArray("results");

            for (int i = 0; i < newsPostArray.length(); i++) {
                JSONObject currentPost = newsPostArray.getJSONObject(i);
                String sectionName = currentPost.getString("sectionName");
                String time = currentPost.getString("webPublicationDate");
                String title = currentPost.getString("webTitle");
                String url = currentPost.getString("webUrl");
                JSONArray tags = currentPost.getJSONArray("tags");
                String authorFirstName = null;
                String authorLastName = null;

                if (tags != null && tags.length() >= 0){
                    for (int j=0; j < tags.length(); j++){
                        JSONObject author = tags.getJSONObject(j);
                        authorFirstName = author.getString("firstName");
                        authorLastName = author.getString("lastName");
                    }
                }

                Post newsPost = new Post(sectionName, time, title, url, authorFirstName, authorLastName);
                posts.add(newsPost);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the post JSON results", e);
        }

        return posts;
    }

}

