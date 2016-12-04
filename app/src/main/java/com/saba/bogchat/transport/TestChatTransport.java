package com.saba.bogchat.transport;

import android.os.AsyncTask;

import com.saba.bogchat.model.Contact;
import com.saba.bogchat.model.Message;
import com.saba.bogchat.model.storage.ContactStorage;
import com.saba.bogchat.model.storage.MessageStorage;

import java.util.Date;
import java.util.Random;

/**
 * For testing/simulation purposes only
 */
public class TestChatTransport extends ChatTransport {
    private MessageStorage mMessageStorage;
    private ContactStorage mContactStorage;

    public TestChatTransport(MessageStorage messageStorage, ContactStorage contactStorage) {
        mMessageStorage = messageStorage;
        mContactStorage = contactStorage;
    }

    @Override
    public void start() {
        new AsyncTask<Void, Message, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                while (true) {
                    try {
                        Thread.sleep(20000);
                        Random r = new Random();
                        int val = r.nextInt(100) + 1;
                        Contact contact = mContactStorage.getContact("" + val);
                        Date date = new Date(System.currentTimeMillis());
                        String message = "Message from " + contact.getDisplayName() +
                                ", " + date.toString();
                        Message msg = new Message(contact.getUserId(), message, date, true, true);
                        publishProgress(msg);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            protected void onProgressUpdate(Message... values) {
                super.onProgressUpdate(values);
                sendMessage(values[0]);
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void sendMessage(Message m) {
        mMessageStorage.storeMessage(m);
        for (ChatEventListener listener : mListeners) {
            listener.onIncomingMsg(m);
        }
    }

}
