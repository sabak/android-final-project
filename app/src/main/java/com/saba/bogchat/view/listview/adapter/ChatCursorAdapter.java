package com.saba.bogchat.view.listview.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.saba.bogchat.R;
import com.saba.bogchat.db.LocalDbContract;
import com.saba.bogchat.model.Contact;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

/**
 * Created by Reaper on 25-05-2015.
 */
public class ChatCursorAdapter extends CursorAdapter {
    private Contact mMember;
    private Bitmap mMemberAvatar;

    public ChatCursorAdapter(Context context, Cursor c, Contact member) {
        super(context, c, 0);
        mMember = member;
        String avatarDir = context.getString(R.string.directory_avatars);

        /* Saving avatar */
        if (!isDefault(member.getAvatarPath())) {
            Bitmap thumbnail;

            /* path to ...app/app_data/avatar_dir */
            File directory = context.getDir(avatarDir, Context.MODE_PRIVATE);

            /* open directory */
            File path = new File(directory, member.getAvatarPath());
            FileInputStream fi;
            try {
                /* Trying to set images */
                fi = new FileInputStream(path);
                thumbnail = BitmapFactory.decodeStream(fi);
                mMemberAvatar = thumbnail;
                fi.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_view_chat_elem, parent, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.chat_from_avatar);
        TextView textView = (TextView) view.findViewById(R.id.message);

        ViewHolder holder = new ViewHolder();
        holder.mImageView = imageView;
        holder.mTextView = textView;
        holder.mTextIndex = cursor.getColumnIndex(
                LocalDbContract.Message.COLUMN_NAME_TEXT);
        holder.mDateIndex = cursor.getColumnIndex(
                LocalDbContract.Message.COLUMN_NAME_DATE);
        holder.mIsFromMemberIndex = cursor.getColumnIndex(
                LocalDbContract.Conversation.COLUMN_NAME_IS_FROM_MEMBER);

        view.setTag(holder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        /* Getting hold of tag - optimization strategy */
        ViewHolder holder = (ViewHolder) view.getTag();

        /* Getting user details from cursor */
        String messageText = cursor.getString(holder.mTextIndex);
        Date dateSent = new Date(cursor.getInt(holder.mDateIndex));
        boolean isFromMember = cursor.getInt(holder.mIsFromMemberIndex) > 0;

        /* Setting user display name */
        holder.mTextView.setText(messageText);

        if (isFromMember) {
            ((LinearLayout) view).setGravity(Gravity.LEFT);
            holder.mImageView.setVisibility(View.VISIBLE);
            holder.mTextView.setBackgroundDrawable(context.getResources()
                    .getDrawable(R.drawable.textview_chat_rounded_from));
        } else {
            ((LinearLayout) view).setGravity(Gravity.RIGHT);
            holder.mImageView.setVisibility(View.GONE);
            holder.mTextView.setBackgroundDrawable(context.getResources()
                    .getDrawable(R.drawable.textview_chat_rounded_to));
        }

        /* Setting avatar */
        if (mMemberAvatar != null && isFromMember) {
            holder.mImageView.setImageBitmap(mMemberAvatar);
        }
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
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
        int mTextIndex;
        int mDateIndex;
        int mIsFromMemberIndex;
    }

}
