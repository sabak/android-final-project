package com.saba.bogchat.model.storage;

import com.saba.bogchat.model.Contact;

/**
 * Represents storage class for contacts, it supports basic
 * methods of retrieval by index/id and storing them as well.
 */
public interface ContactStorage {

    /**
     * @param contactId Uniqie contact identifier
     * @return Contact associated with given unique id
     */
    Contact getContact(String contactId);

    /**
     * @param contact Contact object to be stored
     */
    void storeContact(Contact contact);

    /**
     * @param userId  Contact id to be updated with new avatar
     * @param newPath New filename/path of avatar
     * @return Updated contact
     */
    Contact setAvatarPath(String userId, String newPath);

    /**
     * @param contact Contact to be deleted from storage
     */
    void deleteContact(Contact contact);
}
