package com.example.newsapp1;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Post>> {

    private TextView mEmptyStateTextView;

    private static final int NEWS_LOADER_ID = 1;
    private PostAdapter postAdapter;
    private String preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView postListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        postListView.setEmptyView(mEmptyStateTextView);

        postAdapter = new PostAdapter(this, new ArrayList<Post>());

        postListView.setAdapter(postAdapter);

        postListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Post currentPost = postAdapter.getItem(position);

                Uri postUri = Uri.parse(currentPost.getUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, postUri);

                startActivity(websiteIntent);
            }
        });

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<Post>> onCreateLoader(int id, Bundle args) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        preference = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        Uri.Builder builder = new Uri.Builder();
        builder.scheme(getString(R.string.network_scheme))
                .authority(getString(R.string.guardian_api_domain))
                .appendPath(preference)
                .appendQueryParameter(getString(R.string.show_tags),getString(R.string.news_author))
                .appendQueryParameter(getString(R.string.guardian_api_key), getString(R.string.guardian_student_key));

        return (new PostLoader(this, builder.build().toString()));
    }

    @Override
    public void onLoadFinished(Loader<List<Post>> loader, List<Post> newsList) {
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        mEmptyStateTextView.setText(R.string.no_posts);

        postAdapter.clear();

        if (newsList != null && !newsList.isEmpty()) {
            postAdapter.addAll(newsList);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Post>> loader) {

        postAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
