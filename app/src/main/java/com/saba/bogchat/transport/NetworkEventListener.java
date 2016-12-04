package com.saba.bogchat.transport;

import android.graphics.Bitmap;

import java.util.List;

import com.saba.bogchat.model.Contact;

/**
 * Observer for contact list and avatar download tasks
 */
public interface NetworkEventListener {
    /**
     * @param contacts Downloaded list of contacts
     */
    void onContactListDownloaded(List<Contact> contacts);

    /**
     * @param imgData   Downloaded avatar bytestream
     * @param contactId Contact USERNAME corresponding with avatar
     */
    void onAvatarDownloaded(Bitmap imgData, String contactId);

    /**
     * @param errorCode Download error code
     * @param errorMsg  Download error message
     */
    void onError(int errorCode, String errorMsg);
}
