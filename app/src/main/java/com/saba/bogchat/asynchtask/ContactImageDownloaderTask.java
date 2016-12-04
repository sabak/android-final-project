package com.saba.bogchat.asynchtask;

import java.util.List;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.saba.bogchat.model.Contact;
import com.saba.bogchat.transport.NetworkEventListener;
import com.saba.bogchat.util.BitmapDownloader;

/**
 * This class is responsible for downloading avatars for fresh contacts
 * and notifying the listener who's responsible for further image handling.
 */
public class ContactImageDownloaderTask extends AsyncTask<List<Contact>, Object, Void> {
    /* Downloaded image handler */
    private NetworkEventListener mNetworkEventListener;

    /* Empty constructor to avoid accidental initialization */
    public ContactImageDownloaderTask() {
    }

    @SafeVarargs
    @Override
    protected final Void doInBackground(List<Contact>... params) {
        /* Contacts to extract avatar url from */
        List<Contact> contacts = params[0];

        BitmapDownloader downloader = new BitmapDownloader();

        /*
         * Downloading each bitmap and publishing
         * progress for downloaded avatar
         */
        for (Contact curr : contacts) {
            String avatarURL = curr.getAvatarPath();
            Bitmap bitmap = downloader.getBitmapFromURL(avatarURL);
            publishProgress(bitmap, curr.getUserId());
        }

        /* Obligatory return statement */
        return null;
    }

    /*
     * Sets downloaded image handler
     */
    public void setNetworkEventListener(NetworkEventListener networkEventListener) {
        this.mNetworkEventListener = networkEventListener;
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        super.onProgressUpdate(values);
        /*
         * Notifying observer with downloaded bitmap byte array
         * and corresponding contact id
         */
        if (mNetworkEventListener != null)
            mNetworkEventListener.onAvatarDownloaded((Bitmap) values[0], (String) values[1]);
    }

}
