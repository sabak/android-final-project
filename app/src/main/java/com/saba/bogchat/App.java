package com.saba.bogchat;

import android.app.Application;

import com.saba.bogchat.db.LocalDbHelper;
import com.saba.bogchat.db.manager.ContactManager;
import com.saba.bogchat.db.manager.MessageManager;
import com.saba.bogchat.model.storage.ContactStorage;
import com.saba.bogchat.model.storage.MessageStorage;
import com.saba.bogchat.transport.ChatTransport;
import com.saba.bogchat.transport.TestChatTransport;

/**
 * Custom App for chat simulation.
 */
public class App extends Application {
    private static App singleton;
    private static ChatTransport mSimulation;

    public static App getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        LocalDbHelper mHelper = new LocalDbHelper(this);
        ContactStorage contactStorage = new ContactManager(mHelper);
        MessageStorage messageStorage = new MessageManager(mHelper);
        mSimulation = new TestChatTransport(messageStorage, contactStorage);
    }

    public ChatTransport getChatTransport() {
        return mSimulation;
    }

}
