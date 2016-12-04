package com.saba.bogchat.transport;

import java.util.ArrayList;
import java.util.List;

import com.saba.bogchat.model.Message;

/**
 * Class responsible for chat transport.
 */
public abstract class ChatTransport {
    /* List of observers */
    protected List<ChatEventListener> mListeners;

    /**
     * Simple constructor
     */
    protected ChatTransport() {
        this.mListeners = new ArrayList<>();
    }

    /**
     * Starts execution
     */
    public abstract void start();

    /**
     * Sends given message
     *
     * @param m message to be sent
     */
    public abstract void sendMessage(Message m);

    /**
     * Adds chat listener
     *
     * @param listener Listener to be added
     */
    public void addChatEventListener(ChatEventListener listener) {
        if (!mListeners.contains(listener))
            mListeners.add(listener);
    }

    /**
     * Removes chat listener
     *
     * @param listener Listener to be removed
     */
    public void removeChatEventListener(ChatEventListener listener) {
        if (mListeners.contains(listener))
            mListeners.remove(listener);
    }

}
