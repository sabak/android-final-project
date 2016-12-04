package com.saba.bogchat.view.fragment.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.saba.bogchat.R;
import com.saba.bogchat.view.fragment.ContactsFragment;
import com.saba.bogchat.view.fragment.RecentChatsFragment;
import com.saba.bogchat.view.fragment.SettingsFragment;

/**
 * Created by Reaper on 17-05-2015.
 */
public class MainFragmentPagerAdapter extends FragmentPagerAdapter {
    private int[] imageResId = {
            R.drawable.drawable_tab_recents,
            R.drawable.drawable_tab_contacts,
            R.drawable.drawable_tab_settings
    };

    public MainFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return RecentChatsFragment.newInstance();
            case 1:
                return ContactsFragment.newInstance();
            case 2:
                return SettingsFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return imageResId.length;
    }

    /**
     * Returns drawable icon for given position.
     *
     * @param position Tab position
     * @return Corresponding drawable icon id for given position (tab)
     */
    public int getDrawableId(int position) {
        return imageResId[position];
    }

}
