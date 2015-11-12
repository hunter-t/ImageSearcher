package com.octabrain.search.data;

import com.esotericsoftware.jsonbeans.Json;
import com.esotericsoftware.jsonbeans.JsonValue;
import com.octabrain.search.data.json.SearchResults;

import java.util.ArrayList;

/**
 * Created by Jack Kerouac on 12.11.2015.
 */
public class JsonWorker {

    private static Json json;

    private static Json getJson() {
        if (json == null) {
            json = new Json();
            json.setIgnoreUnknownFields(true);
        }
        return json;
    }
    
    public static SearchResults readResult(String data) {
        return getJson().fromJson(SearchResults.class, data);
    }

    @SuppressWarnings("unchecked")
    public static <T> ArrayList<T> readArray(Class<T> type, String data) {
        ArrayList<JsonValue> values = getJson().fromJson(ArrayList.class, data);
        ArrayList<T> result = new ArrayList<>(values.size());
        for (JsonValue value : values) {
            result.add(getJson().readValue(type, value));
        }
        return result;
    }

    public static <T> T readValue(Class<T> type, String data) {
        return getJson().fromJson(type, data);
    }

}
