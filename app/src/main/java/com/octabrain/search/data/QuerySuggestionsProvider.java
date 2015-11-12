package com.octabrain.search.data;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by Jack Kerouac on 12.11.2015.
 */
public class QuerySuggestionsProvider extends SearchRecentSuggestionsProvider {

    public final static String AUTHORITY = "com.octabrain.search.data.QuerySuggestionsProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public QuerySuggestionsProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }

}
