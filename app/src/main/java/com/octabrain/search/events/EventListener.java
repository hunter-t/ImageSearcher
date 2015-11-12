package com.octabrain.search.events;

/**
 * Created by Jack Kerouac on 12.11.2015.
 */
public interface EventListener<T> {

    void handle(Event<T> event);

}
