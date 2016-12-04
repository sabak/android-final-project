package com.saba.bogchat.transport;

import com.saba.bogchat.model.Message;

/**
 * Chat observer
 */
public interface ChatEventListener {
    /**
     * Trigger for incoming message
     *
     * @param m Incoming message
     */
    void onIncomingMsg(Message m);

    /**
     * Trigger for outgoing message
     *
     * @param m Outgoing message
     */
    void onOutgoingMsg(Message m);

    /**
     * Trigger for contact status change
     *
     * @param contactId Unique id for contact
     * @param isOnline  boolean representing contact's online status
     */
    void onStatusChanged(long contactId, boolean isOnline);
}
