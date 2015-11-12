package com.octabrain.search.data;

import android.util.LruCache;

import com.octabrain.search.data.json.Result;
import com.octabrain.search.data.json.SearchResults;

/**
 * Created by Jack Kerouac on 12.11.2015.
 */
public class ResultsDataSource {

    private static LruCache<String, SearchResults> cache;

    static {
        int cacheSize = (int) (Runtime.getRuntime().maxMemory() / 1024);
        cache = new LruCache<String, SearchResults>(cacheSize) {
            @Override
            protected int sizeOf(String key, SearchResults value) {
                int size = 0;
                for (Result result : value.getResults()) {
                    if (result.getImage() != null)
                        size += result.getImage().getByteCount() / 1024;
                    if (result.getThumbnail() != null)
                        size += result.getThumbnail().getByteCount() / 1024;
                }
                return size;
            }
        };
    }

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
