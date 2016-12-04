package com.saba.bogchat.transport;

/**
 * Classes that implement this interface are responsible for signaling
 * list view update request to all of the observers.
 */
public interface ListUpdateNotifier {
    /**
     * Registers observer.
     *
     * @param observer Observer to be loaded
     */
    void registerObserver(ListUpdateObserver observer);

    /**
     * Unregisteres observer.
     *
     * @param observer Observer to be unloaded
     */
    void unregisterObserver(ListUpdateObserver observer);

    /**
     * Notifies all registered observers to update corresponding
     * list view.
     */
    void notifyObservers();
}
