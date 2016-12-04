package com.saba.bogchat.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.saba.bogchat.App;
import com.saba.bogchat.R;
import com.saba.bogchat.db.LocalDbHelper;
import com.saba.bogchat.model.Message;
import com.saba.bogchat.transport.ChatEventListener;
import com.saba.bogchat.view.ChatActivity;
import com.saba.bogchat.view.listview.adapter.ConversationCursorAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecentChatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecentChatsFragment extends Fragment implements ChatEventListener {
    public final static String USER_ID = "fragment.conversations.user_id";
    private LocalDbHelper mDbHelper;
    private ConversationCursorAdapter mAdapter;
    private ListView mListView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RecentChatsFragment.
     */
    public static RecentChatsFragment newInstance() {
        RecentChatsFragment fragment = new RecentChatsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public RecentChatsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDbHelper = new LocalDbHelper(getActivity());
        Cursor cursor = mDbHelper.getRecentMessages();

        mAdapter = new ConversationCursorAdapter(getActivity(), cursor);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recent_chats, container, false);
        mListView = (ListView) view.findViewById(R.id.list_view_conversations);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String userId = (String) view.getTag(R.id.list_view_conversation_text);
                startChat(userId);
            }
        });
        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        App app = App.getInstance();
        app.getChatTransport().addChatEventListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        App app = App.getInstance();
        app.getChatTransport().removeChatEventListener(this);
    }

    private void startChat(String userId) {
        Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra(USER_ID, userId);
        startActivity(intent);
    }

    @Override
    public void onIncomingMsg(Message m) {
        Cursor cursor = mDbHelper.getRecentMessages();
        mAdapter.changeCursor(cursor);
    }

    @Override
    public void onOutgoingMsg(Message m) {
        Cursor cursor = mDbHelper.getRecentMessages();
        mAdapter.changeCursor(cursor);
    }

    @Override
    public void onStatusChanged(long contactId, boolean isOnline) {
    }

}
