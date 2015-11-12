package com.octabrain.search.events;

import com.octabrain.search.data.json.Result;

/**
 * Created by Jack Kerouac on 12.11.2015.
 */
public class Event<T> {

    private T message;

    public String name;

    public Event(String name) {
        this.name = name;
    }

    public T getMessage() {
        return message;
    }

    public void setMessage(T message) {
        this.message = message;
    }


    /* ---- Static event list below ---- */

    public static final Event<Result> THUMBNAIL_LOADED = new Event<>("thumbnail_loaded");

}
