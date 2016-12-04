package com.saba.bogchat.view.listview.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

/**
 * Created by Reaper on 25-05-2015.
 */
public class ContactsCursorAdapter extends CursorAdapter {
    private String mAvatarDir;

    public ContactsCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
        mAvatarDir = context.getString(R.string.directory_avatars);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_view_contact, parent, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.elem_avatar);
        TextView textView = (TextView) view.findViewById(R.id.list_view_contact_elem);

        ViewHolder holder = new ViewHolder();
        holder.mImageView = imageView;
        holder.mTextView = textView;
        holder.mContactIndex = cursor.getColumnIndex(
                LocalDbContract.Contact.COLUMN_NAME_CONTACT_ID);
        holder.mDisplayNameIndex = cursor.getColumnIndex(
                LocalDbContract.Contact.COLUMN_NAME_DISPLAY_NAME);
        holder.mPhoneNumberIndex = cursor.getColumnIndex(
                LocalDbContract.Contact.COLUMN_NAME_PHONE_NUMBER);
        holder.mAvatarIndex = cursor.getColumnIndex(
                LocalDbContract.Contact.COLUMN_NAME_AVATAR_PATH);

        view.setTag(holder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        /* Getting hold of tag - optimization strategy */
        ViewHolder holder = (ViewHolder) view.getTag();

        /* Getting user details from cursor */
        String userId = cursor.getString(holder.mContactIndex);
        String displayName = cursor.getString(holder.mDisplayNameIndex);
        String phoneNumber = cursor.getString(holder.mPhoneNumberIndex);
        String avatar = cursor.getString(holder.mAvatarIndex);

        /* Setting tag for user id */
        view.setTag(R.id.list_view_contact_elem, userId);

        /* Setting user display name */
        holder.mTextView.setText(displayName);

        /* Setting avatar */
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
        int mContactIndex;
        int mDisplayNameIndex;
        int mPhoneNumberIndex;
        int mAvatarIndex;
    }

}
