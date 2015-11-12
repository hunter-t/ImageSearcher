package com.octabrain.search.events;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jack Kerouac on 12.11.2015.
 */
public class EventDispatcher {

    private static EventDispatcher instance;

    private HashMap<Event, ArrayList<EventListener>> listeners = new HashMap<>();

    private static EventDispatcher getInstance() {
        if (instance == null)
            instance = new EventDispatcher();
        return instance;
    }

    public static void addListener(Event event, EventListener listener) {
        getInstance().getListeners(event, true).add(listener);
    }

    public static void removeListener(Event event, EventListener listener) {
        ArrayList<EventListener> listenersOfEvent = getInstance().getListeners(event, false);
        if (listenersOfEvent == null)
            return;
        listenersOfEvent.remove(listener);
    }

    @SuppressWarnings("unchecked")
    public static <T> void dispatch(Event<T> event, T message) {
        ArrayList<EventListener> listenersOfEvent = getInstance().getListeners(event, false);
        if (listenersOfEvent == null)
            return;
        event.setMessage(message);
        for (EventListener listener : listenersOfEvent) {
            listener.handle(event);
        }
    }

    private ArrayList<EventListener> getListeners(Event event, boolean createIfNotExist) {
        ArrayList<EventListener> listenersOfEvent = getInstance().listeners.get(event);
        if (listenersOfEvent == null && createIfNotExist) {
            listenersOfEvent = new ArrayList<>();
            getInstance().listeners.put(event, listenersOfEvent);
        }
        return listenersOfEvent;
    }

}
