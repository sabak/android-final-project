package com.saba.bogchat.db.manager;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.saba.bogchat.db.LocalDbContract;
import com.saba.bogchat.model.Message;
import com.saba.bogchat.model.storage.MessageStorage;

/**
 * MessageManager is a local database storage responsible for
 * message/conversation handling with operations such as getting
 * messages sorted by receiving time or storing new ones into it,
 * etc.
 */
public class MessageManager implements MessageStorage {
    /* Actual underlying database */
    private SQLiteOpenHelper mHelper;

    /**
     * Constructs new MessageManager class with given custom SQLite helper
     *
     * @param sqLiteOpenHelper (Presumably) custom sqlite helper instance
     */
    public MessageManager(SQLiteOpenHelper sqLiteOpenHelper) {
        mHelper = sqLiteOpenHelper;
    }

    @Override
    public void storeMessage(Message message) {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        /* Create a new map of values, where column names are the keys*/
        ContentValues values = new ContentValues();
        values.put(LocalDbContract.Message.COLUMN_NAME_TEXT,
                message.getMessage());
        values.put(LocalDbContract.Message.COLUMN_NAME_DATE,
                message.getMessageDate().getTime());

        /* Performing insertion query */
        Long newRowId = db.insert(
                LocalDbContract.Message.TABLE_NAME,
                null,
                values);

        long id = getMemberUniqueId(message.getMemberId(), db);

        /* Create a new map for conversation */
        ContentValues conValues = new ContentValues();
        conValues.put(LocalDbContract.Conversation.COLUMN_NAME_MEMBER_ID,
                id);
        conValues.put(LocalDbContract.Conversation.COLUMN_NAME_MESSAGE_ID,
                newRowId);
        conValues.put(LocalDbContract.Conversation.COLUMN_NAME_IS_FROM_MEMBER,
                message.isFromMember());
        conValues.put(LocalDbContract.Conversation.COLUMN_NAME_IS_READ,
                message.isIncoming() ? 0 : 1);

        /* Performing insertion query */
        db.insert(
                LocalDbContract.Conversation.TABLE_NAME,
                null,
                conValues);
        //db.close();
    }

    @Override
    public void markAsRead(String userId) {
        SQLiteDatabase db = mHelper.getWritableDatabase();

        /* New value for one column */
        ContentValues values = new ContentValues();
        values.put(LocalDbContract.Conversation.COLUMN_NAME_IS_READ, 1);

        /* Which row to update, based on the member id  */
        String selection = LocalDbContract.Conversation.COLUMN_NAME_MEMBER_ID + " = ?";
        long id = getMemberUniqueId(userId, db);
        String[] selectionArgs = {String.valueOf(id)};

        /* performing update query */
        db.update(
                LocalDbContract.Conversation.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        //db.close();
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
        //db.close();

        return id;
    }

}
