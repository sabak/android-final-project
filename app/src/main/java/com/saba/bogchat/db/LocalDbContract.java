package com.saba.bogchat.db;

import android.provider.BaseColumns;

/**
 * Contract class representing SQLite scheme.
 */
public final class LocalDbContract {
    /* Handy variables for statement construction */
    private static final String COMMA_SEP = ",";
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";

    /*
     * To prevent someone from accidentally instantiating the contract class,
     * give it an empty constructor
     */
    public LocalDbContract() {
    }

    /* Contacts table */
    public abstract static class Contact implements BaseColumns {
        public static final String TABLE_NAME = "contact";
        public static final String COLUMN_NAME_CONTACT_ID = "contact_id";
        public static final String COLUMN_NAME_DISPLAY_NAME = "display_name";
        public static final String COLUMN_NAME_PHONE_NUMBER = "phone_number";
        public static final String COLUMN_NAME_AVATAR_PATH = "avatar_path";
    }

    /* Messages table */
    public abstract static class Message implements BaseColumns {
        public static final String TABLE_NAME = "message";
        public static final String COLUMN_NAME_TEXT = "text";
        public static final String COLUMN_NAME_DATE = "date";
    }

    /* Conversations table */
    public abstract static class Conversation implements BaseColumns {
        public static final String TABLE_NAME = "conversation";
        public static final String COLUMN_NAME_MEMBER_ID = "member_id";
        public static final String COLUMN_NAME_MESSAGE_ID = "message_id";
        public static final String COLUMN_NAME_IS_FROM_MEMBER = "is_from_member";
        public static final String COLUMN_NAME_IS_READ = "is_read";
    }

    /*
     * Creation and deletion queries for Contacts table
     */
    public static final String SQL_CREATE_CONTACTS =
            "CREATE TABLE " + Contact.TABLE_NAME + " (" +
                    Contact._ID + " INTEGER PRIMARY KEY," +
                    Contact.COLUMN_NAME_CONTACT_ID + TEXT_TYPE + COMMA_SEP +
                    Contact.COLUMN_NAME_DISPLAY_NAME + TEXT_TYPE + COMMA_SEP +
                    Contact.COLUMN_NAME_PHONE_NUMBER + TEXT_TYPE + COMMA_SEP +
                    Contact.COLUMN_NAME_AVATAR_PATH + TEXT_TYPE + " );";

    public static final String SQL_DELETE_CONTACTS =
            "DROP TABLE IF EXISTS " + Contact.TABLE_NAME + ";";

    /*
     * Creation and deletion queries for Messages table
     */
    public static final String SQL_CREATE_MESSAGE =
            "CREATE TABLE " + Message.TABLE_NAME + " (" +
                    Message._ID + " INTEGER PRIMARY KEY," +
                    Message.COLUMN_NAME_TEXT + TEXT_TYPE + COMMA_SEP +
                    Message.COLUMN_NAME_DATE + INTEGER_TYPE + " );";

    public static final String SQL_DELETE_MESSAGE =
            "DROP TABLE IF EXISTS " + Message.TABLE_NAME + ";";

    /*
     * Creation and deletion queries for Conversations table
     */
    public static final String SQL_CREATE_CONVERSATION =
            "CREATE TABLE " + Conversation.TABLE_NAME + " (" +
                    Conversation._ID + " INTEGER PRIMARY KEY," +
                    Conversation.COLUMN_NAME_MEMBER_ID + INTEGER_TYPE + COMMA_SEP +
                    Conversation.COLUMN_NAME_MESSAGE_ID + INTEGER_TYPE + COMMA_SEP +
                    Conversation.COLUMN_NAME_IS_FROM_MEMBER + INTEGER_TYPE + COMMA_SEP +
                    Conversation.COLUMN_NAME_IS_READ + INTEGER_TYPE + COMMA_SEP +
                    "FOREIGN KEY(" + Conversation.COLUMN_NAME_MEMBER_ID + ") REFERENCES " +
                    Contact.TABLE_NAME + "(" + Contact._ID + ") ON DELETE CASCADE" + COMMA_SEP +
                    "FOREIGN KEY(" + Conversation.COLUMN_NAME_MESSAGE_ID + ") REFERENCES " +
                    Message.TABLE_NAME + "(" + Message._ID + ") ON DELETE CASCADE" + " );";

    public static final String SQL_DELETE_CONVERSATION =
            "DROP TABLE IF EXISTS " + Conversation.TABLE_NAME + ";";
}
