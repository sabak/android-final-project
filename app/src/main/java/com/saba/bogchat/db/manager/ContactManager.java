package com.saba.bogchat.db.manager;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.saba.bogchat.db.LocalDbContract;
import com.saba.bogchat.model.Contact;
import com.saba.bogchat.model.storage.ContactStorage;

/**
 * ContactManager is a local database storage responsible for
 * contact handling with operations such as getting contacts
 * in alphabetical order or storing new one into it, etc.
 */
public class ContactManager implements ContactStorage {
    /* Actual underlying database */
    private SQLiteOpenHelper mHelper;

    /**
     * Constructs new ContactManager class with given custom SQLite helper
     *
     * @param sqLiteOpenHelper (Presumably) custom sqlite helper instance
     */
    public ContactManager(SQLiteOpenHelper sqLiteOpenHelper) {
        this.mHelper = sqLiteOpenHelper;
    }

    @Override
    public Contact getContact(String contactId) {
        SQLiteDatabase db = mHelper.getReadableDatabase();

        /*
         * Defines a projection that specifies which columns from the database
         * we will actually use after this query. In this case we need maximal
         * information.
         */
        String[] projection = {
                LocalDbContract.Contact.COLUMN_NAME_CONTACT_ID,
                LocalDbContract.Contact.COLUMN_NAME_DISPLAY_NAME,
                LocalDbContract.Contact.COLUMN_NAME_PHONE_NUMBER,
                LocalDbContract.Contact.COLUMN_NAME_AVATAR_PATH,
        };

        /*
         * Where clause selection and it's corresponding arguments - basically
         * just contact id.
         */
        String selection = LocalDbContract.Contact.COLUMN_NAME_CONTACT_ID + " = ?";
        String[] selectionArgs = {contactId};

        /* Executing select query */
        Cursor cursor = db.query(
                LocalDbContract.Contact.TABLE_NAME,      // The table to query
                projection,                              // The columns to return
                selection,                               // The columns for the WHERE clause
                selectionArgs,                           // The values for the WHERE clause
                null,                                    // don't group the rows
                null,                                    // don't filter by row groups
                null                                     // The sort order
        );

        /* Parsing cursor and constructing contat object */
        Contact contact = null;
        if (cursor.moveToFirst()) {
            String userId = cursor.getString(0);
            String displayName = cursor.getString(1);
            String phoneNumber = cursor.getString(2);
            String avatarFilename = cursor.getString(3);

            /* constructing contact to be returned */
            contact = new Contact(userId, displayName, phoneNumber,
                    avatarFilename);
        }
        cursor.close();
        //db.close();

        return contact;
    }

    @Override
    public void storeContact(Contact contact) {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        /* Create a new map of values, where column names are the keys*/
        ContentValues values = new ContentValues();
        values.put(LocalDbContract.Contact.COLUMN_NAME_CONTACT_ID,
                contact.getUserId());
        values.put(LocalDbContract.Contact.COLUMN_NAME_DISPLAY_NAME,
                contact.getDisplayName());
        values.put(LocalDbContract.Contact.COLUMN_NAME_PHONE_NUMBER,
                contact.getPhoneNumber());
        values.put(LocalDbContract.Contact.COLUMN_NAME_AVATAR_PATH,
                contact.getAvatarPath());

        /* Performing insertion query */
        db.insert(
                LocalDbContract.Contact.TABLE_NAME,
                null,
                values);
        //db.close();
    }

    @Override
    public Contact setAvatarPath(String userId, String newPath) {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        /* New value for one column */
        ContentValues values = new ContentValues();
        values.put(LocalDbContract.Contact.COLUMN_NAME_AVATAR_PATH, newPath);

        /* Which row to update, based on the user id  */
        String selection = LocalDbContract.Contact.COLUMN_NAME_CONTACT_ID + " = ?";
        String[] selectionArgs = {userId};

        /* performing update query */
        db.update(
                LocalDbContract.Contact.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        /* Returning updated contact from database */
        //db.close();

        return getContact(userId);
    }

    @Override
    public void deleteContact(Contact contact) {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        /* Defines 'where' part of query. */
        String selection = LocalDbContract.Contact.COLUMN_NAME_CONTACT_ID + " = ?";

        /* Specify arguments in placeholder (?) order. */
        String[] selectionArgs = {contact.getUserId()};

        /* Executing delete query */
        db.delete(LocalDbContract.Contact.TABLE_NAME, selection, selectionArgs);
        //db.close();
    }

}
