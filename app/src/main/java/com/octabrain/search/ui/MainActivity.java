package com.octabrain.search.ui;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.octabrain.search.R;
import com.octabrain.search.data.JsonWorker;
import com.octabrain.search.data.QuerySuggestionsProvider;
import com.octabrain.search.data.ResultsDataSource;
import com.octabrain.search.data.json.Result;
import com.octabrain.search.data.json.SearchResults;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private View progressIndicator;
    private RecyclerView resultsList;
    private CoordinatorLayout coordinator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        progressIndicator = findViewById(R.id.progress_indicator);
        resultsList = (RecyclerView) findViewById(R.id.results_list);
        coordinator = (CoordinatorLayout) findViewById(R.id.coordinator);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            final String query = intent.getStringExtra(SearchManager.QUERY);
            // Save query in suggestions.
            SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this,
                    QuerySuggestionsProvider.AUTHORITY, QuerySuggestionsProvider.MODE);
            suggestions.saveRecentQuery(query, null);

            // Check if data is already loaded; perform request if it's not.
            if (ResultsDataSource.isEmpty(query)) {
                if (!isConnected()) {
                    showMessage(getResources().getString(R.string.no_connection));
                    return;
                }
                showProgressIndicator();
                String queryURL = "";
                try {
                    queryURL = URLEncoder.encode(query, "UTF-8");
                } catch (UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                }
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String string) {
                        SearchResults results = JsonWorker.readResult(string);
                        ResultsDataSource.cacheData(query, results);
                        showResults(results);
                    }
                };

                String bingURL = "https://api.datamarket.azure.com/Bing/Search/Image?$format=json&Query=%27" + queryURL + "%27" + "&$top=15";
                final String accKey = "Ti9xTURxM0Mrb3pqd3NxSzRKMEZFOXRRSEJoVm5hSjlld2MyMjNJbmVKRTpOL3FNRHEzQytvemp3c3FLNEowRkU5dFFIQmhWbmFKOWV3YzIyM0luZUpF";

                StringRequest request = new StringRequest(Request.Method.GET, bingURL, responseListener, null) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<>(1);
                        headers.put("Authorization", "Basic " + accKey);
                        return headers;
                    }
                };
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                queue.add(request);
            } else {
                showResults(ResultsDataSource.get(query));
            }
        }
    }

    private void showProgressIndicator() {
        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        resultsList.setVisibility(View.GONE);
        progressIndicator.setVisibility(View.VISIBLE);
        progressIndicator.setAnimation(rotate);
    }

    private void showResults(SearchResults results) {
        progressIndicator.clearAnimation();
        progressIndicator.setVisibility(View.GONE);
        resultsList.setVisibility(View.VISIBLE);
    }

    private class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultHolder> {

        private ArrayList<Result> results;

        public SearchResultsAdapter(ArrayList<Result> results) {
            this.results = results;
        }

        @Override
        public SearchResultHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View resultView = LayoutInflater.from(MainActivity.this).inflate(R.layout.view_result, parent, false);
            resultView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Result result = results.get(resultsList.getChildLayoutPosition(view));
                    showImage(result.getImage());
                }
            });
            return new SearchResultHolder(resultView);
        }

        @Override
        public void onBindViewHolder(SearchResultHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return results.size();
        }
    }

    private void showImage(Bitmap image) {

    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void showMessage(String msg) {
        Snackbar.make(coordinator, msg, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(info);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
