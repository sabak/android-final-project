package com.saba.bogchat;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import com.saba.bogchat.asynchtask.ContactImageDownloaderTask;
import com.saba.bogchat.asynchtask.ContactListDownloaderTask;
import com.saba.bogchat.asynchtask.URLContactListDownloaderTask;
import com.saba.bogchat.db.LocalDbHelper;
import com.saba.bogchat.db.manager.ContactManager;
import com.saba.bogchat.model.Contact;
import com.saba.bogchat.model.storage.ContactStorage;
import com.saba.bogchat.transport.ListUpdateNotifier;
import com.saba.bogchat.transport.ListUpdateObserver;
import com.saba.bogchat.transport.NetworkEventListener;
import com.saba.bogchat.view.LoginActivity;
import com.saba.bogchat.view.fragment.adapter.MainFragmentPagerAdapter;
import com.google.ui.slidingtab.SlidingTabLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements
        NetworkEventListener, ListUpdateNotifier {
    private List<ListUpdateObserver> mObservers;
    private ContactStorage mContactStorage;
    static final int LOGIN_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Checking whether login session is expired */
        if (sessionExpired()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, LOGIN_REQUEST);
        } else {
            App.getInstance().getChatTransport().start();
        }

        /* List containing observers */
        mObservers = new ArrayList<>();

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new MainFragmentPagerAdapter(getSupportFragmentManager(),
                MainActivity.this));

        // Give the SlidingTabLayout the ViewPager
        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);

        // Customize tab indicator/separator color
        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorPrimary);
            }

            @Override
            public int getDividerColor(int position) {
                return getResources().getColor(R.color.colorPrimary);
            }
        });
        slidingTabLayout.setViewPager(viewPager);

        /* Getting local database helper and its managers */
        LocalDbHelper localDbHelper = new LocalDbHelper(this); //getApplicationContext()
        mContactStorage = new ContactManager(localDbHelper);
    }

    private boolean sessionExpired() {
        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        return sharedPref.getBoolean(getString(R.string.session_expired), true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /* Check which request we're responding to */
        if (requestCode == LOGIN_REQUEST) {
            onInitialLogin();
        }
    }

    private void onInitialLogin() {
        ContactListDownloaderTask lstDwnTsk = new URLContactListDownloaderTask();
        lstDwnTsk.setNetworkEventListener(this);
        String jsonURI = getString(R.string.fresh_contacts_json_uri);
        String jsonKey = getString(R.string.json_list_arr_name);
        lstDwnTsk.execute(jsonURI, jsonKey);
    }

    @Override
    public void onContactListDownloaded(List<Contact> contacts) {
        for (Contact curr : contacts) {
            mContactStorage.storeContact(curr);
        }
        notifyObservers();
        ContactImageDownloaderTask imgDwnTsk = new ContactImageDownloaderTask();
        imgDwnTsk.setNetworkEventListener(this);
        imgDwnTsk.execute(contacts);
        App.getInstance().getChatTransport().start();
    }

    @Override
    public void onAvatarDownloaded(Bitmap imgData, String contactId) {
        String filename = "" + contactId + ".png";
        saveBitmap(imgData, filename);
        mContactStorage.setAvatarPath(contactId, filename);
        notifyObservers();
    }

    private void saveBitmap(Bitmap bitmap, String filename) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        /* path to ...app/app_data/avatar_dir */
        File directory = cw.getDir("avatar_dir", Context.MODE_PRIVATE);
        /* Create imageDir */
        File path = new File(directory, filename);

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(path);

            /* Use the compress method on the BitMap object to write image to the OutputStream */
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(int errorCode, String errorMsg) {

    }

    @Override
    public void registerObserver(ListUpdateObserver observer) {
        mObservers.add(observer);
    }

    @Override
    public void unregisterObserver(ListUpdateObserver observer) {
        mObservers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (ListUpdateObserver observer : mObservers) {
            observer.signalList();
        }
    }

}
