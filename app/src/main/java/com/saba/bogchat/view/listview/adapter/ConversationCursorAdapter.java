package com.saba.bogchat.view.listview.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.saba.bogchat.R;
import com.saba.bogchat.db.LocalDbContract;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

/**
 * CursorAdapter for conversations.
 */
public class ConversationCursorAdapter extends CursorAdapter {
    private String mAvatarDir;

    public ConversationCursorAdapter(Context context, Cursor c) {
        super(context, c, true);
        mAvatarDir = context.getString(R.string.directory_avatars);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_view_conversations, parent, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.elem_conversation_avatar);
        TextView textView = (TextView) view.findViewById(R.id.list_view_conversation_text);

        ViewHolder holder = new ViewHolder();
        holder.mImageView = imageView;
        holder.mTextView = textView;
        holder.mContactIdIndex = cursor.getColumnIndex(
                LocalDbContract.Contact.COLUMN_NAME_CONTACT_ID);
        holder.mDisplayNameIndex = cursor.getColumnIndex(
                LocalDbContract.Contact.COLUMN_NAME_DISPLAY_NAME);
        holder.mAvatarIndex = cursor.getColumnIndex(
                LocalDbContract.Contact.COLUMN_NAME_AVATAR_PATH);
        holder.mTextIndex = cursor.getColumnIndex(
                LocalDbContract.Message.COLUMN_NAME_TEXT);
        holder.mDateIndex = cursor.getColumnIndex("MAX(" +
                LocalDbContract.Message.COLUMN_NAME_DATE + ")");
        holder.mIsFromMemberIndex = cursor.getColumnIndex(
                LocalDbContract.Conversation.COLUMN_NAME_IS_FROM_MEMBER);
        holder.mIsReadIndex = cursor.getColumnIndex(
                LocalDbContract.Conversation.COLUMN_NAME_IS_READ);

        view.setTag(holder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        /* Getting hold of tag - optimization strategy */
        ViewHolder holder = (ViewHolder) view.getTag();

        /* Getting details from cursor */
        String contactId = cursor.getString(holder.mContactIdIndex);
        String displayName = cursor.getString(holder.mDisplayNameIndex);
        String avatar = cursor.getString(holder.mAvatarIndex);
        String messageText = cursor.getString(holder.mTextIndex);
        Date dateSent = new Date(cursor.getInt(holder.mDateIndex));
        boolean isFromMember = cursor.getInt(holder.mIsFromMemberIndex) > 0;
        boolean isRead = cursor.getInt(holder.mIsReadIndex) > 0;

        if (isFromMember) {
            messageText = displayName + ": " + messageText;
        } else {
            messageText = "Me: " + messageText;
        }

        holder.mTextView.setText(messageText);

        if (!isRead && isFromMember) {
            holder.mTextView.setTypeface(null, Typeface.BOLD);
        } else {
            holder.mTextView.setTypeface(null, Typeface.NORMAL);
        }

        if (!isDefault(avatar)) {
            Bitmap thumbnail;

            /* path to ...app/app_data/avatar_dir */
            File directory = context.getDir(mAvatarDir, Context.MODE_PRIVATE);

            /* open directory */
            File path = new File(directory, avatar);
            FileInputStream fi;
            try {
                /* Trying to set images */
                fi = new FileInputStream(path);
                thumbnail = BitmapFactory.decodeStream(fi);
                holder.mImageView.setImageBitmap(thumbnail);
                fi.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /* Setting tag for user id */
        view.setTag(R.id.list_view_conversation_text, contactId);
    }

    /*
         * Returns true if given avatar name is for default
         * image
         */
    private boolean isDefault(String avatar) {
        return avatar.contains("https");
    }

    /**
     * Helper class for adapter optimization
     */
    private static class ViewHolder {
        ImageView mImageView;
        TextView mTextView;
        int mContactIdIndex;
        int mDisplayNameIndex;
        int mAvatarIndex;
        int mTextIndex;
        int mDateIndex;
        int mIsFromMemberIndex;
        int mIsReadIndex;
    }

}
