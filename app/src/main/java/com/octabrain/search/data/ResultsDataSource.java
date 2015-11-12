package com.octabrain.search.data;

import com.octabrain.search.data.json.SearchResults;

import java.util.HashMap;

/**
 * Created by Jack Kerouac on 12.11.2015.
 */
public class ResultsDataSource {

    private static HashMap<String, SearchResults> cache = new HashMap<>();

    public static SearchResults get(String query) {
        return cache.get(query);
    }

    public static boolean isEmpty(String query) {
        return cache.get(query) == null;
    }

    public static void cacheData(String query, SearchResults result) {
        cache.put(query, result);
    }

}
