package com.saba.bogchat.asynchtask;

import java.util.List;

import com.saba.bogchat.model.Contact;
import com.saba.bogchat.transport.NetworkEventListener;

import android.os.AsyncTask;

/**
 * This abstract class is responsible for downloading contacts for fresh startup
 * and notifying the listener who's responsible for further contacts' handling.
 */
public abstract class ContactListDownloaderTask extends AsyncTask<String, Void, List<Contact>> {
    /* Downloaded contacts handler */
    private NetworkEventListener mNetworkEventListener;

    /*
     * Sets downloaded contacts handler
     */
    public void setNetworkEventListener(NetworkEventListener networkEventListener) {
        this.mNetworkEventListener = networkEventListener;
    }

    @Override
    protected void onPostExecute(List<Contact> contacts) {
        super.onPostExecute(contacts);
        /* Notifying observer with downloaded list of contacts */
        mNetworkEventListener.onContactListDownloaded(contacts);
    }

}
