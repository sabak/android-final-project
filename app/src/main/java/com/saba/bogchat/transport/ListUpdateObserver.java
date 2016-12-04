package com.saba.bogchat.transport;

/**
 * Classes implementing this interface are responsible for handling
 * signal requesting to update list view.
 */
public interface ListUpdateObserver {
    /**
     * Notifies list to be updated. Is invoked when underlying data
     * storage is modified.
     */
    void signalList();
}
