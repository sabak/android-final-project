package com.saba.bogchat.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LocalDbHelper extends SQLiteOpenHelper {
    /* Local database identifiers */
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FreeUniLocalChat.db";

    public LocalDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /* Creating all necessary tables in correct order */
        db.execSQL(LocalDbContract.SQL_CREATE_CONTACTS);
        db.execSQL(LocalDbContract.SQL_CREATE_MESSAGE);
        db.execSQL(LocalDbContract.SQL_CREATE_CONVERSATION);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /* recreating all necessary tables in correct order */
        db.execSQL(LocalDbContract.SQL_DELETE_CONTACTS);
        db.execSQL(LocalDbContract.SQL_DELETE_MESSAGE);
        db.execSQL(LocalDbContract.SQL_DELETE_CONVERSATION);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    /**
     * Returns cursor pointing to a list of contacts
     *
     * @return Cursor pointing to a list of contacts with following format:
     * {id, name, phone, avatar}.
     */
    public Cursor getContactList() {
        /*
         * Defines a projection that specifies which columns from the database
         * we will actually use after this query. In this case we need maximal
         * information.
         */
        String[] projection = {
                LocalDbContract.Contact._ID,
                LocalDbContract.Contact.COLUMN_NAME_CONTACT_ID,
                LocalDbContract.Contact.COLUMN_NAME_DISPLAY_NAME,
                LocalDbContract.Contact.COLUMN_NAME_PHONE_NUMBER,
                LocalDbContract.Contact.COLUMN_NAME_AVATAR_PATH,
        };

        /* Sorting result in alphabetical order */
        String sortOrder =
                LocalDbContract.Contact.COLUMN_NAME_DISPLAY_NAME + " ASC";

        /* Executing select statement */
        SQLiteDatabase db = getReadableDatabase();

        return db.query(
                LocalDbContract.Contact.TABLE_NAME,      // The table to query
                projection,                              // The columns to return
                null,                                    // The columns for the WHERE clause
                null,                                    // The values for the WHERE clause
                null,                                    // don't group the rows
                null,                                    // don't filter by row groups
                sortOrder                                // The sort order
        );
    }

    /**
     * Returns filtered contact list
     *
     * @param displayName name used for filtering
     * @return Filtered contact list
     */
    public Cursor getFilteredContactList(String displayName) {
        if (displayName.isEmpty())
            return getContactList();
        /*
         * Defines a projection that specifies which columns from the database
         * we will actually use after this query. In this case we need maximal
         * information.
         */
        String raw = "SELECT * FROM " + LocalDbContract.Contact.TABLE_NAME +
                " WHERE " + LocalDbContract.Contact.COLUMN_NAME_DISPLAY_NAME +
                " LIKE ? ORDER BY " + LocalDbContract.Contact.COLUMN_NAME_DISPLAY_NAME +
                " ASC;";

        String[] params = {'%' + displayName + '%'};

        /* Executing select statement */
        SQLiteDatabase db = getReadableDatabase();

        return db.rawQuery(raw, params);
    }

    /**
     * Returns cursor pointing to a list of recent messages.
     *
     * @return Cursor pointing to a list of recent messages with following format:
     * {id (conversation), avatar, member id, message text, is from member, is read, date}
     */
    public Cursor getRecentMessages() {
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT " + LocalDbContract.Conversation.TABLE_NAME + "." +
                LocalDbContract.Conversation._ID + ", " +
                LocalDbContract.Contact.COLUMN_NAME_CONTACT_ID + ", " +
                LocalDbContract.Contact.COLUMN_NAME_DISPLAY_NAME + ", " +
                LocalDbContract.Contact.COLUMN_NAME_AVATAR_PATH +
                ", " + LocalDbContract.Message.COLUMN_NAME_TEXT + ", " +
                LocalDbContract.Conversation.COLUMN_NAME_IS_FROM_MEMBER + ", " +
                LocalDbContract.Conversation.COLUMN_NAME_IS_READ + ", MAX(" +
                LocalDbContract.Message.COLUMN_NAME_DATE + ") FROM " +
                LocalDbContract.Conversation.TABLE_NAME + ", " +
                LocalDbContract.Message.TABLE_NAME + ", " +
                LocalDbContract.Contact.TABLE_NAME + " WHERE " +
                LocalDbContract.Conversation.TABLE_NAME + "." +
                LocalDbContract.Conversation.COLUMN_NAME_MESSAGE_ID + " = " +
                LocalDbContract.Message.TABLE_NAME + "." +
                LocalDbContract.Message._ID + " AND " +
                LocalDbContract.Conversation.TABLE_NAME + "." +
                LocalDbContract.Conversation.COLUMN_NAME_MEMBER_ID + " = " +
                LocalDbContract.Contact.TABLE_NAME + "." +
                LocalDbContract.Contact._ID + " GROUP BY " +
                LocalDbContract.Conversation.COLUMN_NAME_MEMBER_ID +
                " ORDER BY " + LocalDbContract.Message.COLUMN_NAME_DATE +
                " DESC;";

        return db.rawQuery(query, null);
    }

    /**
     * Returns cursor pointing chat for given username
     *
     * @param userId Conversation member's id
     * @return Cursor pointing to chat messages
     */
    public Cursor getChat(String userId) {
        SQLiteDatabase db = getReadableDatabase();

        /* Raw select statement for message retrieval */
        String rawQuery = "SELECT * FROM " + LocalDbContract.Conversation.TABLE_NAME +
                ", " + LocalDbContract.Message.TABLE_NAME + " WHERE " +
                LocalDbContract.Conversation.TABLE_NAME + "." +
                LocalDbContract.Conversation.COLUMN_NAME_MESSAGE_ID + "="
                + LocalDbContract.Message.TABLE_NAME + "." +
                LocalDbContract.Message._ID + " AND " +
                LocalDbContract.Conversation.COLUMN_NAME_MEMBER_ID +
                " = ? ORDER BY DATE ASC;";

        long id = getMemberUniqueId(userId, db);

        /* Parameter values */
        String[] paramVal = {String.valueOf(id)};

        return db.rawQuery(rawQuery, paramVal);
    }

    private long getMemberUniqueId(String username, SQLiteDatabase db) {
        String rawQuery = "SELECT " + LocalDbContract.Contact._ID +
                " FROM " + LocalDbContract.Contact.TABLE_NAME +
                " WHERE " + LocalDbContract.Contact.COLUMN_NAME_CONTACT_ID +
                " = ?;";
        /* Parameter values */
        String[] paramVal = {username};

        /* Executing select statement */
        Cursor cursor = db.rawQuery(rawQuery, paramVal);

        /* Returning the number of messages per conversation */
        cursor.moveToFirst();
        long id = cursor.getLong(0);
        cursor.close();

        return id;
    }

}
