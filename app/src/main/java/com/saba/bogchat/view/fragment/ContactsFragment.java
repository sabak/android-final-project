package com.saba.bogchat.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;

import com.saba.bogchat.R;
import com.saba.bogchat.db.LocalDbHelper;
import com.saba.bogchat.transport.ListUpdateNotifier;
import com.saba.bogchat.transport.ListUpdateObserver;
import com.saba.bogchat.view.ChatActivity;
import com.saba.bogchat.view.listview.adapter.ContactsCursorAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactsFragment extends Fragment implements ListUpdateObserver {
    public final static String USER_ID = "fragment.contacts.user_id";
    private ListUpdateNotifier mNotifier;
    private LocalDbHelper mDbHelper;
    private ContactsCursorAdapter mAdapter;
    private ListView mListView;
    private EditText mFilterText;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ContactsFragment.
     */
    public static ContactsFragment newInstance() {
        ContactsFragment fragment = new ContactsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ContactsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDbHelper = new LocalDbHelper(getActivity());
        Cursor cursor = mDbHelper.getContactList();
        mAdapter = new ContactsCursorAdapter(getActivity(), cursor);
        mListView.setAdapter(mAdapter);

        mFilterText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapter.getFilter().filter(s);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {
                // ingore
            }
        });

        mAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence constraint) {
                String displayName = constraint.toString();
                return mDbHelper.getFilteredContactList(displayName);
            }
        });

        mNotifier.registerObserver(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        mListView = (ListView) view.findViewById(R.id.list_view_contacts);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String userId = (String) view.getTag(R.id.list_view_contact_elem);
                startChat(userId);
            }
        });
        mFilterText = (EditText) view.findViewById(R.id.search_filter);

        return view;
    }

    private void startChat(String userId) {
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra(USER_ID, userId);
        startActivity(intent);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mNotifier = (ListUpdateNotifier) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mNotifier.unregisterObserver(this);
    }

    @Override
    public void signalList() {
        Cursor newCursor = mDbHelper.getContactList();
        mAdapter.changeCursor(newCursor);
    }

}
