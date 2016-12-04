package com.saba.bogchat.model.storage;

import com.saba.bogchat.model.Message;

/**
 * Represents storage class for messages/conversations, it supports basic
 * methods of retrieval by index/id and storing them as well.
 */
public interface MessageStorage {

    /**
     * Saves message in local storage
     *
     * @param message Message to be stored locally
     */
    void storeMessage(Message message);

    /**
     * Marks given conversation member's chat as read.
     *
     * @param userId Conversation member to be marked as read
     */
    void markAsRead(String userId);
}
